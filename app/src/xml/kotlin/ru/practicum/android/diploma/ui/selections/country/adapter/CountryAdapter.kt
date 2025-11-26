package ru.practicum.android.diploma.ui.selections.country.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.practicum.android.diploma.databinding.ItemCountryRegionSelectionBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.ui.selections.country.listener.OnCountryClickListener

class CountryAdapter(private val onCountryClickListener: OnCountryClickListener) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    val countries = mutableListOf<Country>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCountryRegionSelectionBinding.inflate(inflater, parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
        holder.itemView.setOnClickListener {
            onCountryClickListener.onCountryClick(countries[position])
        }
    }

    override fun getItemCount(): Int = countries.size

    class CountryViewHolder(private val binding: ItemCountryRegionSelectionBinding) : ViewHolder(binding.root) {

        fun bind(country: Country) {
            binding.countryName.text = country.name
        }
    }
}
