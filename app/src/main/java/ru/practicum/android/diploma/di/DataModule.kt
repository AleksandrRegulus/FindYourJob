package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.repository.FilterSearchRepositoryImpl
import ru.practicum.android.diploma.data.repository.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.share.ExternalNavigatorImpl
import ru.practicum.android.diploma.domain.api.FilterSearchRepository
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.share.ExternalNavigator
import javax.inject.Singleton

@Module
class DataModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("FIND_YOUR_JOB_PREFERENCES", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideRoom(context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "database.db")
            .build()
    }

    @Singleton
    @Provides
    fun provideVacanciesRepository(networkClient: NetworkClient, appDatabase: AppDatabase): VacanciesRepository {
        return VacanciesRepositoryImpl(networkClient, appDatabase)
    }

    @Singleton
    @Provides
    fun provideExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    @Singleton
    @Provides
    fun provideFilterSearchRepository(
        networkClient: NetworkClient,
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): FilterSearchRepository {
        return FilterSearchRepositoryImpl(networkClient, sharedPreferences, gson)
    }

}
