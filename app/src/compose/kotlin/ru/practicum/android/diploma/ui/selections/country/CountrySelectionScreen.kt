package ru.practicum.android.diploma.ui.selections.country

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.selections.country.CountrySelectionViewModel
import ru.practicum.android.diploma.presentation.selections.country.state.CountrySelectionState
import ru.practicum.android.diploma.ui.utils.FiltersUnit
import ru.practicum.android.diploma.ui.utils.NavigateBackButton
import ru.practicum.android.diploma.ui.utils.ShowLoading
import ru.practicum.android.diploma.ui.utils.ShowPlaceholder
import ru.practicum.android.diploma.ui.utils.TopBar

@Composable
fun CountrySelectionScreen(
    viewModel: CountrySelectionViewModel,
    onNavigateBack: () -> Unit,
) {

    val stateCountrySelection by viewModel.countryScreenState.collectAsState()

    Column {
        TopBar(
            text = stringResource(R.string.area_country_header),
            navigationIcon = { NavigateBackButton(onNavigateBack) }
        )

        when (val state = stateCountrySelection) {
            CountrySelectionState.Empty ->
                ShowPlaceholder(
                    textId = R.string.failed_to_retrieve_list,
                    paintId = R.drawable.png_nothing_found
                )

            CountrySelectionState.Error ->
                ShowPlaceholder(
                    textId = R.string.error_server,
                    paintId = R.drawable.error_server
                )

            CountrySelectionState.Loading ->
                ShowLoading()

            CountrySelectionState.NoInternet ->
                ShowPlaceholder(
                    textId = R.string.no_internet,
                    paintId = R.drawable.png_no_internet
                )

            is CountrySelectionState.Content ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.countries) { country ->
                        FiltersUnit(
                            titleText = country.name,
                            onClick = {
                                viewModel.saveCountry(country)
                                onNavigateBack()
                            }
                        )
                    }
                }
        }
    }
}
