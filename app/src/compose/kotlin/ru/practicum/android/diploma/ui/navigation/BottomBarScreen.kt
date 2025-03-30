package ru.practicum.android.diploma.ui.navigation

import ru.practicum.android.diploma.R

sealed class BottomBarScreen(
    val title: String,
    val iconId: Int,
    val route: String
) {
    object ScreenSearch : BottomBarScreen(
        "Главная",
        R.drawable.ic_main,
        "screen_search"
    )

    object ScreenFavorite : BottomBarScreen(
        "Избранное",
        R.drawable.ic_favorites_on,
        "screen_favorite"
    )

    object ScreenAbout : BottomBarScreen(
        "Команда",
        R.drawable.ic_team,
        "screen_about"
    )
}
