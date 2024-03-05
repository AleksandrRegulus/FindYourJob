package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.util.SearchResult

interface FilterSearchRepository {

    fun getIndustries(): Flow<SearchResult<List<Industry>>>

    fun getCountries(): Flow<SearchResult<List<Country>>>

    fun getAreas(): Flow<SearchResult<List<Region>>>

    fun getParentAreas(parentAreaId: String?): Flow<SearchResult<List<Region>>>
}
