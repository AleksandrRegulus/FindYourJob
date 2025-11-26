package ru.practicum.android.diploma.data

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.practicum.android.diploma.data.converters.Converter
import ru.practicum.android.diploma.data.converters.PagingConverters
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.entity.RemoteKeys
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.NO_INTERNET_CONNECTION
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class VacanciesRemoteMediator(
    private val appDatabase: AppDatabase,
    private val networkClient: NetworkClient,
    private val vacanciesRequest: VacanciesRequest
) : RemoteMediator<Int, VacancyEntity>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, VacancyEntity>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 0
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {

            val requestVacanciesListSearch = Converter.fromVacanciesRequestToRequestVacanciesListSearch(
                vacanciesRequest,
                page = page,
                perPage = state.config.pageSize
            )

            val apiResponse = networkClient.doRequestSearchVacancies(requestVacanciesListSearch)

            if (apiResponse.resultCode == NO_INTERNET_CONNECTION) {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.withTransaction {
                        appDatabase.getRemoteKeysDao().clearRemoteKeys()
                        appDatabase.getVacanciesDao().clearAllVacancies()
                    }
                    return MediatorResult.Error(Throwable(message = "NO_INTERNET_CONNECTION"))
                }
            }

            val vacancies = PagingConverters.fromResponseVacanciesListDtoToListVacancyEntity(
                (apiResponse as ResponseVacanciesListDto),
                page
            )
            val endOfPaginationReached = vacancies.isEmpty()

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.getRemoteKeysDao().clearRemoteKeys()
                    appDatabase.getVacanciesDao().clearAllVacancies()
                }
                val prevKey = if (page > 0) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = vacancies.map {
                    RemoteKeys(vacancyID = it.id, prevKey = prevKey, currentPage = page, nextKey = nextKey)
                }

                appDatabase.getRemoteKeysDao().insertAll(remoteKeys)
                appDatabase.getVacanciesDao().insertAll(vacancies)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            return MediatorResult.Error(error)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, VacancyEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                appDatabase.getRemoteKeysDao().getRemoteKeyByVacancyID(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, VacancyEntity>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { vacancy ->
            return appDatabase.getRemoteKeysDao().getRemoteKeyByVacancyID(vacancy.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, VacancyEntity>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { vacancy ->
            appDatabase.getRemoteKeysDao().getRemoteKeyByVacancyID(vacancy.id)
        }
    }
}
