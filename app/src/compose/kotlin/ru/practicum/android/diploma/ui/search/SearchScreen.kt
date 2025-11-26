package ru.practicum.android.diploma.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.ui.theme.Black
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.White
import ru.practicum.android.diploma.ui.theme.regular16
import ru.practicum.android.diploma.ui.utils.ShowLoading
import ru.practicum.android.diploma.ui.utils.ShowPlaceholder
import ru.practicum.android.diploma.ui.utils.TextFieldSearch
import ru.practicum.android.diploma.ui.utils.TopBar
import ru.practicum.android.diploma.ui.utils.VacancyCard

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onVacancyClick: (String) -> Unit,
    onFiltersClick: () -> Unit,
) {

    LaunchedEffect(Unit) {
        viewModel.searchNow()
    }

    val state by viewModel.state.collectAsState()

    Column {
        TopBar(
            text = stringResource(R.string.search_vacancy),
            actions = {
                IconButton(
                    onClick = { onFiltersClick() },
                ) {
                    SetFilterIcon(state.isFilters)
                }
            }
        )

        TextFieldSearch(
            text = state.searchText,
            placeholderText = stringResource(R.string.search),
            onTextChange = { newText -> viewModel.textFieldChange(newText) }
        )

        if (state.vacanciesFlow == null) {
            ShowStart()
        } else {
            ShowContent(state.vacanciesFlow!!, state.quantityFoundVacancies, onVacancyClick)
        }
    }
}

@Composable
private fun SetFilterIcon(isFilters: Boolean) {
    if (isFilters) {
        AsyncImage(
            model = R.drawable.ic_filter_on,
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen._24dp)),
        )
    } else {
        Icon(
            painter = painterResource(R.drawable.ic_filter_off),
            contentDescription = "Filter",
            tint = if (isSystemInDarkTheme()) White else Black
        )
    }
}

@Composable
private fun ShowStart() {
    ShowPlaceholder(textId = null, paintId = R.drawable.png_search)
}

@Composable
private fun ShowError() {
    ShowPlaceholder(textId = R.string.error_server, paintId = R.drawable.error_server)
}

@Composable
private fun ShowNoInternet() {
    ShowPlaceholder(
        textId = R.string.no_internet,
        paintId = R.drawable.png_no_internet
    )
//        onSnack(resources.getString(R.string.check_internet))
}

@Composable
private fun ShowEmpty(modifier: Modifier) {

    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen._16dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuantityVacanciesTextView(stringResource(R.string.nothing_found))
        ShowPlaceholder(
            textId = R.string.nothing_found_description,
            paintId = R.drawable.png_nothing_found
        )
    }
}

@Composable
private fun ShowContent(
    vacanciesFlow: Flow<PagingData<Vacancy>>,
    found: Int?,
    onVacancyClick: (String) -> Unit
) {

    val vacancies = vacanciesFlow.collectAsLazyPagingItems()
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = dimensionResource(R.dimen._32dp))
        ) {
            items(
                count = vacancies.itemCount,
                key = vacancies.itemKey()
            ) { key ->
                vacancies[key]?.let {
                    VacancyCard(it, onVacancyClick)
                }
            }

            when (vacancies.loadState.refresh) {

                is LoadState.Loading -> {
                    item {
                        keyboardController?.hide()
                        ShowLoading(Modifier.fillParentMaxSize())
                    }
                }

                is LoadState.Error -> {
                    if ((vacancies.loadState.refresh as LoadState.Error).error.message == "NO_INTERNET_CONNECTION") {
                        item {
                            ShowNoInternet()
                        }
                    } else {
                        item {
                            ShowError()
                        }
                    }
                }

                else -> {}
            }

            if (vacancies.loadState.isIdle) {
                if (vacancies.itemCount == 0) {
                    item {
                        ShowEmpty(
                            Modifier.fillParentMaxSize()
                        )
                    }
                }
            }

        }
        if (found != null && found > 0) {
            QuantityVacanciesTextView(pluralStringResource(R.plurals.vacancy_plurals, found, found))
        }
    }
}


@Composable
private fun QuantityVacanciesTextView(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.regular16,
        maxLines = 1,
        color = White,
        modifier = Modifier
            .padding(top = dimensionResource(R.dimen._4dp))
            .background(color = Blue, shape = RoundedCornerShape(dimensionResource(R.dimen._12dp)))
            .padding(
                horizontal = dimensionResource(R.dimen._12dp),
                vertical = dimensionResource(R.dimen._4dp)
            )
    )
}

