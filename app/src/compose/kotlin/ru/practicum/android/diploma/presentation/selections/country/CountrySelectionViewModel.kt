package ru.practicum.android.diploma.presentation.selections.country

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
import ru.practicum.android.diploma.presentation.selections.country.state.CountrySelectionState
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

@HiltViewModel
class CountrySelectionViewModel @Inject constructor(
    private val filterSearchInteractor: FilterSearchInteractor,
) : ViewModel() {

    private val _filterParametersState = MutableStateFlow(FilterParameters.initial)

    private val _countryScreenState = MutableStateFlow<CountrySelectionState>(CountrySelectionState.Loading)
    val countryScreenState: StateFlow<CountrySelectionState> = _countryScreenState

    init {
        viewModelScope.launch {
            filterSearchInteractor.readFilterParameters().collect { filterParams ->
                _filterParametersState.value = filterParams
            }
        }

        viewModelScope.launch {
            filterSearchInteractor
                .getCountries()
                .catch {
                    emit(SearchResult.Error())
                }
                .collect { searchResult ->
                    when (searchResult) {
                        is SearchResult.Success -> {
                            if (searchResult.data.isEmpty()) {
                                renderCountrySelectionState(CountrySelectionState.Empty)
                            } else {
                                renderCountrySelectionState(CountrySelectionState.Content(searchResult.data))
                            }
                        }

                        is SearchResult.NoInternet -> {
                            renderCountrySelectionState(CountrySelectionState.NoInternet)
                        }

                        is SearchResult.Error -> {
                            renderCountrySelectionState(CountrySelectionState.Error)
                        }
                    }
                }
        }
    }

    private fun renderCountrySelectionState(state: CountrySelectionState) {
        _countryScreenState.update { state }
    }

    fun saveCountry(country: Country) {
        viewModelScope.launch {
            filterSearchInteractor.saveFilterParameters(
                _filterParametersState.value.copy(
                    idCountry = country.id,
                    nameCountry = country.name,
                    idRegion = "",
                    nameRegion = ""
                )
            )
        }
    }
}
