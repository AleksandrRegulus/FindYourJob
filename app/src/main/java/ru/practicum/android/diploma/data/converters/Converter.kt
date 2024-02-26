package ru.practicum.android.diploma.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.data.dto.RequestVacanciesListSearch
import ru.practicum.android.diploma.data.dto.ResponseVacanciesListDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacanciesRequest
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

object Converter {

    fun fromVacanciesRequestToRequestVacanciesListSearch(
        vacanciesRequest: VacanciesRequest
    ): RequestVacanciesListSearch {
        return RequestVacanciesListSearch(
            page = vacanciesRequest.page,
            perPage = vacanciesRequest.perPage,
            text = vacanciesRequest.text,
            area = vacanciesRequest.area,
            industry = vacanciesRequest.industry,
            currency = vacanciesRequest.currency,
            salary = vacanciesRequest.salary,
            onlyWithSalary = vacanciesRequest.onlyWithSalary,
        )
    }

    fun fromResponseVacanciesListDtoToVacancies(responseVacanciesListDto: ResponseVacanciesListDto): Vacancies {
        return Vacancies(
            found = responseVacanciesListDto.found,
            page = responseVacanciesListDto.page,
            pages = responseVacanciesListDto.pages,
            items = responseVacanciesListDto
                .listVacancies
                .map { vacancyResponse ->
                    Vacancy(
                        id = vacancyResponse.id,
                        areaName = vacancyResponse.area.name,
                        employerLogoUrl = vacancyResponse.employer.logoUrls?.original.orEmpty(),
                        employerName = vacancyResponse.employer.name,
                        name = vacancyResponse.name,
                        salary = Salary(
                            currency = vacancyResponse.salary?.currency,
                            from = vacancyResponse.salary?.from,
                            to = vacancyResponse.salary?.to
                        )
                    )
                }
                .toList()
        )
    }

    fun fromVacancyDtoToVacancyDetails(vacancyDto: VacancyDto): VacancyDetails {
        return VacancyDetails(
            id = vacancyDto.id,
            address = vacancyDto.address.let { "${it.city}, ${it.street}, ${it.building}" },
            alternateUrl = vacancyDto.alternateUrl,
            areaName = vacancyDto.area.name,
            contactEmail = vacancyDto.contacts.email,
            contactName = vacancyDto.contacts.name,
            contactPhones = vacancyDto.contacts.phones.map { phonesResponseDto ->
                Phone(
                    comment = phonesResponseDto.comment,
                    phone = "+${phonesResponseDto.country} (${phonesResponseDto.city}) ${phonesResponseDto.number}"
                )
            },
            description = vacancyDto.description,
            employerLogoUrl = vacancyDto.employer.logoUrls.original,
            employerName = vacancyDto.employer.name,
            employment = vacancyDto.employment.name,
            experience = vacancyDto.experience.name,
            keySkills = vacancyDto.keySkills.map { keySkillsResponseDto -> keySkillsResponseDto.name },
            name = vacancyDto.name,
            salary = Salary(
                currency = vacancyDto.salary.currency,
                from = vacancyDto.salary.from,
                to = vacancyDto.salary.to
            ),
            schedule = vacancyDto.schedule.name
        )
    }

    fun fromFavoriteVacancyEntityToVacancyDetails(
        favoriteVacancyEntity: FavoriteVacancyEntity
    ): VacancyDetails {
        return VacancyDetails(
            id = favoriteVacancyEntity.id,
            address = favoriteVacancyEntity.address,
            alternateUrl = favoriteVacancyEntity.alternateUrl,
            areaName = favoriteVacancyEntity.areaName,
            contactEmail = favoriteVacancyEntity.contactsEmail,
            contactName = favoriteVacancyEntity.contactsName,
            contactPhones = mutableListOf<Phone>().also {
                val listType = object : TypeToken<List<Phone>>() {}.type
                it.addAll(
                    Gson().fromJson<List<Phone>>(favoriteVacancyEntity.contactsPhonesInJson, listType)
                )
            },
            description = favoriteVacancyEntity.description,
            employerLogoUrl = favoriteVacancyEntity.employerLogoUrl,
            employerName = favoriteVacancyEntity.employerName,
            employment = favoriteVacancyEntity.employment,
            experience = favoriteVacancyEntity.experience,
            keySkills = mutableListOf<String>().also {
                val listType = object : TypeToken<List<String>>() {}.type
                it.addAll(
                    Gson().fromJson<List<String>>(favoriteVacancyEntity.keySkillsInJson, listType)
                )
            },
            name = favoriteVacancyEntity.name,
            salary = Salary(
                currency = favoriteVacancyEntity.salaryCurrency,
                from = favoriteVacancyEntity.salaryFrom,
                to = favoriteVacancyEntity.salaryTo
            ),
            schedule = favoriteVacancyEntity.schedule
        )
    }

    fun fromVacancyDetailsToFavoriteVacancyEntity(vacancyDetails: VacancyDetails): FavoriteVacancyEntity {
        return FavoriteVacancyEntity(
            id = vacancyDetails.id,
            address = vacancyDetails.address,
            areaName = vacancyDetails.areaName,
            name = vacancyDetails.name,
            employerName = vacancyDetails.employerName,
            employerLogoUrl = vacancyDetails.employerLogoUrl,
            salaryCurrency = vacancyDetails.salary.currency.orEmpty(),
            salaryFrom = vacancyDetails.salary.from ?: -1,
            salaryTo = vacancyDetails.salary.to ?: -1,
            contactsEmail = vacancyDetails.contactEmail,
            contactsName = vacancyDetails.contactName,
            contactsPhonesInJson = Gson().toJson(vacancyDetails.contactPhones),
            description = vacancyDetails.description,
            employment = vacancyDetails.employment,
            experience = vacancyDetails.experience,
            keySkillsInJson = Gson().toJson(vacancyDetails.keySkills),
            alternateUrl = vacancyDetails.alternateUrl,
            schedule = vacancyDetails.schedule
        )
    }
}
