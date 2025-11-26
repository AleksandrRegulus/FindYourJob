package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {
    @Insert(entity = FavoriteVacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: FavoriteVacancyEntity)

    @Query("DELETE FROM fav_vacancy_table WHERE id = :vacancyId")
    suspend fun deleteVacancy(vacancyId: String)

    @Query("SELECT * FROM fav_vacancy_table ORDER BY timeAddToFav DESC")
    fun getAllVacancies(): Flow<List<FavoriteVacancyEntity>>

    @Query("SELECT * FROM fav_vacancy_table WHERE id = :vacancyId")
    fun getVacancyById(vacancyId: String): FavoriteVacancyEntity
}
