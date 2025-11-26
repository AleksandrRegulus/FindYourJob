package ru.practicum.android.diploma.presentation.vacancy

import ru.practicum.android.diploma.domain.models.VacancyDetails

sealed interface VacancyScreenState {
    data object Loading : VacancyScreenState

    data class Content(val vacancy: VacancyDetails) : VacancyScreenState

    data object ServerError : VacancyScreenState

    data object NoInternetConnection : VacancyScreenState
}
