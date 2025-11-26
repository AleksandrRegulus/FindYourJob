package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.state.SearchState
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val vacanciesInteractor: VacanciesInteractor,
    private val filterSearchInteractor: FilterSearchInteractor
) : ViewModel() {

    private var searchJob: Job? = null
    private fun searchDebounced(request: VacanciesRequest) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            _request.update { request }
        }
    }

    private val _filterParameters = MutableStateFlow(FilterParameters.initial)

    private var _request = MutableStateFlow<VacanciesRequest?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val vacanciesFlow: Flow<PagingData<Vacancy>> = _request
        .mapNotNull { it }
        .flatMapLatest { vacanciesInteractor.searchVacancies(it) }
        .cachedIn(viewModelScope)

    private val _state = MutableStateFlow(SearchState())
    val state = combine(
        _request,
        _state,
        vacanciesInteractor.quantityFoundVacancies,
        _filterParameters
    ) { request, state, quantity, filterParams ->
        state.copy(
            vacanciesFlow = if (request == null) null else vacanciesFlow,
            searchText = state.searchText,
            quantityFoundVacancies = quantity,
            isFilters = filterParams != FilterParameters.initial
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        SearchState()
    )

    init {
        viewModelScope.launch {
            filterSearchInteractor.readFilterParameters().collect { filterParams ->
                _filterParameters.value = filterParams
            }
        }
    }

    fun textFieldChange(newText: String) {
        _state.update { it.copy(searchText = newText) }
        if (newText.isNotEmpty()) {
            searchByText(newText)
        } else {
            searchJob?.cancel()
            _request.update { null }
        }
    }

    private fun searchByText(text: String, withDelay: Boolean = true) {
        val area = with(this._filterParameters.value) {
            idRegion.ifEmpty {
                idCountry.ifEmpty {
                    null
                }
            }
        }
        val industry = this._filterParameters.value.idIndustry.ifEmpty { null }
        val salary = this._filterParameters.value.salary
        val onlyWithSalary = this._filterParameters.value.doNotShowWithoutSalary

        val newRequest = _request.value?.copy(
            text = text,
            area = area,
            industry = industry,
            salary = if (salary.isEmpty()) null else salary.toInt(),
            onlyWithSalary = onlyWithSalary
        ) ?: VacanciesRequest(
            text = text,
            area = area,
            industry = industry,
            salary = if (salary.isEmpty()) null else salary.toInt(),
            onlyWithSalary = onlyWithSalary
        )

        if (withDelay) {
            searchDebounced(newRequest)
        } else {
            _request.update { newRequest }
        }
    }

    fun searchNow() {
        val text = _state.value.searchText
        if (text.isNotEmpty()) searchByText(text = text, false)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
