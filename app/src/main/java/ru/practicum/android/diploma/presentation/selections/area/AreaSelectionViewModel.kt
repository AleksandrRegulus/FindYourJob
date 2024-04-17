package ru.practicum.android.diploma.presentation.selections.area

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import javax.inject.Inject

class AreaSelectionViewModel(private val filterSearchInteractor: FilterSearchInteractor) : ViewModel() {

    private var currentFilterParameters: FilterParameters? = null
    private val _selectionAreaState = MutableLiveData<FilterParameters?>()
    val selectionAreaState: LiveData<FilterParameters?> = _selectionAreaState

    init {
        getFilterParameters()
    }

    private fun getFilterParameters() {
        currentFilterParameters = filterSearchInteractor.getFilterParameters()
        _selectionAreaState.postValue(currentFilterParameters)
    }

    fun saveCountrySelection(countryId: String, countryName: String) {
        if (countryId.isEmpty()) {
            saveRegionSelection("", "")
        }
        currentFilterParameters =
            currentFilterParameters?.copy(
                idCountry = countryId,
                nameCountry = countryName,
                idRegion = "",
                nameRegion = ""
            )
                ?: FilterParameters(idCountry = countryId, nameCountry = countryName)
        _selectionAreaState.postValue(currentFilterParameters)
    }

    fun saveRegionSelection(regionId: String, regionName: String) {
        currentFilterParameters =
            currentFilterParameters?.copy(idRegion = regionId, nameRegion = regionName)
                ?: FilterParameters(idRegion = regionId, nameRegion = regionName)
        _selectionAreaState.postValue(currentFilterParameters)
    }

    fun saveCountryAndRegionToSharedPref() {
        currentFilterParameters?.let { filterSearchInteractor.saveFilterParameters(it) }
    }

    @Suppress("UNCHECKED_CAST")
    class AreaSelectionViewModelFactory @Inject constructor(
        private val filterSearchInteractor: FilterSearchInteractor,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AreaSelectionViewModel(filterSearchInteractor) as T
        }
    }
}
