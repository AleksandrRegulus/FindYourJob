package ru.practicum.android.diploma.ui.selections.region

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.selections.region.RegionSelectionViewModel
import ru.practicum.android.diploma.presentation.selections.region.state.RegionSelectionState
import ru.practicum.android.diploma.ui.utils.FiltersUnit
import ru.practicum.android.diploma.ui.utils.NavigateBackButton
import ru.practicum.android.diploma.ui.utils.ShowLoading
import ru.practicum.android.diploma.ui.utils.ShowPlaceholder
import ru.practicum.android.diploma.ui.utils.TextFieldSearch
import ru.practicum.android.diploma.ui.utils.TopBar

@Composable
fun RegionSelectionScreen(
    viewModel: RegionSelectionViewModel,
    onNavigateBack: () -> Unit,
) {

    val stateRegionSelection by viewModel.regionScreenState.collectAsState()
    var textFilter by rememberSaveable { mutableStateOf("") }

    Column {
        TopBar(
            text = stringResource(R.string.area_region_header),
            navigationIcon = { NavigateBackButton(onNavigateBack) }
        )

        TextFieldSearch(
            text = textFilter,
            placeholderText = stringResource(R.string.region_search),
            onTextChange = { newText ->
                textFilter = newText
                viewModel.onTextChange(newText)
            }
        )

        when (val state = stateRegionSelection) {
            RegionSelectionState.Empty ->
                ShowPlaceholder(
                    textId = R.string.failed_to_retrieve_list,
                    paintId = R.drawable.png_no_regions
                )

            RegionSelectionState.Error ->
                ShowPlaceholder(
                    textId = R.string.error_server,
                    paintId = R.drawable.error_server
                )

            RegionSelectionState.Loading ->
                ShowLoading()

            RegionSelectionState.NoInternet ->
                ShowPlaceholder(
                    textId = R.string.no_internet,
                    paintId = R.drawable.png_no_internet
                )

            is RegionSelectionState.Content -> {
                if (state.regions.isEmpty()) {
                    ShowPlaceholder(
                        textId = R.string.nothing_found_regions,
                        paintId = R.drawable.png_nothing_found
                    )
                } else {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen._8dp)))
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.regions) { region ->
                            FiltersUnit(
                                titleText = region.name,
                                onClick = {
                                    viewModel.saveRegion(region)
                                    onNavigateBack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


