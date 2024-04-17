package ru.practicum.android.diploma.ui.filter

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.presentation.filter.FilterSettingsViewModel
import ru.practicum.android.diploma.presentation.filter.state.InitialState
import ru.practicum.android.diploma.ui.appComponent
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import javax.inject.Inject

class FilterSettingsFragment : BindingFragment<FragmentFilterSettingsBinding>() {

    @Inject
    lateinit var vmFactory: FilterSettingsViewModel.FilterSettingsViewModelFactory
    private lateinit var viewModel: FilterSettingsViewModel

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterSettingsBinding {
        return FragmentFilterSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.appComponent?.inject(this)
        viewModel = ViewModelProvider(this, vmFactory)[FilterSettingsViewModel::class.java]

        setupBackButton()
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.industryField.root.setOnClickListener {
            findNavController().navigate(R.id.action_filterSettingsFragment_to_industrySelectionFragment)
        }
        binding.areaField.root.setOnClickListener {
            findNavController().navigate(R.id.action_filterSettingsFragment_to_areaSelectionFragment)
        }
        binding.areaField.arrowForwardButton.setOnClickListener {
            if (binding.areaField.arrowForwardButton.isSelected) {
                viewModel.deleteAreaInFilterParameters()
            }
        }
        binding.industryField.arrowForwardButton.setOnClickListener {
            if (binding.industryField.arrowForwardButton.isSelected) {
                viewModel.deleteIndustryFilterParameters()
            }
        }
        binding.inputEditText.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotBlank()) {
                viewModel.isSalaryChosen(value = true)
            } else {
                viewModel.isSalaryChosen(value = false)
            }
        }
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.doNotShowWithoutSalary(value = isChecked)
        }
        binding.applyButton.setOnClickListener {
            viewModel.saveOthers(
                salary = binding.inputEditText.text.toString(),
                flag = binding.checkBox.isChecked
            )
            findNavController().navigateUp()
        }
        binding.resetButton.setOnClickListener {
            viewModel.deleteFilterParameters()
        }
        viewModel.getFilterParameters()
        viewModel.filterParameters.observe(viewLifecycleOwner) { filterParameters ->
            if (filterParameters != null) {
                renderUI(filterParameters = filterParameters)
            } else {
                cleanUI()
            }
        }
        viewModel.initialState.observe(viewLifecycleOwner) {
            if (checkIfTheStateIsDefault(it)) {
                hideButtons()
                if (viewModel.filterParameters.value != null) {
                    viewModel.deleteFilterParameters()
                }
            } else {
                showButtons()
            }
        }
    }

    private fun setupBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

    private fun checkIfTheStateIsDefault(initialState: InitialState): Boolean {
        return !initialState.isIndustryChosen
            && !initialState.isAreaChosen
            && !initialState.isSalaryChosen
            && !initialState.doNotShowWithoutSalary
    }

    private fun renderUI(filterParameters: FilterParameters) {
        if (filterParameters.nameCountry.isNotBlank() || filterParameters.nameRegion.isNotBlank()) {
            renderArea(nameCountry = filterParameters.nameCountry, nameRegion = filterParameters.nameRegion)
        } else {
            cleanArea()
        }
        if (filterParameters.nameIndustry.isNotBlank()) {
            renderIndustry(nameIndustry = filterParameters.nameIndustry)
        } else {
            cleanIndustry()
        }
        if (filterParameters.salary.isNotBlank()) {
            renderSalary(salary = filterParameters.salary)
        } else {
            cleanSalary()
        }
        renderCheckBox(filterParameters.doNotShowWithoutSalary)
    }

    private fun cleanUI() {
        cleanArea()
        cleanIndustry()
        binding.inputEditText.setText("")
        binding.checkBox.isChecked = false
    }

    private fun renderArea(nameCountry: String, nameRegion: String) {
        binding.areaField.bigTitleTextView.isVisible = false
        binding.areaField.smallTitleTextView.isVisible = true
        binding.areaField.smallTitleTextView.text = getString(R.string.area)
        binding.areaField.smallValueTextView.isVisible = true
        binding.areaField.arrowForwardButton.isSelected = true
        if (nameCountry.isNotBlank() && nameRegion.isBlank()) {
            binding.areaField.smallValueTextView.text = nameCountry
        } else if (nameCountry.isBlank() && nameRegion.isNotBlank()) {
            binding.areaField.smallValueTextView.text = nameRegion
        } else {
            binding.areaField.smallValueTextView.text = "$nameCountry, $nameRegion"
        }
        viewModel.isAreaChosen(value = true)
    }

    private fun renderIndustry(nameIndustry: String) {
        binding.industryField.bigTitleTextView.isVisible = false
        binding.industryField.smallTitleTextView.isVisible = true
        binding.industryField.smallTitleTextView.text = getString(R.string.industry)
        binding.industryField.smallValueTextView.isVisible = true
        binding.industryField.smallValueTextView.text = nameIndustry
        binding.industryField.arrowForwardButton.isSelected = true
        viewModel.isIndustryChosen(value = true)
    }

    private fun renderSalary(salary: String) {
        binding.inputEditText.setText(salary)
        viewModel.isSalaryChosen(value = true)
        val statesForFloatingHint = if (salary.isBlank()) {
            arrayOf(
                intArrayOf(android.R.attr.state_focused),
                intArrayOf(-android.R.attr.state_focused),
            )
        } else {
            arrayOf(
                intArrayOf(android.R.attr.state_focused),
                intArrayOf(-android.R.attr.state_focused),
            )
        }
        val colorsForFloatingHint = if (salary.isBlank()) {
            intArrayOf(
                resources.getColor(R.color.blue, null),
                resources.getColor(R.color.gray_day_white_night, null),
            )
        } else {
            intArrayOf(
                resources.getColor(R.color.blue, null),
                resources.getColor(R.color.black, null),
            )
        }
        binding.textInputLayout.defaultHintTextColor = ColorStateList(statesForFloatingHint, colorsForFloatingHint)
    }

    private fun cleanArea() {
        binding.areaField.bigTitleTextView.isVisible = true
        binding.areaField.bigTitleTextView.text = getString(R.string.area)
        binding.areaField.smallTitleTextView.isVisible = false
        binding.areaField.smallValueTextView.isVisible = false
        binding.areaField.arrowForwardButton.isSelected = false
        viewModel.isAreaChosen(value = false)
    }

    private fun cleanIndustry() {
        binding.industryField.bigTitleTextView.isVisible = true
        binding.industryField.bigTitleTextView.text = getString(R.string.industry)
        binding.industryField.smallTitleTextView.isVisible = false
        binding.industryField.smallValueTextView.isVisible = false
        binding.industryField.arrowForwardButton.isSelected = false
        viewModel.isIndustryChosen(value = false)
    }

    private fun cleanSalary() {
        binding.inputEditText.setText("")
        viewModel.isSalaryChosen(value = false)
    }

    private fun renderCheckBox(doNotShowWithoutSalary: Boolean) {
        binding.checkBox.isChecked = doNotShowWithoutSalary
        viewModel.doNotShowWithoutSalary(value = doNotShowWithoutSalary)
    }

    private fun showButtons() {
        binding.buttonsConstraintLayout.isVisible = true
    }

    private fun hideButtons() {
        binding.buttonsConstraintLayout.isVisible = false
    }
}
