package ru.practicum.android.diploma.ui.selections.industry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustrySelectionBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.selections.industry.IndustrySelectionViewModel
import ru.practicum.android.diploma.presentation.selections.industry.state.IndustrySelectionState
import ru.practicum.android.diploma.ui.appComponent
import ru.practicum.android.diploma.ui.fragment.BindingFragment
import javax.inject.Inject

class IndustrySelectionFragment : BindingFragment<FragmentIndustrySelectionBinding>() {

    @Inject
    lateinit var vmFactory: IndustrySelectionViewModel.IndustrySelectionViewModelFactory
    private lateinit var viewModel: IndustrySelectionViewModel

    private val adapter = IndustryAdapter(
        object : IndustryAdapter.IndustryClickListener {
            override fun onIndustryClick(industry: Industry, position: Int) {
                viewModel.saveCheckedIndustry(industry, position)
            }
        }
    )

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentIndustrySelectionBinding {
        return FragmentIndustrySelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.appComponent?.inject(this)
        viewModel = ViewModelProvider(this, vmFactory)[IndustrySelectionViewModel::class.java]

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.inputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.filter(searchQuery = text.toString())

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

        binding.buttonConfirm.setOnClickListener {
            viewModel.saveIndustry()
            findNavController().navigateUp()
        }

        binding.rvIndustries.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvIndustries.adapter = adapter

        viewModel.getIndustriesFragmentState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: IndustrySelectionState) {
        when (state) {
            is IndustrySelectionState.NoInternet -> {
                showNoInternet()
            }

            is IndustrySelectionState.Error -> {
                showError()
            }

            is IndustrySelectionState.Content -> {
                showContent(state.industries, state.selectedPos)
            }

            is IndustrySelectionState.Loading -> {
                showLoading()
            }

            is IndustrySelectionState.Empty -> {
                showEmptiness()
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.llErrorPlaceholder.isVisible = false
        binding.rvIndustries.isVisible = false
    }

    private fun showError() {
        binding.progressBar.isVisible = false
        binding.llErrorPlaceholder.isVisible = true
        binding.rvIndustries.isVisible = false
        binding.imageError.setImageResource(R.drawable.png_no_regions)
        binding.textError.setText(R.string.failed_to_retrieve_list)
        binding.inputEditText.isEnabled = false
    }

    private fun showNoInternet() {
        binding.progressBar.isVisible = false
        binding.llErrorPlaceholder.isVisible = true
        binding.rvIndustries.isVisible = false
        binding.imageError.setImageResource(R.drawable.png_no_internet)
        binding.textError.setText(R.string.no_internet)
        binding.inputEditText.isEnabled = false
    }

    private fun showContent(industries: List<Industry>, selectedPos: Int) {
        binding.progressBar.isVisible = false
        binding.llErrorPlaceholder.isVisible = false
        binding.rvIndustries.isVisible = true
        adapter.setIndustryList(industries, selectedPos)
        binding.buttonConfirm.isVisible = selectedPos > -1
    }

    private fun showEmptiness() {
        binding.progressBar.isVisible = false
        binding.buttonConfirm.isVisible = false
        binding.llErrorPlaceholder.isVisible = true
        binding.rvIndustries.isVisible = false
        binding.imageError.setImageResource(R.drawable.png_nothing_found)
        binding.textError.setText(R.string.nothing_found_industries)
    }
}
