package ru.practicum.android.diploma.domain.models

data class VacanciesRequest(
    val page: Int = 0,
    val perPage: Int = 20,
    val text: String? = null,
    val area: String? = null,
    val industry: String? = null,
    val currency: String? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false
)
