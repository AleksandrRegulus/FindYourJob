package ru.practicum.android.diploma.ui.navigation

sealed class Screen(val route: String) {

    data object VacancyDetail : Screen("vacancyDetail/{vacancyId}") {
        fun createRoute(vacancyId: String) = "vacancyDetail/$vacancyId"
    }

    data object FilterSettings : Screen("filter_settings")
    data object AreaSelection : Screen("area_selection")
    data object CountrySelection : Screen("country_selection")
    data object RegionSelection : Screen("region_selection")
    data object IndustrySelection : Screen("industry_selection")
}
