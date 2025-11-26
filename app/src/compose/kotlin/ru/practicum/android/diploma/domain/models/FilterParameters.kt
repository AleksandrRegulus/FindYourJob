package ru.practicum.android.diploma.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class FilterParameters(
    val idIndustry: String,
    val nameIndustry: String,
    val idCountry: String,
    val nameCountry: String,
    val idRegion: String,
    val nameRegion: String,
    val salary: String,
    val doNotShowWithoutSalary: Boolean
) {

    companion object {
        val initial: FilterParameters = FilterParameters(
            idIndustry = "",
            nameIndustry = "",
            idCountry = "",
            nameCountry = "",
            idRegion = "",
            nameRegion = "",
            salary = "",
            doNotShowWithoutSalary = false
        )
    }
}
