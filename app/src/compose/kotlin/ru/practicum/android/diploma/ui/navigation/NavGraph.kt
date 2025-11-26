package ru.practicum.android.diploma.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.practicum.android.diploma.ui.about.AboutScreen
import ru.practicum.android.diploma.ui.favorite.FavoriteScreen
import ru.practicum.android.diploma.ui.filter.FilterSettings
import ru.practicum.android.diploma.ui.search.SearchScreen
import ru.practicum.android.diploma.ui.selections.area.AreaSelectionScreen
import ru.practicum.android.diploma.ui.selections.country.CountrySelectionScreen
import ru.practicum.android.diploma.ui.selections.industry.IndustrySelectionScreen
import ru.practicum.android.diploma.ui.selections.region.RegionSelectionScreen
import ru.practicum.android.diploma.ui.vacancy.VacancyDetailsScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    NavHost(
        navController = navHostController,
        startDestination = BottomBarScreen.ScreenSearch.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        composable(route = BottomBarScreen.ScreenSearch.route) {
            bottomBarState.value = true
            SearchScreen(
                viewModel = hiltViewModel(viewModelStoreOwner),
                onVacancyClick = { vacancyId ->
                    navHostController.navigate(Screen.VacancyDetail.createRoute(vacancyId))
                },
                onFiltersClick = { navHostController.navigate(Screen.FilterSettings.route) }
            )
        }
        composable(route = BottomBarScreen.ScreenFavorite.route) {
            bottomBarState.value = true
            FavoriteScreen(
                viewModel = hiltViewModel(),
                onVacancyClick = { vacancyId ->
                    navHostController.navigate(Screen.VacancyDetail.createRoute(vacancyId))
                }
            )
        }
        composable(route = BottomBarScreen.ScreenAbout.route) {
            bottomBarState.value = true
            AboutScreen()
        }

        composable(
            route = Screen.VacancyDetail.route,
            arguments = listOf(
                navArgument("vacancyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            bottomBarState.value = false
            val vacancyId = backStackEntry.arguments?.getString("vacancyId") ?: return@composable
            VacancyDetailsScreen(
                vacancyId = vacancyId,
                viewModel = hiltViewModel(),
                onNavigateBack = { navHostController.popBackStack() }
            )
        }

        composable(
            route = Screen.FilterSettings.route,
        ) {
            bottomBarState.value = false
            FilterSettings(
                viewModel = hiltViewModel(),
                onNavigateBack = { navHostController.popBackStack() },
                onAreaNavigateClick = { navHostController.navigate(Screen.AreaSelection.route) },
                onIndustryNavigateClick = { navHostController.navigate(Screen.IndustrySelection.route) }
            )
        }

        composable(
            route = Screen.AreaSelection.route,
        ) {
            bottomBarState.value = false
            AreaSelectionScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = { navHostController.popBackStack() },
                onCountryNavigateClick = { navHostController.navigate(Screen.CountrySelection.route) },
                onRegionNavigateClick = { navHostController.navigate(Screen.RegionSelection.route) }
            )
        }

        composable(
            route = Screen.CountrySelection.route,
        ) {
            bottomBarState.value = false
            CountrySelectionScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = { navHostController.popBackStack() },
            )
        }

        composable(
            route = Screen.RegionSelection.route,
        ) {
            bottomBarState.value = false
            RegionSelectionScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = { navHostController.popBackStack() },
            )
        }

        composable(
            route = Screen.IndustrySelection.route,
        ) {
            bottomBarState.value = false
            IndustrySelectionScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = { navHostController.popBackStack() },
            )
        }
    }
}
