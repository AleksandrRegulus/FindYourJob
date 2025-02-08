package ru.practicum.android.diploma.presentation.selections.industry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.selections.industry.state.IndustrySelectionState
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

@HiltViewModel
class IndustrySelectionViewModel @Inject constructor(
    private val filterSearchInteractor: FilterSearchInteractor
) : ViewModel() {

    private val industriesFragmentState = MutableLiveData<IndustrySelectionState>()
    private var currentFilterParameters: FilterParameters? = null
    private var checkedIndustry: Industry? = null
    private var selectedPosition = -1
    private val industries = mutableListOf<Industry>()
    private val filteredIndustries = mutableListOf<Industry>()

    init {
        getFilterParameters()
        getIndustries()
    }

    fun getIndustriesFragmentState(): LiveData<IndustrySelectionState> = industriesFragmentState

    private fun getFilterParameters() {
        currentFilterParameters = filterSearchInteractor.getFilterParameters()
    }

    fun saveCheckedIndustry(industry: Industry, position: Int) {
        checkedIndustry = industry
        selectedPosition = position
        renderContent()
    }

    fun saveIndustry() {
        checkedIndustry?.let {
            filterSearchInteractor.saveFilterParameters(
                currentFilterParameters?.copy(idIndustry = it.id, nameIndustry = it.name)
                    ?: FilterParameters(idIndustry = it.id, nameIndustry = it.name)
            )
        }
    }

    private fun getIndustries() {
        industriesFragmentState.postValue(IndustrySelectionState.Loading)
        viewModelScope.launch {
            filterSearchInteractor
                .getIndustries()
                .catch {
                    emit(SearchResult.Error())
                }
                .collect { result ->
                    when (result) {
                        is SearchResult.NoInternet -> {
                            industriesFragmentState.postValue(IndustrySelectionState.NoInternet)
                        }

                        is SearchResult.Error -> {
                            industriesFragmentState.postValue(IndustrySelectionState.Error)
                        }

                        is SearchResult.Success -> {
                            if (result.data.isEmpty()) {
                                industriesFragmentState.postValue(IndustrySelectionState.Empty)
                            } else {
                                industries.addAll(result.data)
                                filteredIndustries.addAll(result.data)
                                selectedPosition = filteredIndustries.indexOfFirst { industry ->
                                    val idIndustry = currentFilterParameters?.idIndustry ?: ""
                                    industry.id == idIndustry
                                }
                                renderContent()
                            }
                        }
                    }
                }
        }
    }

    private fun renderContent() {
        industriesFragmentState.postValue(IndustrySelectionState.Content(filteredIndustries, selectedPosition))
    }

    private fun saveFilteredIndustries(newFilteredList: List<Industry>) {
        filteredIndustries.clear()
        filteredIndustries.addAll(newFilteredList)
    }

    fun filter(searchQuery: String?) {
        selectedPosition = -1
        if (searchQuery.isNullOrEmpty()) {
            saveFilteredIndustries(industries)
            renderContent()
        } else {
            saveFilteredIndustries(
                industries.filter {
                    it.name.contains(searchQuery, true)
                }
            )
            if (filteredIndustries.isNotEmpty()) {
                renderContent()
            } else {
                industriesFragmentState.postValue(IndustrySelectionState.Empty)
            }
        }
    }

}
