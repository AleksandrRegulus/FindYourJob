package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.SearchResult
import javax.inject.Inject

class VacanciesInteractorImpl @Inject constructor(
    private val vacanciesRepository: VacanciesRepository
) : VacanciesInteractor {

    override fun searchVacancies(vacanciesRequest: VacanciesRequest): Flow<SearchResult<Vacancies>> {
        return vacanciesRepository.searchVacancies(vacanciesRequest = vacanciesRequest)
    }

    override fun getVacancyDetails(vacancyId: String): Flow<SearchResult<VacancyDetails>> {
        return vacanciesRepository.getVacancyDetails(vacancyId = vacancyId)
    }

    override fun getVacancyDetailsFromLocalStorage(vacancyId: String): Flow<VacancyDetails> {
        return vacanciesRepository.getVacancyDetailsFromLocalStorage(vacancyId = vacancyId)
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
