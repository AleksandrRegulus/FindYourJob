package ru.practicum.android.diploma.presentation.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.share.SharingInteractor
import ru.practicum.android.diploma.presentation.vacancy.state.VacancyFragmentScreenState
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

@HiltViewModel
class VacancyViewModel @Inject constructor(
    private val vacanciesInteractor: VacanciesInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private var isRequestMade = false
    private val vacancyFragmentScreenState = MutableLiveData<VacancyFragmentScreenState>()
    fun getVacancyFragmentScreenState(): LiveData<VacancyFragmentScreenState> = vacancyFragmentScreenState

    fun getVacancyDetails(vacancyId: String) {
        if (!isRequestMade && vacancyId.isNotEmpty()) {
            isRequestMade = true
            renderVacancyFragmentScreenState(VacancyFragmentScreenState.Loading)
            viewModelScope.launch(Dispatchers.IO) {
                vacanciesInteractor
                    .getVacancyDetails(vacancyId)
                    .collect { searchResult ->
                        when (searchResult) {
                            is SearchResult.Error -> {
                                renderVacancyFragmentScreenState(VacancyFragmentScreenState.ServerError)
                            }

                            is SearchResult.NoInternet -> {
                                renderVacancyFragmentScreenState(VacancyFragmentScreenState.NoInternetConnection)
                            }

                            is SearchResult.Success -> {
                                renderVacancyFragmentScreenState(VacancyFragmentScreenState.Content(searchResult.data))
                                setInitialVacancyFavoriteStatus(searchResult.data)
                            }
                        }
                    }
            }
        }
    }

    fun getVacancyDetailsFromLocalStorage(vacancyId: String) {
        if (vacancyId.isNotEmpty()) {
            renderVacancyFragmentScreenState(VacancyFragmentScreenState.Loading)
            viewModelScope.launch(Dispatchers.IO) {
                vacanciesInteractor
                    .getVacancyDetailsFromLocalStorage(vacancyId)
                    .collect {
                        renderVacancyFragmentScreenState(VacancyFragmentScreenState.Content(it))
                        setInitialVacancyFavoriteStatus(it)
                    }
            }
        }
    }

    private fun renderVacancyFragmentScreenState(state: VacancyFragmentScreenState) {
        vacancyFragmentScreenState.postValue(state)
    }

    fun getStringKeySkills(keySkills: List<String>): String {
        val sb = StringBuilder()

        keySkills.forEachIndexed { _, skill ->
            sb.append("• $skill\n")
        }
        return sb.toString()
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

    fun addVacancyToFavorites(vacancyDetails: VacancyDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            vacanciesInteractor.addVacancyToFavorites(vacancyDetails)
        }
        renderVacancyFragmentScreenState(
            VacancyFragmentScreenState.Content(vacancyDetails.copy(isFavorite = true))
        )
    }

    fun removeVacancyFromFavorites(vacancyDetails: VacancyDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            vacanciesInteractor.removeVacancyFromFavorites(vacancyDetails.id)
            renderVacancyFragmentScreenState(
                VacancyFragmentScreenState.Content(vacancyDetails.copy(isFavorite = false))
            )
        }

    }

    private fun setInitialVacancyFavoriteStatus(vacancyDetails: VacancyDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            vacanciesInteractor.getFavoriteVacancies().collect {
                val vacanciesId = it.map { it.id }
                if (vacanciesId.contains(vacancyDetails.id)) {
                    renderVacancyFragmentScreenState(
                        VacancyFragmentScreenState.Content(vacancyDetails.copy(isFavorite = true))
                    )
                }
            }
        }
    }

}
