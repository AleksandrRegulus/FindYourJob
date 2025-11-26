package ru.practicum.android.diploma.presentation.favorite.state

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface FavoritesState {

    data class Content(val favorites: List<Vacancy>) : FavoritesState

    data object Empty : FavoritesState

    data object Error : FavoritesState

    data object Loading : FavoritesState

}
