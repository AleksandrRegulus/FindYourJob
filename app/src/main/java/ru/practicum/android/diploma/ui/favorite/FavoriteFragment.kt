package ru.practicum.android.diploma.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.favorite.FavoriteViewModel
import ru.practicum.android.diploma.presentation.favorite.state.FavoritesState
import ru.practicum.android.diploma.ui.appComponent
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.ui.search.VacancyAdapter
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

class FavoriteFragment : BindingFragment<FragmentFavoriteBinding>() {

    @Inject
    lateinit var vmFactory: FavoriteViewModel.FavoriteViewModelFactory
    private lateinit var viewModel: FavoriteViewModel

    private var adapter: VacancyAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.appComponent?.inject(this)
        viewModel = ViewModelProvider(this, vmFactory)[FavoriteViewModel::class.java]

        val onVacancyClickDebounce =
            debounce<Vacancy>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { openVacancyFragment(it) }

        adapter = VacancyAdapter(
            object : VacancyAdapter.VacancyClickListener {
                override fun onVacancyClick(vacancy: Vacancy) {
                    onVacancyClickDebounce.invoke(vacancy)
                }
            }
        )

        binding.favoritesRecyclerView.adapter = adapter
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )

        viewModel.favoritesStateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(favoritesState: FavoritesState) {
        with(binding) {
            when (favoritesState) {
                is FavoritesState.Content -> {
                    adapter?.setVacancyList(favoritesState.favorites)
                    favoritesRecyclerView.isVisible = true
                    emptyFavoritesConstraintLayout.isVisible = false
                    errorFavoritesConstraintLayout.isVisible = false
                }

                FavoritesState.Empty -> {
                    favoritesRecyclerView.isVisible = false
                    emptyFavoritesConstraintLayout.isVisible = true
                    errorFavoritesConstraintLayout.isVisible = false
                }

                FavoritesState.Error -> {
                    favoritesRecyclerView.isVisible = false
                    emptyFavoritesConstraintLayout.isVisible = false
                    errorFavoritesConstraintLayout.isVisible = true
                }
            }
        }
    }

    private fun openVacancyFragment(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_favoriteFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id, VacancyFragment.FAVORITE_FRAGMENT_ORIGIN)
        )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 0L
    }
}
