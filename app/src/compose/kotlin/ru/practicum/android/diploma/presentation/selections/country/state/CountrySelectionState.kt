package ru.practicum.android.diploma.presentation.selections.country.state

import ru.practicum.android.diploma.domain.models.Country

sealed interface CountrySelectionState {

    data class Content(val countries: List<Country>) : CountrySelectionState

    data object Empty : CountrySelectionState

    data object Error : CountrySelectionState

    data object Loading : CountrySelectionState

    data object NoInternet : CountrySelectionState
}
