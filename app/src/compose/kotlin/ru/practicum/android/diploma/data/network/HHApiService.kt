package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.AreaDTO
import ru.practicum.android.diploma.data.dto.CountryDto
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.data.dto.VacancyDto

interface HHApiService {

    @GET("/vacancies")
    suspend fun searchVacancies(@QueryMap options: Map<String, String>): ResponseVacanciesListDto

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): VacancyDto

    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun getSimilarVacancies(
        @Path("vacancy_id") id: String,
        @QueryMap options: Map<String, String>
    ): ResponseVacanciesListDto

    @GET("/industries")
    suspend fun getIndustries(): List<IndustryDto>

    @GET("/areas/countries")
    suspend fun getCountries(): List<CountryDto>

    @GET("/areas")
    suspend fun getAreas(): List<AreaDTO>

    @GET("/areas/{area_id}")
    suspend fun getAreas(@Path("area_id") id: String): AreaDTO
}
