package ru.practicum.android.diploma.di

import dagger.Component
import ru.practicum.android.diploma.ui.favorite.FavoriteFragment
import ru.practicum.android.diploma.ui.filter.FilterSettingsFragment
import ru.practicum.android.diploma.ui.root.RootActivity
import ru.practicum.android.diploma.ui.search.SearchFragment
import ru.practicum.android.diploma.ui.selections.area.AreaSelectionFragment
import ru.practicum.android.diploma.ui.selections.country.CountrySelectionFragment
import ru.practicum.android.diploma.ui.selections.industry.IndustrySelectionFragment
import ru.practicum.android.diploma.ui.selections.region.RegionSelectionFragment
import ru.practicum.android.diploma.ui.similar.SimilarVacanciesFragment
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, NetworkModule::class, DomainModule::class])
interface AppComponent {

    fun inject(rootActivity: RootActivity)
    fun inject(searchFragment: SearchFragment)
    fun inject(favoriteFragment: FavoriteFragment)
    fun inject(filterSettingsFragment: FilterSettingsFragment)
    fun inject(areaSelectionFragment: AreaSelectionFragment)
    fun inject(countrySelectionFragment: CountrySelectionFragment)
    fun inject(industrySelectionFragment: IndustrySelectionFragment)
    fun inject(regionSelectionFragment: RegionSelectionFragment)
    fun inject(vacancyFragment: VacancyFragment)
    fun inject(similarVacanciesFragment: SimilarVacanciesFragment)
}
