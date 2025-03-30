package ru.practicum.android.diploma.ui.selections.industry

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustrySelectionBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustryViewHolder(val binding: ItemIndustrySelectionBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(industry: Industry, isChecked: Boolean) {
        binding.rbIndustryName.text = industry.name
        binding.rbIndustryName.isChecked = isChecked
    }
}
