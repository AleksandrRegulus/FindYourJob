package ru.practicum.android.diploma.ui.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.favorite.FavoriteViewModel
import ru.practicum.android.diploma.presentation.favorite.state.FavoritesState
import ru.practicum.android.diploma.ui.utils.ShowLoading
import ru.practicum.android.diploma.ui.utils.ShowPlaceholder
import ru.practicum.android.diploma.ui.utils.TopBar
import ru.practicum.android.diploma.ui.utils.VacancyCard

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel,
    onVacancyClick: (String) -> Unit
) {

    val favoriteScreenState = viewModel.state.collectAsState()

    Column {
        TopBar(text = stringResource(R.string.favorite))

        when (val state = favoriteScreenState.value) {
            is FavoritesState.Loading -> {
                ShowLoading()
            }

            is FavoritesState.Empty -> {
                ShowPlaceholder(textId = R.string.favorites_are_empty, paintId = R.drawable.empty_favorites)
            }

            is FavoritesState.Error -> {
                ShowPlaceholder(textId = R.string.error_favorites, paintId = R.drawable.error_favorites)
            }

            is FavoritesState.Content -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = dimensionResource(R.dimen._16dp))
                ) {
                    items(state.favorites) { favorite ->
                        VacancyCard(favorite, onVacancyClick)
                    }
                }
            }
        }
    }
}

