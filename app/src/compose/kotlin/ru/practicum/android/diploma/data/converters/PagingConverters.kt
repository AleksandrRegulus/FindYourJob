package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Vacancy

object PagingConverters {

    fun fromResponseVacanciesListDtoToListVacancyEntity(
        responseVacanciesListDto: ResponseVacanciesListDto,
        page: Int
    ): List<VacancyEntity> {
        return responseVacanciesListDto.listVacancies
            .map { vacancyResponse ->
                VacancyEntity(
                    id = vacancyResponse.id.toInt(),
                    areaName = vacancyResponse.area.name,
                    name = vacancyResponse.name,
                    employerName = vacancyResponse.employer.name,
                    employerLogoUrl = vacancyResponse.employer.logoUrls?.original.orEmpty(),
                    salaryCurrency = vacancyResponse.salary?.currency,
                    salaryFrom = vacancyResponse.salary?.from,
                    salaryTo = vacancyResponse.salary?.to,
                    page = page
                )
            }
            .toList()
    }

    fun fromVacancyEntityToVacancy(vacancyEntity: VacancyEntity): Vacancy {
        return Vacancy(
            id = vacancyEntity.id.toString(),
            areaName = vacancyEntity.areaName,
            employerLogoUrl = vacancyEntity.employerLogoUrl,
            employerName = vacancyEntity.employerName,
            name = vacancyEntity.name,
            salary = Salary(
                currency = vacancyEntity.salaryCurrency,
                from = vacancyEntity.salaryFrom,
                to = vacancyEntity.salaryTo
            )
        )
    }
}


