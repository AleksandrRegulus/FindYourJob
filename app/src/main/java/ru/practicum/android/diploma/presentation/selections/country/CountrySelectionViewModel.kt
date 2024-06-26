package ru.practicum.android.diploma.presentation.selections.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.presentation.selections.country.state.CountrySelectionState
import ru.practicum.android.diploma.util.SearchResult

class CountrySelectionViewModel(private val filterSearchInteractor: FilterSearchInteractor) : ViewModel() {

    private val countrySelectionState = MutableLiveData<CountrySelectionState>()

    fun getCountrySelectionState(): LiveData<CountrySelectionState> = countrySelectionState

    fun getCountries() {
        renderCountrySelectionState(CountrySelectionState.Loading)
        viewModelScope.launch {
            filterSearchInteractor
                .getCountries()
                .collect {
                    when (it) {
                        is SearchResult.Success -> {
                            if (it.data.isEmpty()) {
                                renderCountrySelectionState(CountrySelectionState.Empty)
                            } else {
                                renderCountrySelectionState(CountrySelectionState.Content(it.data))
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
        countrySelectionState.postValue(state)
    }
}
