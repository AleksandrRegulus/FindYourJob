package ru.practicum.android.diploma.presentation.selections.industry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.selections.industry.state.IndustrySelectionState
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

@HiltViewModel
class IndustrySelectionViewModel @Inject constructor(
    private val filterSearchInteractor: FilterSearchInteractor,
) : ViewModel() {

    private val _filterParametersState = MutableStateFlow(FilterParameters.initial)

    private val _industryScreenState = MutableStateFlow<IndustrySelectionState>(IndustrySelectionState.Loading)
    val industryScreenState: StateFlow<IndustrySelectionState> = _industryScreenState

    private val industries = mutableListOf<Industry>()
    private var filteredIndustries = emptyList<Industry>()
    private var selectedIndustry: Industry = Industry("", "")

    init {
        viewModelScope.launch {
            filterSearchInteractor.readFilterParameters().collect { filterParams ->
                _filterParametersState.value = filterParams
                selectedIndustry = Industry(
                    id = filterParams.idIndustry,
                    name = filterParams.nameIndustry
                )
            }
        }

        viewModelScope.launch {
            filterSearchInteractor
                .getIndustries()
                .catch {
                    emit(SearchResult.Error())
                }
                .collect { result ->
                    when (result) {
                        is SearchResult.Success -> {
                            if (result.data.isEmpty()) {
                                renderIndustrySelectionState(IndustrySelectionState.Empty)
                            } else {
                                filteredIndustries = result.data
                                renderContent()
                                industries.addAll(result.data)
                            }
                        }

                        is SearchResult.Error -> {
                            renderIndustrySelectionState(IndustrySelectionState.Error)
                        }

                        is SearchResult.NoInternet -> {
                            renderIndustrySelectionState(IndustrySelectionState.NoInternet)
                        }
                    }
                }
        }
    }

    private fun renderIndustrySelectionState(state: IndustrySelectionState) {
        _industryScreenState.update { state }
    }

    private fun renderContent() {
        renderIndustrySelectionState(
            IndustrySelectionState.Content(
                industries = filteredIndustries,
                idSelectedIndustry = selectedIndustry.id
            )
        )
    }


    fun selectIndustry(industry: Industry) {
        selectedIndustry = industry
        renderContent()
    }

    fun saveSelectedIndustry() {
        viewModelScope.launch {
            filterSearchInteractor.saveFilterParameters(
                _filterParametersState.value.copy(
                    idIndustry = selectedIndustry.id,
                    nameIndustry = selectedIndustry.name
                )
            )
        }
    }

    fun onTextChange(textFilter: String) {
        filteredIndustries = if (textFilter.isBlank()) {
            industries
        } else {
            filteredIndustries.filter { industry ->
                industry.name.contains(textFilter, ignoreCase = true)
            }
        }
        renderContent()
    }
}
