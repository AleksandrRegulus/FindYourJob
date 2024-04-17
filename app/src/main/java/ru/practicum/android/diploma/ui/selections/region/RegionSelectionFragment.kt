package ru.practicum.android.diploma.ui.selections.region

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionSelectionBinding
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.selections.region.RegionSelectionViewModel
import ru.practicum.android.diploma.presentation.selections.region.state.RegionSelectionState
import ru.practicum.android.diploma.ui.appComponent
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.ui.selections.area.AreaSelectionFragment
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

class RegionSelectionFragment : BindingFragment<FragmentRegionSelectionBinding>() {

    @Inject
    lateinit var vmFactory: RegionSelectionViewModel.RegionSelectionViewModelFactory
    private lateinit var viewModel: RegionSelectionViewModel

    private var selectedRegion: Region? = null

    private val regionAdapter by lazy {
        val onClickDebounce: (Region) -> Unit =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                onRegionClick(it)
            }
        RegionAdapter(onClickDebounce)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegionSelectionBinding {
        return FragmentRegionSelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.appComponent?.inject(this)
        viewModel = ViewModelProvider(this, vmFactory)[RegionSelectionViewModel::class.java]

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        initRegionRecyclerView()

        val countryId = requireArguments().getString(ARGS_COUNTRY_ID, "").toString()
        if (countryId.isEmpty()) {
            viewModel.getRegions()
        } else {
            viewModel.getRegionsOfCountry(countryId)
        }

        binding.inputEditText.doOnTextChanged { text, start, before, count ->
            viewModel.filterRegions(text.toString())

            if (text.isNullOrBlank()) {
                binding.icClose.setImageResource(R.drawable.ic_search)
                binding.icClose.isClickable = false
            } else {
                binding.icClose.setImageResource(R.drawable.ic_close)
                binding.icClose.isClickable = true
            }
        }

        binding.icClose.setOnClickListener {
            if (binding.inputEditText.isEnabled) {
                binding.inputEditText.setText("")
            }
        }

        viewModel.regionsStateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun initRegionRecyclerView() {
        binding.regionRecyclerView.adapter = regionAdapter
        binding.regionRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun onRegionClick(region: Region) {
        selectedRegion = region
        viewModel.getCountryByRegion(region)
    }

    private fun render(state: RegionSelectionState) {
        when (state) {
            is RegionSelectionState.Content -> {
                renderContent(state.regions)
            }

            is RegionSelectionState.Empty -> {
                renderEmptiness()
            }

            is RegionSelectionState.Error -> {
                renderError()
            }

            is RegionSelectionState.NoInternet -> {
                renderNoInternet()
            }

            is RegionSelectionState.Loading -> {
                renderLoading()
            }

            is RegionSelectionState.RegionSelected -> {
                returnFragmentResult(state.country)
            }
        }
    }

    private fun returnFragmentResult(country: Region) {
        setFragmentResult(
            AreaSelectionFragment.REQUEST_REGION_KEY,
            AreaSelectionFragment.createArgsRegionSelection(
                selectedRegion?.id ?: "",
                selectedRegion?.name ?: ""
            )
        )

        val countryId = requireArguments().getString(ARGS_COUNTRY_ID, "").toString()
        if (countryId.isEmpty()) {
            setFragmentResult(
                AreaSelectionFragment.REQUEST_COUNTRY_KEY,
                AreaSelectionFragment.createArgsCountrySelection(country.id, country.name)
            )
        }
        findNavController().navigateUp()
    }

    private fun renderContent(regions: List<Region>) {
        binding.regionRecyclerView.isVisible = true
        binding.llErrorPlaceholder.isVisible = false
        binding.progressBar.isVisible = false
        regionAdapter.setItems(regions)
    }

    private fun renderEmptiness() {
        binding.regionRecyclerView.isVisible = false
        binding.llErrorPlaceholder.isVisible = true
        binding.progressBar.isVisible = false
        binding.imageError.setImageResource(R.drawable.png_nothing_found)
        binding.textError.setText(R.string.nothing_found_regions)
    }

    private fun renderError() {
        binding.regionRecyclerView.isVisible = false
        binding.llErrorPlaceholder.isVisible = true
        binding.progressBar.isVisible = false
        binding.imageError.setImageResource(R.drawable.png_no_regions)
        binding.textError.setText(R.string.failed_to_retrieve_list)
        binding.inputEditText.isEnabled = false
    }

    private fun renderNoInternet() {
        binding.regionRecyclerView.isVisible = false
        binding.llErrorPlaceholder.isVisible = true
        binding.progressBar.isVisible = false
        binding.imageError.setImageResource(R.drawable.png_no_internet)
        binding.textError.setText(R.string.no_internet)
        binding.inputEditText.isEnabled = false
    }

    private fun renderLoading() {
        binding.regionRecyclerView.isVisible = false
        binding.llErrorPlaceholder.isVisible = false
        binding.progressBar.isVisible = true
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 0L
        private const val ARGS_COUNTRY_ID = "ARGS_COUNTRY_ID"

        fun createArgs(countryId: String): Bundle = bundleOf(
            ARGS_COUNTRY_ID to countryId,
        )
    }
}
