package ru.practicum.android.diploma.presentation.search.state

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SearchFragmentState {

    data class Loading(val isFirstPage: Boolean) : SearchFragmentState

    data class Content(val vacancies: List<Vacancy>, val found: Int) : SearchFragmentState

    data class Error(val isFirstPage: Boolean) : SearchFragmentState

    data class NoInternet(val isFirstPage: Boolean) : SearchFragmentState

    data object Empty : SearchFragmentState

    data object Start : SearchFragmentState
}
