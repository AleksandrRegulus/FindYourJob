package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey
    val id: Int,
    val areaName: String,
    val name: String,
    val employerName: String,
    val employerLogoUrl: String,
    val salaryCurrency: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,

    var page: Int,
)
