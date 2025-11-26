package ru.practicum.android.diploma.ui.selections.area

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.selections.area.AreaSelectionViewModel
import ru.practicum.android.diploma.ui.theme.Gray
import ru.practicum.android.diploma.ui.utils.FiltersButton
import ru.practicum.android.diploma.ui.utils.FiltersUnit
import ru.practicum.android.diploma.ui.utils.NavigateBackButton
import ru.practicum.android.diploma.ui.utils.TopBar

@Composable
fun AreaSelectionScreen(
    viewModel: AreaSelectionViewModel,
    onNavigateBack: () -> Unit,
    onCountryNavigateClick: () -> Unit,
    onRegionNavigateClick: () -> Unit,
) {

    val state by viewModel.filterParametersState.collectAsState()

    Column {
        TopBar(
            text = stringResource(R.string.area_selection_header),
            navigationIcon = { NavigateBackButton(onNavigateBack) }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._16dp)))
        FiltersUnit(
            titleText = stringResource(R.string.filter_country),
            text = state.nameCountry,
            onClick = onCountryNavigateClick,
            onCloseClick = { viewModel.onCountryClearClick() },
            titleTextColor = Gray
        )
        FiltersUnit(
            titleText = stringResource(R.string.filter_region),
            text = state.nameRegion,
            onClick = onRegionNavigateClick,
            onCloseClick = { viewModel.onRegionClearClick() },
            titleTextColor = Gray
        )
        if (state.nameCountry.isNotBlank() || state.nameRegion.isNotBlank()) {
            Spacer(modifier = Modifier.weight(1f))
            FiltersButton(
                onClick = onNavigateBack,
                text = stringResource(R.string.filter_button_approve)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen._24dp)))
        }
    }
}
