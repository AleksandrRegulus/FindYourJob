package ru.practicum.android.diploma.ui.selections.industry

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.selections.industry.IndustrySelectionViewModel
import ru.practicum.android.diploma.presentation.selections.industry.state.IndustrySelectionState
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.regular16
import ru.practicum.android.diploma.ui.utils.FiltersButton
import ru.practicum.android.diploma.ui.utils.NavigateBackButton
import ru.practicum.android.diploma.ui.utils.ShowLoading
import ru.practicum.android.diploma.ui.utils.ShowPlaceholder
import ru.practicum.android.diploma.ui.utils.TextFieldSearch
import ru.practicum.android.diploma.ui.utils.TopBar

@Composable
fun IndustrySelectionScreen(
    viewModel: IndustrySelectionViewModel,
    onNavigateBack: () -> Unit,
) {

    val stateIndustrySelection by viewModel.industryScreenState.collectAsState()
    var textFilter by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        TopBar(
            text = stringResource(R.string.industry_header),
            navigationIcon = { NavigateBackButton(onNavigateBack) }
        )

        TextFieldSearch(
            text = textFilter,
            placeholderText = stringResource(R.string.industry_search),
            onTextChange = { newText ->
                textFilter = newText
                viewModel.onTextChange(newText)
            }
        )

        when (val state = stateIndustrySelection) {
            IndustrySelectionState.Empty ->
                ShowPlaceholder(
                    textId = R.string.failed_to_retrieve_list,
                    paintId = R.drawable.png_nothing_found
                )

            IndustrySelectionState.Error ->
                ShowPlaceholder(
                    textId = R.string.error_server,
                    paintId = R.drawable.error_server
                )

            IndustrySelectionState.Loading ->
                ShowLoading()

            IndustrySelectionState.NoInternet ->
                ShowPlaceholder(
                    textId = R.string.no_internet,
                    paintId = R.drawable.png_no_internet
                )

            is IndustrySelectionState.Content -> {

                if (state.industries.isEmpty()) {
                    ShowPlaceholder(
                        textId = R.string.nothing_found_industries,
                        paintId = R.drawable.png_nothing_found
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                    ) {
                        items(state.industries) { industry ->
                            RadioButtonRow(
                                text = industry.name,
                                onClick = {
                                    viewModel.selectIndustry(industry)
                                    keyboardController?.hide()
                                },
                                selected = industry.id == state.idSelectedIndustry
                            )
                        }
                    }
                    if (state.idSelectedIndustry.isNotBlank()) {
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._8dp)))
                        FiltersButton(
                            onClick = {
                                viewModel.saveSelectedIndustry()
                                onNavigateBack()
                            },
                            text = stringResource(R.string.filter_button_approve)
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._24dp)))
                    }
                }
            }
        }
    }
}

@Composable
private fun RadioButtonRow(
    text: String = "",
    onClick: () -> Unit,
    selected: Boolean
) {

    Row(
        verticalAlignment = CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(R.dimen._16dp),
                end = dimensionResource(R.dimen._6dp)
            )
            .height(dimensionResource(R.dimen._60dp))
            .clickable(onClick = onClick)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = MaterialTheme.typography.regular16,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Blue,
                unselectedColor = Blue
            ),
        )
    }
}
