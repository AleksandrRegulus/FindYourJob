package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.data.db.dao.RemoteKeysDao
import ru.practicum.android.diploma.data.db.dao.VacancyDao
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.data.db.entity.RemoteKeys
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Database(
    version = 1,
    entities = [
        FavoriteVacancyEntity::class,
        VacancyEntity::class,
        RemoteKeys::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteVacancyDao(): FavoriteVacancyDao
    abstract fun getVacanciesDao(): VacancyDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}
