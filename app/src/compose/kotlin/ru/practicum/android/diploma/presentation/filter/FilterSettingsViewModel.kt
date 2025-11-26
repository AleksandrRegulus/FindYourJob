package ru.practicum.android.diploma.presentation.filter

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
class FilterSettingsViewModel @Inject constructor(
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

    private fun saveFilterParams(filterParams: FilterParameters) {
        viewModelScope.launch {
            filterSearchInteractor.saveFilterParameters(filterParams)
        }
    }

    fun onCheckedChange(newCheckedValue: Boolean) {
        saveFilterParams(
            _filterParametersState.value.copy(doNotShowWithoutSalary = newCheckedValue)
        )
    }

    fun onTextSalaryChange(text: String) {
        saveFilterParams(_filterParametersState.value.copy(salary = text))
    }

    fun onClearClick() {
        onTextSalaryChange("")
    }

    fun onResetClick() {
        saveFilterParams(FilterParameters.initial)
    }

    fun onAreaClearClick() {
        saveFilterParams(
            _filterParametersState.value.copy(
                idCountry = "",
                nameCountry = "",
                idRegion = "",
                nameRegion = ""
            )
        )
    }

    fun onIndustryClearClick() {
        saveFilterParams(
            _filterParametersState.value.copy(
                idIndustry = "",
                nameIndustry = "",
            )
        )
    }

}
