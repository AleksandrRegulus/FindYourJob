package ru.practicum.android.diploma.presentation.search.state

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

data class SearchState(
    val vacanciesFlow: Flow<PagingData<Vacancy>>? = null,
    val quantityFoundVacancies: Int? = null,
    val searchText: String = "",
    val isFilters: Boolean = false
)
