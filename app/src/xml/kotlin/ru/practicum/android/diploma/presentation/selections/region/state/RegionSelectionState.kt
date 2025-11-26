package ru.practicum.android.diploma.presentation.selections.region.state

import ru.practicum.android.diploma.domain.models.Region

sealed interface RegionSelectionState {

    data class Content(val regions: List<Region>) : RegionSelectionState

    data object Empty : RegionSelectionState

    data object Error : RegionSelectionState

    data object NoInternet : RegionSelectionState

    data object Loading : RegionSelectionState

    data class RegionSelected(val country: Region) : RegionSelectionState
}
