package ru.practicum.android.diploma.presentation.selections.region

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.selections.region.state.RegionSelectionState
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

@HiltViewModel
class RegionSelectionViewModel @Inject constructor(
    private val filterSearchInteractor: FilterSearchInteractor,
) : ViewModel() {

    private val _filterParametersState = MutableStateFlow(FilterParameters.initial)

    private val _regionScreenState = MutableStateFlow<RegionSelectionState>(RegionSelectionState.Loading)
    val regionScreenState: StateFlow<RegionSelectionState> = _regionScreenState

    private val foundRegions = mutableListOf<Region>()
    private val allRegions = mutableListOf<Region>()

    init {
        viewModelScope.launch {
            filterSearchInteractor.readFilterParameters().collect { filterParams ->
                _filterParametersState.value = filterParams
                getRegions(filterParams.idCountry)
            }
        }
    }

    private suspend fun getRegions(idCountry: String) {
        filterSearchInteractor
            .getRegionsOfCountry(idCountry)
            .catch {
                emit(SearchResult.Error())
            }
            .collect { searchResult ->
                when (searchResult) {
                    is SearchResult.Success -> {
                        if (searchResult.data.isEmpty()) {
                            renderRegionSelectionState(RegionSelectionState.Empty)
                        } else {
                            foundRegions.addAll(searchResult.data.filterNot { it.parentId.isNullOrEmpty() })
                            renderRegionSelectionState(
                                RegionSelectionState.Content(regions = foundRegions)
                            )
                            allRegions.addAll(searchResult.data)
                        }
                    }

                    is SearchResult.Error -> {
                        renderRegionSelectionState(RegionSelectionState.Error)
                    }

                    is SearchResult.NoInternet -> {
                        renderRegionSelectionState(RegionSelectionState.NoInternet)
                    }
                }
            }
    }

    private fun renderRegionSelectionState(state: RegionSelectionState) {
        _regionScreenState.update { state }
    }

    fun saveRegion(region: Region) {
        viewModelScope.launch {
            val country = getCountryByRegion(region)
            filterSearchInteractor.saveFilterParameters(
                _filterParametersState.value.copy(
                    idRegion = region.id,
                    nameRegion = region.name,
                    idCountry = country.id,
                    nameCountry = country.name
                )
            )
        }
    }

    private fun getCountryByRegion(region: Region): Country {
        if (_filterParametersState.value.idCountry.isNotBlank())
            return Country(
                id = _filterParametersState.value.idCountry,
                name = _filterParametersState.value.nameCountry
            )
        var currentRegion = region
        while (currentRegion.parentId != null && currentRegion.parentId != "1001") {
            try {
                currentRegion = allRegions.first { it.id == currentRegion.parentId }
            } catch (e: NoSuchElementException) {
                println(e)
                return Country(id = "", name = "")
            }
        }
        return Country(id = currentRegion.id, name = currentRegion.name)
    }

    fun onTextChange(textFilter: String) {
        if (textFilter.isBlank()) {
            renderRegionSelectionState(RegionSelectionState.Content(foundRegions))
        } else {
            renderRegionSelectionState(
                RegionSelectionState.Content(
                    foundRegions.filter { region ->
                        region.name.contains(textFilter, true)
                    }
                )
            )
        }
    }
}
