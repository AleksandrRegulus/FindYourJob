package ru.practicum.android.diploma.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.practicum.android.diploma.domain.api.FilterSearchInteractor
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.impl.FilterSearchInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacanciesInteractorImpl
import ru.practicum.android.diploma.domain.share.SharingInteractor
import ru.practicum.android.diploma.domain.share.impl.SharingInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface DomainModule {

    @Binds
    fun bindVacanciesInteractor(impl: VacanciesInteractorImpl): VacanciesInteractor

    @Binds
    fun bindSharingInteractor(impl: SharingInteractorImpl): SharingInteractor

    @Binds
    fun bindFilterSearchInteractor(impl: FilterSearchInteractorImpl): FilterSearchInteractor
}
