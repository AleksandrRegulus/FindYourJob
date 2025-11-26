package ru.practicum.android.diploma.presentation.vacancy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.share.SharingInteractor
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

@HiltViewModel
class VacancyViewModel @Inject constructor(
    private val vacanciesInteractor: VacanciesInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private var isRequestMade = false
    private val _vacancyScreenState = MutableStateFlow<VacancyScreenState>(VacancyScreenState.Loading)
    val vacancyScreenState: StateFlow<VacancyScreenState> = _vacancyScreenState

    fun getVacancyDetails(vacancyId: String) {
        if (!isRequestMade && vacancyId.isNotEmpty()) {
            isRequestMade = true
            renderVacancyScreenState(VacancyScreenState.Loading)
            viewModelScope.launch(Dispatchers.IO) {
                vacanciesInteractor
                    .getVacancyDetails(vacancyId)
                    .collect { searchResult ->
                        when (searchResult) {
                            is SearchResult.Error -> {
                                renderVacancyScreenState(VacancyScreenState.ServerError)
                            }

                            is SearchResult.NoInternet -> {
                                renderVacancyScreenState(VacancyScreenState.NoInternetConnection)
                            }

                            is SearchResult.Success -> {
                                renderVacancyScreenState(VacancyScreenState.Content(searchResult.data))
                            }
                        }
                    }
            }
        }
    }

    private fun renderVacancyScreenState(state: VacancyScreenState) {
        _vacancyScreenState.update { state }
    }

    fun shareVacancy(vacancyUrl: String?) {
        if (vacancyUrl != null) {
            sharingInteractor.shareVacancy(vacancyUrl)
        }
    }

    fun openEmail(email: String?) {
        if (email != null) {
            sharingInteractor.openEmail(email)
        }
    }

    fun openPhone(phoneNumber: String?) {
        if (phoneNumber != null) {
            sharingInteractor.openPhone(phoneNumber)
        }
    }

    fun favoriteButtonClick() {
        val vacancyDetails = (vacancyScreenState.value as VacancyScreenState.Content).vacancy
        if (vacancyDetails.isFavorite) removeVacancyFromFavorites(vacancyDetails)
        else addVacancyToFavorites(vacancyDetails)
    }

    private fun addVacancyToFavorites(vacancyDetails: VacancyDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            vacanciesInteractor.addVacancyToFavorites(vacancyDetails)
        }
        renderVacancyScreenState(
            VacancyScreenState.Content(vacancyDetails.copy(isFavorite = true))
        )
    }

    private fun removeVacancyFromFavorites(vacancyDetails: VacancyDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            vacanciesInteractor.removeVacancyFromFavorites(vacancyDetails.id)
            renderVacancyScreenState(
                VacancyScreenState.Content(vacancyDetails.copy(isFavorite = false))
            )
        }

    }

}
