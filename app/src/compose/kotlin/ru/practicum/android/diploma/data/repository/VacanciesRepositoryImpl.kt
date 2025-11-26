package ru.practicum.android.diploma.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.VacanciesRemoteMediator
import ru.practicum.android.diploma.data.converters.Converter
import ru.practicum.android.diploma.data.converters.PagingConverters
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.dto.RequestVacancySearch
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

class VacanciesRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,
) : VacanciesRepository {

    override val quantityFoundVacancies: StateFlow<Int?> = networkClient.quantityFoundVacancies

    @OptIn(ExperimentalPagingApi::class)
    override fun searchVacanciesPager(vacanciesRequest: VacanciesRequest): Flow<PagingData<Vacancy>> {

        val vacanciesRemoteMediator = VacanciesRemoteMediator(
            appDatabase,
            networkClient,
            vacanciesRequest
        )

        return Pager(
            config = PagingConfig(
                pageSize = VACANCIES_PER_PAGE,
            ),
            pagingSourceFactory = {
                appDatabase.getVacanciesDao().getVacancies()
            },
            remoteMediator = vacanciesRemoteMediator
        ).flow
            .map { pagingData ->
                pagingData.map { vacancyEntity ->
                    PagingConverters.fromVacancyEntityToVacancy(vacancyEntity)
                }
            }
    }

    override fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<SearchResult<Vacancies>> = flow {
        val requestVacanciesListSearch =
            Converter.fromVacanciesRequestToRequestVacanciesListSearch(vacanciesRequest, 0, 5)
        val response = networkClient.doRequestSearchVacancies(requestVacanciesListSearch)
        when (response.resultCode) {
            SUCCESS_RESPONSE -> {
                emit(
                    SearchResult.Success(
                        Converter.fromResponseVacanciesListDtoToVacancies(response as ResponseVacanciesListDto)
                    )
                )
            }

            NO_INTERNET_CONNECTION -> {
                emit(SearchResult.NoInternet())
            }

            else -> {
                emit(SearchResult.Error())
            }
        }
    }

    override fun getVacancyDetails(vacancyId: String): Flow<SearchResult<VacancyDetails>> = flow {
        val vacancyFromFavorite = appDatabase.favoriteVacancyDao().getVacancyById(vacancyId)
        val isFavorite = vacancyFromFavorite != null

        val requestVacancySearch = RequestVacancySearch(id = vacancyId)
        val response = networkClient.doRequestGetVacancy(requestVacancySearch)

        when (response.resultCode) {
            SUCCESS_RESPONSE -> {
                val vacancy = Converter.fromVacancyDtoToVacancyDetails(response as VacancyDto, isFavorite)
                if (isFavorite) addVacancyToFavorites(vacancy)

                emit(SearchResult.Success(vacancy))
            }

            NO_INTERNET_CONNECTION -> {
                if (vacancyFromFavorite != null) emit(
                    SearchResult.Success(
                        Converter.fromFavoriteVacancyEntityToVacancyDetails(
                            vacancyFromFavorite
                        )
                    )
                ) else emit(SearchResult.NoInternet())
            }

            else -> {
                if (vacancyFromFavorite != null) emit(
                    SearchResult.Success(
                        Converter.fromFavoriteVacancyEntityToVacancyDetails(
                            vacancyFromFavorite
                        )
                    )
                ) else emit(SearchResult.Error())
            }
        }
    }

    override fun getSimilarVacancies(
        vacancyId: String,
        vacanciesRequest: VacanciesRequest
    ): Flow<SearchResult<Vacancies>> = flow {
        val requestVacanciesListSearch =
            Converter.fromVacanciesRequestToRequestVacanciesListSearch(vacanciesRequest, 0, 5)
        val response = networkClient.doRequestGetSimilarVacancies(vacancyId, requestVacanciesListSearch)
        when (response.resultCode) {
            SUCCESS_RESPONSE -> {
                emit(
                    SearchResult.Success(
                        Converter.fromResponseVacanciesListDtoToVacancies(response as ResponseVacanciesListDto)
                    )
                )
            }

            NO_INTERNET_CONNECTION -> {
                emit(SearchResult.NoInternet())
            }

            else -> {
                emit(SearchResult.Error())
            }
        }
    }

    override suspend fun addVacancyToFavorites(vacancy: VacancyDetails) {
        appDatabase.favoriteVacancyDao().insertVacancy(
            Converter.fromVacancyDetailsToFavoriteVacancyEntity(vacancy)
        )
    }

    override suspend fun removeVacancyFromFavorites(vacancyId: String) {
        appDatabase.favoriteVacancyDao().deleteVacancy(vacancyId)
    }

    override fun getFavoriteVacancies(): Flow<List<Vacancy>> {
        return appDatabase.favoriteVacancyDao().getAllVacancies().map {
            it.map { Converter.fromFavoriteVacancyEntityToVacancy(it) }
        }
    }

    companion object {
        private const val VACANCIES_PER_PAGE = 20
        private const val SUCCESS_RESPONSE = 200
        private const val NO_INTERNET_CONNECTION = -1
    }
}
