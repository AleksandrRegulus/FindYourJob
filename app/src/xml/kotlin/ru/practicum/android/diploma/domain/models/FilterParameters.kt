package ru.practicum.android.diploma.domain.models

data class FilterParameters(
    val idIndustry: String = "",
    val nameIndustry: String = "",
    val idCountry: String = "",
    val nameCountry: String = "",
    val idRegion: String = "",
    val nameRegion: String = "",
    val salary: String = "",
    val doNotShowWithoutSalary: Boolean = false
)
