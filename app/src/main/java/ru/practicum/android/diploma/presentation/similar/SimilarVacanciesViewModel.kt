package ru.practicum.android.diploma.presentation.similar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.similar.state.SimilarVacanciesFragmentState
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

@HiltViewModel
class SimilarVacanciesViewModel @Inject constructor(
    private val vacanciesInteractor: VacanciesInteractor,
) : ViewModel() {

    private var similarVacancyId = ""
    private var isRequestMade = false
    private val vacanciesList = mutableListOf<Vacancy>()
    private var currentRequest: VacanciesRequest? = null
    private var currentPage = 0
    private var maxPages = 0
    private var isNextPageLoading = false

    private val similarVacanciesFragmentState = MutableLiveData<SimilarVacanciesFragmentState>()
    fun getSimilarVacanciesFragmentState(): LiveData<SimilarVacanciesFragmentState> = similarVacanciesFragmentState

    fun getSimilarVacancies(vacancyId: String) {
        if (!isRequestMade && vacancyId.isNotEmpty()) {
            isRequestMade = true
            similarVacancyId = vacancyId
            currentRequest = VacanciesRequest()
            makeRequest(currentRequest!!)
        }
    }

    fun getNextPage() {
        if (currentRequest != null && !isNextPageLoading) {
            val nextPage = currentPage + 1
            if (nextPage < maxPages) {
                currentRequest = currentRequest?.copy(page = nextPage)
                isNextPageLoading = true
                currentRequest?.let { makeRequest(it) }
            }
        }
    }

    private fun makeRequest(vacanciesRequest: VacanciesRequest) {
        val isFirstPage = vacanciesRequest.page == 0
        renderState(SimilarVacanciesFragmentState.Loading(isFirstPage))
        viewModelScope.launch {
            vacanciesInteractor
                .getSimilarVacancies(similarVacancyId, vacanciesRequest)
                .catch {
                    emit(SearchResult.Error())
                }
                .collect { result ->
                    if (isActive) {
                        parsingResultSearch(result, isFirstPage)
                        isNextPageLoading = false
                    }
                }
        }
    }

    private fun parsingResultSearch(result: SearchResult<Vacancies>, isFirstPage: Boolean) {
        when (result) {
            is SearchResult.NoInternet -> {
                renderState(SimilarVacanciesFragmentState.NoInternet(isFirstPage))
            }

            is SearchResult.Error -> {
                renderState(SimilarVacanciesFragmentState.Error(isFirstPage))
            }

            is SearchResult.Success -> {
                maxPages = result.data.pages
                currentPage = result.data.page
                if (result.data.items.isEmpty()) {
                    renderState(SimilarVacanciesFragmentState.Empty)
                } else {
                    vacanciesList.addAll(
                        filterDuplicateVacancy(result.data.items)
                    )
                    renderState(SimilarVacanciesFragmentState.Content(vacanciesList))
                }
            }
        }
    }

    private fun filterDuplicateVacancy(responseListVacancy: List<Vacancy>): List<Vacancy> {
        return responseListVacancy.filterNot { vacancy ->
            vacanciesList.any {
                vacancy.id == it.id
            }
        }
    }

    fun getContent() {
        renderState(SimilarVacanciesFragmentState.Content(vacanciesList))
    }

    private fun renderState(state: SimilarVacanciesFragmentState) {
        similarVacanciesFragmentState.postValue(state)
    }

}
