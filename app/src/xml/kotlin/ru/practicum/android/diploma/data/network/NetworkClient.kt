package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.RequestAreasSearch
import ru.practicum.android.diploma.data.dto.RequestVacanciesListSearch
import ru.practicum.android.diploma.data.dto.RequestVacancySearch
import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {

    suspend fun doRequestSearchVacancies(requestDto: RequestVacanciesListSearch): Response

    suspend fun doRequestGetVacancy(dto: RequestVacancySearch): Response

    suspend fun doRequestGetSimilarVacancies(vacancyId: String, requestDto: RequestVacanciesListSearch): Response

    suspend fun doRequestGetIndustries(): Response

    suspend fun doRequestGetCountries(): Response

    suspend fun doRequestGetAreas(): Response

    suspend fun doRequestGetAreas(dto: RequestAreasSearch): Response
}
