package ru.practicum.android.diploma.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.presentation.filter.state.InitialState
import javax.inject.Inject

@HiltViewModel
class FilterSettingsViewModel @Inject constructor(
    private val filterSearchInteractor: FilterSearchInteractor
) : ViewModel() {

    private val _filterParameters = MutableLiveData<FilterParameters?>()
    val filterParameters: LiveData<FilterParameters?> = _filterParameters

    private val _initialState = MutableLiveData(InitialState())
    val initialState: LiveData<InitialState> = _initialState

    fun getFilterParameters() {
        _filterParameters.value = filterSearchInteractor.getFilterParameters()
    }

    fun deleteFilterParameters() {
        filterSearchInteractor.deleteFilterParameters()
        _filterParameters.value = null
    }

    fun deleteAreaInFilterParameters() {
        val currentFilterParameters = filterSearchInteractor.getFilterParameters()
        currentFilterParameters?.let {
            filterSearchInteractor.saveFilterParameters(
                filterParameters = it.copy(
                    idCountry = "",
                    nameCountry = "",
                    idRegion = "",
                    nameRegion = ""
                )
            )
        }
        _filterParameters.value = filterSearchInteractor.getFilterParameters()
    }

    fun deleteIndustryFilterParameters() {
        val currentFilterParameters = filterSearchInteractor.getFilterParameters()
        currentFilterParameters?.let {
            filterSearchInteractor.saveFilterParameters(
                filterParameters = it.copy(
                    idIndustry = "",
                    nameIndustry = ""
                )
            )
        }
        _filterParameters.value = filterSearchInteractor.getFilterParameters()
    }

    fun saveOthers(salary: String, flag: Boolean) {
        val currentFilterParameters = filterSearchInteractor.getFilterParameters()
        if (currentFilterParameters != null) {
            filterSearchInteractor.saveFilterParameters(
                filterParameters = currentFilterParameters.copy(
                    salary = salary,
                    doNotShowWithoutSalary = flag
                )
            )
        } else {
            filterSearchInteractor.saveFilterParameters(
                FilterParameters(
                    salary = salary,
                    doNotShowWithoutSalary = flag
                )
            )
        }
    }

    fun isIndustryChosen(value: Boolean) {
        _initialState.value = initialState.value?.copy(isIndustryChosen = value)
    }

    fun isAreaChosen(value: Boolean) {
        _initialState.value = initialState.value?.copy(isAreaChosen = value)
    }

    fun isSalaryChosen(value: Boolean) {
        _initialState.value = initialState.value?.copy(isSalaryChosen = value)
    }

    fun doNotShowWithoutSalary(value: Boolean) {
        _initialState.value = initialState.value?.copy(doNotShowWithoutSalary = value)
    }

}
