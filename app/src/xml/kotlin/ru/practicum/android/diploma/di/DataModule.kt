package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.repository.FilterSearchRepositoryImpl
import ru.practicum.android.diploma.data.repository.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.share.ExternalNavigatorImpl
import ru.practicum.android.diploma.domain.api.FilterSearchRepository
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.share.ExternalNavigator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindFilterSearchRepository(impl: FilterSearchRepositoryImpl): FilterSearchRepository

    @Binds
    fun bindVacanciesRepository(impl: VacanciesRepositoryImpl): VacanciesRepository

    @Binds
    fun bindExternalNavigator(impl: ExternalNavigatorImpl): ExternalNavigator

    companion object {

        @Provides
        fun provideGson(): Gson {
            return Gson()
        }

        @Singleton
        @Provides
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences("FIND_YOUR_JOB_PREFERENCES", Context.MODE_PRIVATE)
        }

        @Singleton
        @Provides
        fun provideRoom(@ApplicationContext context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, "database.db")
                .build()
        }

    }
}
