package ru.practicum.android.diploma.domain.api

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.SearchResult

interface VacanciesRepository {

    val quantityFoundVacancies: StateFlow<Int?>

    fun searchVacanciesPager(vacanciesRequest: VacanciesRequest): Flow<PagingData<Vacancy>>

    fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<SearchResult<Vacancies>>

    fun getVacancyDetails(vacancyId: String): Flow<SearchResult<VacancyDetails>>

    fun getSimilarVacancies(
        vacancyId: String,
        vacanciesRequest: VacanciesRequest
    ): Flow<SearchResult<Vacancies>>

    suspend fun addVacancyToFavorites(vacancy: VacancyDetails)

    suspend fun removeVacancyFromFavorites(vacancyId: String)

    fun getFavoriteVacancies(): Flow<List<Vacancy>>
}
