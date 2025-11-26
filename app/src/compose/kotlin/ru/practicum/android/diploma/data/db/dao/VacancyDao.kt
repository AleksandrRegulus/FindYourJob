package ru.practicum.android.diploma.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vacancies: List<VacancyEntity>)

    @Query("Select * From vacancy_table Order By page")
    fun getVacancies(): PagingSource<Int, VacancyEntity>

    @Query("Delete From vacancy_table")
    suspend fun clearAllVacancies()
}
