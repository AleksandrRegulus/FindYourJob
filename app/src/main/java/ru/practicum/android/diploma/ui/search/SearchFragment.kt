package ru.practicum.android.diploma.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.search.state.SearchFragmentState
import ru.practicum.android.diploma.ui.appComponent
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    @Inject
    lateinit var vmFactory: SearchViewModel.SearchViewModelFactory
    private lateinit var viewModel: SearchViewModel

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
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.appComponent?.inject(this)
        viewModel = ViewModelProvider(this, vmFactory)[SearchViewModel::class.java]

        binding.InputEditText.doOnTextChanged { text, _, _, _ ->
            binding.placeHolderError.visibility = View.GONE
            viewModel.search(text = binding.InputEditText.text.toString(), withDelay = true)

            if (text.isNullOrEmpty()) {
                viewModel.cancelSearch()
                binding.icClose.setImageResource(R.drawable.ic_search)
                binding.icClose.isClickable = false
            } else {
                binding.icClose.isClickable = true
                binding.icClose.setImageResource(R.drawable.ic_close)
            }
        }

        binding.recyclerViewFoundVacancies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewFoundVacancies.adapter = adapter

        binding.recyclerViewFoundVacancies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos =
                        (binding.recyclerViewFoundVacancies.layoutManager as LinearLayoutManager)
                            .findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.getNextPage()
                    }
                }
            }
        })

        binding.icClose.setOnClickListener {
            binding.InputEditText.setText("")
        }

        viewModel.getSearchFragmentScreenState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.filters.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment_to_filterSettingsFragment
            )
        }
        viewModel.getFilterParameters()
        viewModel.filterParameters.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.filters.setImageResource(R.drawable.ic_filter_on)
                viewModel.search(text = binding.InputEditText.text.toString(), withDelay = false)
            } else {
                binding.filters.setImageResource(R.drawable.ic_filter_off)
            }
        }
    }

    private fun render(state: SearchFragmentState) {
        when (state) {
            is SearchFragmentState.Content -> showContent(state.vacancies, state.found)
            is SearchFragmentState.Empty -> showEmpty()
            is SearchFragmentState.Error -> showError(state.isFirstPage)
            is SearchFragmentState.NoInternet -> showNoInternet(state.isFirstPage)
            is SearchFragmentState.Loading -> showLoading(state.isFirstPage)
            is SearchFragmentState.Start -> showStart()
        }
    }

    private fun showStart() {
        binding.progressBarMain.visibility = View.GONE
        binding.progressBarPaging.visibility = View.GONE
        binding.recyclerViewFoundVacancies.visibility = View.GONE
        binding.ImageSearch.visibility = View.VISIBLE
        binding.messageFound.visibility = View.GONE
        binding.placeHolderError.visibility = View.GONE
    }

    private fun showLoading(isFirstPage: Boolean) {
        hideKeyBoard()
        binding.placeHolderError.visibility = View.GONE
        binding.ImageSearch.visibility = View.GONE
        if (isFirstPage) {
            binding.recyclerViewFoundVacancies.visibility = View.GONE
            binding.messageFound.visibility = View.GONE
            binding.progressBarMain.visibility = View.VISIBLE
        } else {
            binding.progressBarPaging.visibility = View.VISIBLE
        }
    }

    private fun showError(isFirstPage: Boolean) {
        hideViews()
        if (isFirstPage) {
            binding.recyclerViewFoundVacancies.visibility = View.GONE
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
            binding.recyclerViewFoundVacancies.visibility = View.GONE
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
        hideKeyBoard()
        binding.progressBarMain.visibility = View.GONE
        binding.progressBarPaging.visibility = View.GONE
        binding.ImageSearch.visibility = View.GONE
        binding.messageFound.visibility = View.GONE
    }

    private fun showEmpty() {
        binding.progressBarMain.visibility = View.GONE
        binding.progressBarPaging.visibility = View.GONE
        binding.recyclerViewFoundVacancies.visibility = View.GONE
        binding.ImageSearch.visibility = View.GONE
        binding.messageFound.text = getString(R.string.nothing_found)
        binding.messageFound.visibility = View.VISIBLE
        binding.messageError.text = getString(R.string.nothing_found_description)
        binding.imageError.setImageResource(R.drawable.png_nothing_found)
        binding.placeHolderError.visibility = View.VISIBLE
    }

    private fun showContent(vacancies: List<Vacancy>, found: Int) {
        hideKeyBoard()
        binding.progressBarMain.visibility = View.GONE
        binding.progressBarPaging.visibility = View.GONE
        binding.ImageSearch.visibility = View.GONE
        binding.placeHolderError.visibility = View.GONE
        binding.messageFound.text =
            resources.getQuantityString(R.plurals.vacancy_plurals, found, found)
        binding.messageFound.visibility = View.VISIBLE
        adapter.setVacancyList(vacancies)
        binding.recyclerViewFoundVacancies.visibility = View.VISIBLE
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.InputEditText.windowToken, 0)
    }

    private fun openVacancyFragment(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id, VacancyFragment.SEARCH_FRAGMENT_ORIGIN)
        )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 0L
    }
}
