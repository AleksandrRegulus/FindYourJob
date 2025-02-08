package ru.practicum.android.diploma.ui.similar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.similar.SimilarVacanciesViewModel
import ru.practicum.android.diploma.presentation.similar.state.SimilarVacanciesFragmentState
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.ui.search.VacancyAdapter
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.debounce

@AndroidEntryPoint
class SimilarVacanciesFragment : BindingFragment<FragmentSimilarVacanciesBinding>() {

    private val viewModel: SimilarVacanciesViewModel by viewModels()

    private val adapter by lazy {
        VacancyAdapter(object : VacancyAdapter.VacancyClickListener {
            override fun onVacancyClick(vacancy: Vacancy) {
                val onVacancyClickDebounce =
                    debounce<Vacancy>(
                        CLICK_DEBOUNCE_DELAY,
                        viewLifecycleOwner.lifecycleScope,
                        false
                    ) { openVacancyFragment(it) }

                onVacancyClickDebounce(vacancy)
            }
        })
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSimilarVacanciesBinding {
        return FragmentSimilarVacanciesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSimilarVacancies(
            requireArguments().getString(ARGS_SIMILAR_VACANCY_ID).toString()
        )

        binding.rvSimilarVacancies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSimilarVacancies.adapter = adapter

        binding.rvSimilarVacancies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos =
                        (binding.rvSimilarVacancies.layoutManager as LinearLayoutManager)
                            .findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.getNextPage()
                    }
                }
            }
        })

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getSimilarVacanciesFragmentState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: SimilarVacanciesFragmentState) {
        when (state) {
            is SimilarVacanciesFragmentState.Content -> showContent(state.vacancies)
            is SimilarVacanciesFragmentState.Empty -> showEmpty()
            is SimilarVacanciesFragmentState.Error -> showError(state.isFirstPage)
            is SimilarVacanciesFragmentState.NoInternet -> showNoInternet(state.isFirstPage)
            is SimilarVacanciesFragmentState.Loading -> showLoading(state.isFirstPage)
        }
    }

    private fun showLoading(isFirstPage: Boolean) {
        binding.placeHolderError.visibility = View.GONE
        if (isFirstPage) {
            binding.rvSimilarVacancies.visibility = View.GONE
            binding.progressBarMain.visibility = View.VISIBLE
        } else {
            binding.progressBarPaging.visibility = View.VISIBLE
        }
    }

    private fun showError(isFirstPage: Boolean) {
        hideViews()
        if (isFirstPage) {
            binding.rvSimilarVacancies.visibility = View.GONE
            binding.placeHolderError.visibility = View.VISIBLE
            binding.messageError.text = getString(R.string.error_server)
            binding.imageError.setImageResource(R.drawable.error_server)
        } else {
            onSnack(resources.getString(R.string.error_occurred))
            viewModel.getContent()
        }
    }

    private fun showNoInternet(isFirstPage: Boolean) {
        hideViews()
        if (isFirstPage) {
            binding.rvSimilarVacancies.visibility = View.GONE
            binding.placeHolderError.visibility = View.VISIBLE
            binding.messageError.text = getString(R.string.no_internet)
            binding.imageError.setImageResource(R.drawable.png_no_internet)
        } else {
            onSnack(resources.getString(R.string.check_internet))
            viewModel.getContent()
        }
    }

    private fun onSnack(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        val textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }

    private fun hideViews() {
        binding.progressBarMain.visibility = View.GONE
        binding.progressBarPaging.visibility = View.GONE
    }

    private fun showEmpty() {
        binding.progressBarMain.visibility = View.GONE
        binding.progressBarPaging.visibility = View.GONE
        binding.rvSimilarVacancies.visibility = View.GONE
        binding.messageError.text = getString(R.string.nothing_found_description)
        binding.imageError.setImageResource(R.drawable.png_nothing_found)
        binding.placeHolderError.visibility = View.VISIBLE
    }

    private fun showContent(vacancies: List<Vacancy>) {
        binding.progressBarMain.visibility = View.GONE
        binding.progressBarPaging.visibility = View.GONE
        binding.placeHolderError.visibility = View.GONE
        adapter.setVacancyList(vacancies)
        binding.rvSimilarVacancies.visibility = View.VISIBLE
    }

    private fun openVacancyFragment(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_similarVacancyFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id, VacancyFragment.SEARCH_FRAGMENT_ORIGIN)
        )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 0L
        private const val ARGS_SIMILAR_VACANCY_ID = "ARGS_SIMILAR_VACANCY_ID"

        fun createArgs(vacancyId: String): Bundle = bundleOf(
            ARGS_SIMILAR_VACANCY_ID to vacancyId,
        )
    }
}
