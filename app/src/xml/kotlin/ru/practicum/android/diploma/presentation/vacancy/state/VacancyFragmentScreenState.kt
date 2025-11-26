package ru.practicum.android.diploma.presentation.vacancy.state

import ru.practicum.android.diploma.domain.models.VacancyDetails

sealed interface VacancyFragmentScreenState {
    data object Loading : VacancyFragmentScreenState

    data class Content(val vacancy: VacancyDetails) : VacancyFragmentScreenState

    data object ServerError : VacancyFragmentScreenState

    data object NoInternetConnection : VacancyFragmentScreenState
}
