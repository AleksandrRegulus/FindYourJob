package ru.practicum.android.diploma.presentation.similar.state

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SimilarVacanciesFragmentState {

    data class Loading(val isFirstPage: Boolean) : SimilarVacanciesFragmentState
    data class Content(val vacancies: List<Vacancy>) : SimilarVacanciesFragmentState
    data class Error(val isFirstPage: Boolean) : SimilarVacanciesFragmentState
    data class NoInternet(val isFirstPage: Boolean) : SimilarVacanciesFragmentState
    data object Empty : SimilarVacanciesFragmentState
}
