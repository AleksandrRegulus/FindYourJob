package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.practicum.android.diploma.data.datastore.FilterParametersSerializer
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.repository.FilterSearchRepositoryImpl
import ru.practicum.android.diploma.data.repository.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.share.ExternalNavigatorImpl
import ru.practicum.android.diploma.domain.api.FilterSearchRepository
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.share.ExternalNavigator
import javax.inject.Qualifier
import javax.inject.Singleton

private const val DATASTORE_PREFERENCES = "DATASTORE_PREFERENCES.pb"

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
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
            context.getSharedPreferences("FIND_YOUR_JOB_PREFERENCES", Context.MODE_PRIVATE)

        @Provides
        @IODispatcher
        fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @DefaultDispatcher
        fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

        @Provides
        @Singleton
        @ApplicationScope
        fun providesCoroutineScope(
            @DefaultDispatcher dispatcher: CoroutineDispatcher,
        ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

        @Singleton
        @Provides
        fun provideFilterParamsDataStore(
            @ApplicationContext context: Context,
            @IODispatcher ioDispatcher: CoroutineDispatcher,
            @ApplicationScope scope: CoroutineScope,
            filterParametersSerializer: FilterParametersSerializer
        ): DataStore<FilterParameters> =
            DataStoreFactory.create(
                serializer = filterParametersSerializer,
                scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            ) {
                context.dataStoreFile(DATASTORE_PREFERENCES)
            }

        @Singleton
        @Provides
        fun provideRoom(@ApplicationContext context: Context): AppDatabase =
            Room
                .databaseBuilder(context, AppDatabase::class.java, "database.db")
                .build()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
