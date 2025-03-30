package ru.practicum.android.diploma.ui.selections.industry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustrySelectionBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustryAdapter(
    private val itemClickListener: IndustryClickListener
) : RecyclerView.Adapter<IndustryViewHolder>() {

    private val industries = mutableListOf<Industry>()
    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIndustrySelectionBinding.inflate(inflater, parent, false)
        return IndustryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(industries[position], position == selectedPosition)

        holder.binding.rbIndustryName.setOnClickListener {
            if (position != selectedPosition) {
                itemClickListener.onIndustryClick(industries[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return industries.size
    }

    fun setIndustryList(newIndustryList: List<Industry>, newSelectedPos: Int) {
        industries.clear()
        industries.addAll(newIndustryList)
        selectedPosition = newSelectedPos
        notifyDataSetChanged()
    }

    interface IndustryClickListener {
        fun onIndustryClick(industry: Industry, position: Int)
    }
}
