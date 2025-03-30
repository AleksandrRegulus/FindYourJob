package ru.practicum.android.diploma.ui.root

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.navigation.BottomBar
import ru.practicum.android.diploma.ui.navigation.BottomBarScreen
import ru.practicum.android.diploma.ui.navigation.NavGraph

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

//    navController.addOnDestinationChangedListener { _, destination, _ ->
//        Log.d("MYLOG", "${destination.route}")
//
//    }

    Scaffold(
        modifier = Modifier,
        bottomBar = {
            BottomBar(
                navController = navController,
                modifier = Modifier.height(dimensionResource(R.dimen._56dp)),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavGraph(navHostController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {

    when (navController.currentDestination?.route) {
        BottomBarScreen.ScreenSearch.route -> {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.search_vacancy))
                }
            )
        }

        BottomBarScreen.ScreenFavorite.route -> {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.team))
                }
            )
        }

        BottomBarScreen.ScreenAbout.route -> {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.team))
                }
            )
        }

        else -> {
            TopAppBar(
                title = {
                    Text(text = "НИРАБОТАИТЬ")
                }
            )
        }
    }

}
