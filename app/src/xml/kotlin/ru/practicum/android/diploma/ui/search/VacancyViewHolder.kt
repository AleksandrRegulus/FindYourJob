package ru.practicum.android.diploma.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemFoundVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.getSalaryStringAndSymbol

class VacancyViewHolder(
    private val binding: ItemFoundVacancyBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: Vacancy, vacancyClickListener: VacancyAdapter.VacancyClickListener?) {
        binding.vacancyName.text = createVacancyTitle(vacancy.name, vacancy.areaName)
        binding.employerName.text = vacancy.employerName
        binding.salary.text = getSalaryStringAndSymbol(vacancy.salary, itemView.context)
        Glide
            .with(itemView)
            .load(vacancy.employerLogoUrl)
            .placeholder(R.drawable.ic_placeholder_vacancy_item)
            .centerCrop()
            .transform(RoundedCorners(binding.employerLogo.resources.getDimensionPixelSize(R.dimen._12dp)))
            .into(binding.employerLogo)
        itemView.setOnClickListener {
            vacancyClickListener?.onVacancyClick(vacancy)
        }
    }

    private fun createVacancyTitle(name: String, place: String): String {
        return "$name, $place"
    }
}
