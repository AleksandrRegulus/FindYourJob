package ru.practicum.android.diploma.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Gray
import ru.practicum.android.diploma.ui.theme.regular12

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    navDestination: NavDestination?,
    navController: NavController
) {
    NavigationBarItem(
        label = {
            Text(
                modifier = Modifier.offset(y = dimensionResource(R.dimen._minus_6dp)),
                text = screen.title,
                style = MaterialTheme.typography.regular12
            )
        },
        selected = navDestination?.route == screen.route,
        onClick = {
            navController.navigate(screen.route)
        },
        icon = {
            Icon(
                modifier = Modifier.padding(top = dimensionResource(R.dimen._8dp)),
                painter = painterResource(id = screen.iconId),
                contentDescription = "Icon"
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent,
            unselectedTextColor = Gray,
            selectedTextColor = Blue,
            selectedIconColor = Blue,
            unselectedIconColor = Gray
        ),
    )
}

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    bottomBarState: MutableState<Boolean>
) {
    val screens = listOf(
        BottomBarScreen.ScreenSearch,
        BottomBarScreen.ScreenFavorite,
        BottomBarScreen.ScreenAbout
    )
    if (bottomBarState.value) {
        Column {
            HorizontalDivider(
                thickness = 1.dp,
                color = Gray
            )
            NavigationBar(modifier = modifier) {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination
                screens.forEach { screen ->
                    AddItem(
                        screen = screen,
                        navDestination = currentDestination,
                        navController = navController
                    )
                }
            }
        }
    }
}
