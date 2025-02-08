package ru.practicum.android.diploma.presentation.selections.region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.selections.region.state.RegionSelectionState
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

@HiltViewModel
class RegionSelectionViewModel @Inject constructor(
    private val filterSearchInteractor: FilterSearchInteractor
) : ViewModel() {

    private val foundRegions = mutableListOf<Region>()
    private val allRegions = mutableListOf<Region>()
    private val _regionsStateLiveData = MutableLiveData<RegionSelectionState>()
    val regionsStateLiveData: LiveData<RegionSelectionState> get() = _regionsStateLiveData

    fun getRegions() {
        viewModelScope.launch(Dispatchers.IO) {
            _regionsStateLiveData.postValue(RegionSelectionState.Loading)
            filterSearchInteractor.getRegions()
                .catch {
                    _regionsStateLiveData.postValue(RegionSelectionState.Error)
                }
                .collect {
                    handleSearchResult(it)
                }
        }
    }

    fun getRegionsOfCountry(countryId: String) {
        _regionsStateLiveData.postValue(RegionSelectionState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            filterSearchInteractor.getRegionsOfCountry(countryId)
                .catch {
                    _regionsStateLiveData.postValue(RegionSelectionState.Error)
                }
                .collect {
                    handleSearchResult(it)
                }
        }
    }

    private fun handleSearchResult(searchResult: SearchResult<List<Region>>) {
        when (searchResult) {
            is SearchResult.Success -> {
                if (searchResult.data.isEmpty()) {
                    _regionsStateLiveData.postValue(RegionSelectionState.Empty)
                } else {
                    foundRegions.addAll(searchResult.data.filterNot { it.parentId.isNullOrEmpty() })
                    _regionsStateLiveData.postValue(
                        RegionSelectionState.Content(foundRegions)
                    )
                    allRegions.addAll(searchResult.data)
                }
            }

            is SearchResult.Error -> {
                _regionsStateLiveData.postValue(RegionSelectionState.Error)
            }

            is SearchResult.NoInternet -> {
                _regionsStateLiveData.postValue(RegionSelectionState.NoInternet)
            }
        }
    }

    fun filterRegions(searchQuery: String?) {
        if (searchQuery.isNullOrEmpty()) {
            _regionsStateLiveData.postValue(RegionSelectionState.Content(foundRegions))
        } else {
            val filteredList = foundRegions.filter {
                it.name.contains(searchQuery, true)
            }
            if (filteredList.isNotEmpty()) {
                _regionsStateLiveData.postValue(RegionSelectionState.Content(filteredList))
            } else {
                _regionsStateLiveData.postValue(RegionSelectionState.Empty)
            }
        }
    }

    fun getCountryByRegion(region: Region) {
        if (region.parentId != null && region.parentId != "1001") {
            try {
                getCountryByRegion(
                    allRegions.first { it.id == region.parentId }
                )
            } catch (e: NoSuchElementException) {
                println(e)
                _regionsStateLiveData.postValue(RegionSelectionState.RegionSelected(Region("", "", null)))
            }
        } else {
            _regionsStateLiveData.postValue(RegionSelectionState.RegionSelected(region))
        }
    }

}
