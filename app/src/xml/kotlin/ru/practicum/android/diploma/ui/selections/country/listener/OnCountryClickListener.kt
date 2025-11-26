package ru.practicum.android.diploma.ui.selections.country.listener

import ru.practicum.android.diploma.domain.models.Country

fun interface OnCountryClickListener {
    fun onCountryClick(country: Country)
}
