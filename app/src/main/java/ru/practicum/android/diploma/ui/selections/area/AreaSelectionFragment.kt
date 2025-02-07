package ru.practicum.android.diploma.ui.selections.area

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentAreaSelectionBinding
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.presentation.selections.area.AreaSelectionViewModel
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import ru.practicum.android.diploma.ui.selections.region.RegionSelectionFragment

@AndroidEntryPoint
class AreaSelectionFragment : BindingFragment<FragmentAreaSelectionBinding>() {

    private val viewModel: AreaSelectionViewModel by viewModels()

    private var currentCountryId: String = ""

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAreaSelectionBinding {
        return FragmentAreaSelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()

        setFragmentResultListener(REQUEST_COUNTRY_KEY) { _, bundle ->
            viewModel.saveCountrySelection(
                countryId = bundle.getString(BUNDLE_ID_KEY) ?: "",
                countryName = bundle.getString(BUNDLE_NAME_KEY) ?: ""
            )
        }

        setFragmentResultListener(REQUEST_REGION_KEY) { _, bundle ->
            viewModel.saveRegionSelection(
                regionId = bundle.getString(BUNDLE_ID_KEY) ?: "",
                regionName = bundle.getString(BUNDLE_NAME_KEY) ?: ""
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.countryLayout.setOnClickListener {
            findNavController().navigate(R.id.action_areaSelectionFragment_to_countrySelectionFragment)
        }

        binding.regionLayout.setOnClickListener {
            openRegionSelectionFragment()
        }

        binding.icArrowForwardCountry.setOnClickListener {
            viewModel.saveCountrySelection("", "")
        }

        binding.icArrowForwardRegion.setOnClickListener {
            viewModel.saveRegionSelection("", "")
        }

        binding.buttonSelect.setOnClickListener {
            saveCountryAndRegion()
        }

        viewModel.selectionAreaState.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun setupBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

    private fun render(filterParameters: FilterParameters?) {
        if (filterParameters == null) {
            setEmptyCountry()
            setEmptyRegion()
        } else {
            if (filterParameters.nameCountry.isEmpty()) {
                currentCountryId = ""
                setEmptyCountry()
            } else {
                currentCountryId = filterParameters.idCountry
                setSelectedCountry(filterParameters.nameCountry)
            }
            if (filterParameters.nameRegion.isEmpty()) {
                setEmptyRegion()
            } else {
                setSelectedRegion(filterParameters.nameRegion)
            }
        }
    }

    private fun setSelectedCountry(countryName: String) {
        binding.countryNameTitle.visibility = View.GONE
        binding.countrySupportText.visibility = View.VISIBLE
        binding.icArrowForwardCountry.isSelected = true
        binding.countryName.visibility = View.VISIBLE
        binding.countryName.text = countryName
        showButtonSelect()
    }

    private fun setEmptyCountry() {
        binding.countryNameTitle.visibility = View.VISIBLE
        binding.countrySupportText.visibility = View.GONE
        binding.icArrowForwardCountry.isSelected = false
        binding.countryName.visibility = View.GONE
        binding.countryName.text = ""
        showButtonSelect()
    }

    private fun setSelectedRegion(regionName: String) {
        binding.regionNameTitle.visibility = View.GONE
        binding.regionSupportText.visibility = View.VISIBLE
        binding.icArrowForwardRegion.isSelected = true
        binding.regionName.visibility = View.VISIBLE
        binding.regionName.text = regionName
        showButtonSelect()
    }

    private fun setEmptyRegion() {
        binding.regionNameTitle.visibility = View.VISIBLE
        binding.regionSupportText.visibility = View.GONE
        binding.icArrowForwardRegion.isSelected = false
        binding.regionName.visibility = View.GONE
        binding.regionName.text = ""
        showButtonSelect()
    }

    private fun showButtonSelect() {
        if (binding.regionNameTitle.isVisible && binding.countryNameTitle.isVisible) {
            binding.buttonSelect.visibility = View.GONE
        } else {
            binding.buttonSelect.visibility = View.VISIBLE
        }
    }

    private fun openRegionSelectionFragment() {
        findNavController().navigate(
            R.id.action_areaSelectionFragment_to_regionSelectionFragment,
            RegionSelectionFragment.createArgs(currentCountryId)
        )
    }

    private fun saveCountryAndRegion() {
        viewModel.saveCountryAndRegionToSharedPref()
        findNavController().navigateUp()
    }

    companion object {
        private const val BUNDLE_ID_KEY = "ID"
        private const val BUNDLE_NAME_KEY = "NAME"
        const val REQUEST_COUNTRY_KEY = "REQUEST_COUNTRY_KEY"
        const val REQUEST_REGION_KEY = "REQUEST_REGION_KEY"

        fun createArgsCountrySelection(countryId: String, countryName: String): Bundle = bundleOf(
            BUNDLE_ID_KEY to countryId,
            BUNDLE_NAME_KEY to countryName
        )

        fun createArgsRegionSelection(regionId: String, regionName: String): Bundle = bundleOf(
            BUNDLE_ID_KEY to regionId,
            BUNDLE_NAME_KEY to regionName
        )
    }

}
