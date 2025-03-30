package ru.practicum.android.diploma.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.practicum.android.diploma.ui.about.AboutScreen
import ru.practicum.android.diploma.ui.favorite.FavoriteScreen
import ru.practicum.android.diploma.ui.search.SearchScreen

@Composable
fun NavGraph(
    navHostController: NavHostController
) {
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
//            val parentEntry = remember { navHostController.getBackStackEntry(BottomBarScreen.ScreenSearch.route) }
//            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen()
        }
        composable(route = BottomBarScreen.ScreenFavorite.route) {
            FavoriteScreen()
        }
        composable(route = BottomBarScreen.ScreenAbout.route) {
            AboutScreen()
        }
    }
}
