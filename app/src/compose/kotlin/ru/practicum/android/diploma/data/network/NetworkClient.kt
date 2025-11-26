package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.data.dto.RequestVacanciesListSearch
import ru.practicum.android.diploma.data.dto.RequestVacancySearch
import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {

    val quantityFoundVacancies: StateFlow<Int?>

    suspend fun doRequestSearchVacancies(requestDto: RequestVacanciesListSearch): Response

    suspend fun doRequestGetVacancy(dto: RequestVacancySearch): Response

    suspend fun doRequestGetSimilarVacancies(vacancyId: String, requestDto: RequestVacanciesListSearch): Response

    suspend fun doRequestGetIndustries(): Response

    suspend fun doRequestGetCountries(): Response

    suspend fun doRequestGetAreas(): Response

    suspend fun doRequestGetAreas(countryId: String): Response
}
