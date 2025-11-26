package ru.practicum.android.diploma.presentation.selections.area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import javax.inject.Inject

@HiltViewModel
class AreaSelectionViewModel @Inject constructor(
    private val filterSearchInteractor: FilterSearchInteractor
) : ViewModel() {

    private val _filterParametersState = MutableStateFlow(FilterParameters.initial)
    val filterParametersState: StateFlow<FilterParameters> = _filterParametersState

    init {
        viewModelScope.launch {
            filterSearchInteractor.readFilterParameters().collect { filterParams ->
                _filterParametersState.value = filterParams
            }
        }
    }

    fun onCountryClearClick() {
        viewModelScope.launch {
            filterSearchInteractor.saveFilterParameters(
                _filterParametersState.value.copy(
                    idCountry = "",
                    nameCountry = "",
                )
            )
        }
    }

    fun onRegionClearClick() {
        viewModelScope.launch {
            filterSearchInteractor.saveFilterParameters(
                _filterParametersState.value.copy(
                    idRegion = "",
                    nameRegion = "",
                )
            )
        }
    }
}
