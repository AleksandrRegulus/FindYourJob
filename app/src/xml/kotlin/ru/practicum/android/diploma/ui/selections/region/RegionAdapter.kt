package ru.practicum.android.diploma.ui.selections.region

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryRegionSelectionBinding
import ru.practicum.android.diploma.domain.models.Region

class RegionAdapter(private val onRegionClickListener: (Region) -> Unit) :
    RecyclerView.Adapter<RegionAdapter.RegionViewHolder>() {

    private val countries = mutableListOf<Region>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCountryRegionSelectionBinding.inflate(inflater, parent, false)
        return RegionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        holder.bind(countries[position])
        holder.itemView.setOnClickListener {
            onRegionClickListener.invoke(countries[position])
        }
    }

    override fun getItemCount(): Int = countries.size

    fun setItems(items: List<Region>) {
        countries.clear()
        countries.addAll(items)
        notifyDataSetChanged()
    }

    class RegionViewHolder(
        private val binding: ItemCountryRegionSelectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(country: Region) {
            binding.countryName.text = country.name
        }
    }
}
