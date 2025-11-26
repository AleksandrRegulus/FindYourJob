package ru.practicum.android.diploma.domain.impl

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

class VacanciesInteractorImpl @Inject constructor(
    private val vacanciesRepository: VacanciesRepository,
) : VacanciesInteractor {

    override val quantityFoundVacancies: StateFlow<Int?> = vacanciesRepository.quantityFoundVacancies

    override fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<PagingData<Vacancy>> {
        return vacanciesRepository.searchVacanciesPager(vacanciesRequest = vacanciesRequest)
    }

    override fun getVacancyDetails(vacancyId: String): Flow<SearchResult<VacancyDetails>> {
        return vacanciesRepository.getVacancyDetails(vacancyId = vacancyId)
    }

    override fun getSimilarVacancies(
        vacancyId: String,
        vacanciesRequest: VacanciesRequest
    ): Flow<SearchResult<Vacancies>> {
        return vacanciesRepository.getSimilarVacancies(vacancyId = vacancyId, vacanciesRequest = vacanciesRequest)
    }

    override suspend fun addVacancyToFavorites(vacancy: VacancyDetails) {
        vacanciesRepository.addVacancyToFavorites(vacancy)
    }

    override suspend fun removeVacancyFromFavorites(vacancyId: String) {
        vacanciesRepository.removeVacancyFromFavorites(vacancyId)
    }

    override fun getFavoriteVacancies(): Flow<List<Vacancy>> {
        return vacanciesRepository.getFavoriteVacancies()
    }

}
