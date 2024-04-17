package ru.practicum.android.diploma.ui.selections.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentCountrySelectionBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.presentation.selections.country.CountrySelectionViewModel
import ru.practicum.android.diploma.presentation.selections.country.state.CountrySelectionState
import ru.practicum.android.diploma.ui.appComponent
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.ui.selections.area.AreaSelectionFragment
import ru.practicum.android.diploma.ui.selections.country.adapter.CountryAdapter
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

class CountrySelectionFragment : BindingFragment<FragmentCountrySelectionBinding>() {

    @Inject
    lateinit var vmFactory: CountrySelectionViewModel.CountrySelectionViewModelFactory
    private lateinit var viewModel: CountrySelectionViewModel

    private val countryAdapter by lazy {
        val onCountryClickDebounce: (Country) -> Unit =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                onCountryClick(it)
            }
        CountryAdapter(onCountryClickDebounce)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCountrySelectionBinding {
        return FragmentCountrySelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.appComponent?.inject(this)
        viewModel = ViewModelProvider(this, vmFactory)[CountrySelectionViewModel::class.java]

        initUi()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCountries()
        viewModel.getCountrySelectionState().observe(viewLifecycleOwner) {
            renderCountrySelectionState(it)
        }
    }

    private fun initUi() {
        setupBackButton()
        initBtnBack()
        initCountryRecyclerView()
    }

    private fun setupBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackButtonClick()
            }
        })
    }

    private fun initBtnBack() {
        binding.btnBack.setOnClickListener {
            onBackButtonClick()
        }
    }

    private fun initCountryRecyclerView() {
        binding.countryRecyclerView.adapter = countryAdapter
        binding.countryRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun onBackButtonClick() {
        findNavController().navigateUp()
    }

    private fun onCountryClick(country: Country) {
        setFragmentResult(
            AreaSelectionFragment.REQUEST_COUNTRY_KEY,
            AreaSelectionFragment.createArgsCountrySelection(country.id, country.name)
        )
        onBackButtonClick()
    }

    private fun renderCountrySelectionState(state: CountrySelectionState) {
        when (state) {
            is CountrySelectionState.Content -> {
                showContent(state.countries)
            }

            is CountrySelectionState.Empty -> {
                renderEmpty()
            }

            is CountrySelectionState.Error -> {
                renderError()
            }

            is CountrySelectionState.Loading -> {
                renderLoading()
            }

            is CountrySelectionState.NoInternet -> {
                renderNoInternet()
            }
        }
    }

    private fun renderEmpty() {
        hideCountryRecyclerView()
        hideLoader()
        showLLErrorServer(
            imageResource = R.drawable.empty_favorites,
            titleResource = R.string.countries_are_empty
        )
    }

    private fun renderLoading() {
        showLoader()
        hideCountryRecyclerView()
        hideLLErrorServer()
    }

    private fun renderNoInternet() {
        hideCountryRecyclerView()
        hideLoader()
        showLLErrorServer(
            imageResource = R.drawable.png_no_internet,
            titleResource = R.string.no_internet
        )
    }

    private fun showCountryRecyclerView() {
        binding.countryRecyclerView.visibility = View.VISIBLE
    }

    private fun hideCountryRecyclerView() {
        binding.countryRecyclerView.visibility = View.GONE
    }

    private fun showLLErrorServer(@DrawableRes imageResource: Int, @StringRes titleResource: Int) {
        binding.llErrorServer.visibility = View.VISIBLE
        binding.placeholderImageView.setBackgroundResource(imageResource)
        binding.placeholderTextView.setText(titleResource)
    }

    private fun hideLLErrorServer() {
        binding.llErrorServer.visibility = View.GONE
    }

    private fun showLoader() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showContent(countries: List<Country>) {
        showCountryRecyclerView()
        hideLoader()
        hideLLErrorServer()
        countryAdapter.countries.clear()
        countryAdapter.countries.addAll(countries)
        countryAdapter.notifyDataSetChanged()
    }

    private fun renderError() {
        hideCountryRecyclerView()
        hideLoader()
        showLLErrorServer(
            imageResource = R.drawable.png_no_regions,
            titleResource = R.string.failed_to_retrieve_list
        )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
