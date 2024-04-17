package ru.practicum.android.diploma.presentation.selections.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.presentation.selections.country.state.CountrySelectionState
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

class CountrySelectionViewModel(private val filterSearchInteractor: FilterSearchInteractor) : ViewModel() {

    private val countrySelectionState = MutableLiveData<CountrySelectionState>()

    fun getCountrySelectionState(): LiveData<CountrySelectionState> = countrySelectionState

    fun getCountries() {
        renderCountrySelectionState(CountrySelectionState.Loading)
        viewModelScope.launch {
            filterSearchInteractor
                .getCountries()
                .catch {
                    emit(SearchResult.Error())
                }
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

    @Suppress("UNCHECKED_CAST")
    class CountrySelectionViewModelFactory @Inject constructor(
        private val filterSearchInteractor: FilterSearchInteractor,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CountrySelectionViewModel(filterSearchInteractor) as T
        }
    }
}
