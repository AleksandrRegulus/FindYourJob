package ru.practicum.android.diploma.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.api.FilterSearchRepository
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.impl.FilterSearchInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacanciesInteractorImpl
import ru.practicum.android.diploma.domain.share.ExternalNavigator
import ru.practicum.android.diploma.domain.share.SharingInteractor
import ru.practicum.android.diploma.domain.share.impl.SharingInteractorImpl

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideVacanciesInteractor(vacanciesRepository: VacanciesRepository): VacanciesInteractor {
        return VacanciesInteractorImpl(vacanciesRepository)
    }

    @Provides
    fun provideSharingInteractor(externalNavigator: ExternalNavigator): SharingInteractor {
        return SharingInteractorImpl(externalNavigator)
    }

    @Provides
    fun provideFilterSearchInteractor(filterSearchRepository: FilterSearchRepository): FilterSearchInteractor {
        return FilterSearchInteractorImpl(filterSearchRepository)
    }

}
