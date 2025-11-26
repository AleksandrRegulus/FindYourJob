package ru.practicum.android.diploma.domain.models

data class VacanciesRequest(
    val text: String? = null,
    val area: String? = null,
    val industry: String? = null,
    val currency: String? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false
)
