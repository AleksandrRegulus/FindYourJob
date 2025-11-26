package ru.practicum.android.diploma.ui.filter

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.presentation.filter.FilterSettingsViewModel
import ru.practicum.android.diploma.ui.theme.Black
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Gray
import ru.practicum.android.diploma.ui.theme.Red
import ru.practicum.android.diploma.ui.theme.White
import ru.practicum.android.diploma.ui.theme.regular12
import ru.practicum.android.diploma.ui.theme.regular16
import ru.practicum.android.diploma.ui.utils.FiltersButton
import ru.practicum.android.diploma.ui.utils.FiltersUnit
import ru.practicum.android.diploma.ui.utils.NavigateBackButton
import ru.practicum.android.diploma.ui.utils.TopBar

@Composable
fun FilterSettings(
    viewModel: FilterSettingsViewModel,
    onNavigateBack: () -> Unit,
    onAreaNavigateClick: () -> Unit,
    onIndustryNavigateClick: () -> Unit,
) {

    val state by viewModel.filterParametersState.collectAsState()

    Column {
        TopBar(
            text = stringResource(R.string.filter_settings),
            navigationIcon = { NavigateBackButton(onNavigateBack) }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._16dp)))
        val comma = if (state.nameCountry.isBlank() || state.nameRegion.isBlank()) "" else ", "
        val areaName = "${state.nameCountry.ifBlank { "" }}$comma${state.nameRegion.ifBlank { "" }}"
        FiltersUnit(
            titleText = stringResource(R.string.area),
            text = areaName,
            onClick = onAreaNavigateClick,
            onCloseClick = { viewModel.onAreaClearClick() },
            titleTextColor = Gray
        )
        FiltersUnit(
            titleText = stringResource(R.string.industry),
            text = state.nameIndustry,
            onClick = onIndustryNavigateClick,
            onCloseClick = { viewModel.onIndustryClearClick() },
            titleTextColor = Gray
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._24dp)))
        SalaryUnit(
            salary = state.salary,
            onValueChange = { newText ->
                viewModel.onTextSalaryChange(newText)
            },
            onClearClick = { viewModel.onClearClick() }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._24dp)))
        SettingsCheckbox(
            titleText = stringResource(R.string.do_not_show_without_salary),
            checked = state.doNotShowWithoutSalary,
            onCheckedChange = { newCheckedValue ->
                viewModel.onCheckedChange(newCheckedValue)
            }
        )
        if (state != FilterParameters.initial) {
            Spacer(modifier = Modifier.weight(1f))
            ButtonsSection(
                onNavigateBack = onNavigateBack,
                onResetClick = { viewModel.onResetClick() }
            )
        }
    }
}

@Composable
private fun SalaryUnit(
    salary: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit
) {

    var isFocused by remember { mutableStateOf(false) }

    val icClose = @Composable {
        IconButton(
            onClick = onClearClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Icon"
            )
        }
    }

    Box(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen._16dp))
            .height(dimensionResource(R.dimen._50dp))
    ) {

        TextField(
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            shape = RoundedCornerShape(dimensionResource(R.dimen._12dp)),
            value = salary,
            textStyle = MaterialTheme.typography.regular16,
            singleLine = true,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                if (isFocused || salary.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.expected_salary),
                        style = MaterialTheme.typography.regular12,
                    )
                }
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.enter_the_amount),
                    style = MaterialTheme.typography.regular16,
                    color = if (isSystemInDarkTheme()) White else Gray,
                )
            },
            trailingIcon = if (salary.isNotEmpty()) icClose else null,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTrailingIconColor = Black,
                unfocusedTrailingIconColor = Black,
                focusedTextColor = Black,
                unfocusedTextColor = Black,
                unfocusedLabelColor = Black
            ),
        )

        if (!isFocused && salary.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = dimensionResource(R.dimen._8dp),
                        horizontal = dimensionResource(R.dimen._16dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.expected_salary),
                    color = if (isSystemInDarkTheme()) White else Gray,
                    style = MaterialTheme.typography.regular12,
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen._1dp)))
                Text(
                    text = stringResource(R.string.enter_the_amount),
                    style = MaterialTheme.typography.regular16,
                    color = if (isSystemInDarkTheme()) White else Gray,
                )
            }
        }
    }
}


@Composable
private fun SettingsCheckbox(
    titleText: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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
    ) {

        Text(
            text = titleText,
            style = MaterialTheme.typography.regular16,
        )
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            checked = checked, onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(uncheckedColor = Blue)
        )
    }
}

@Composable
private fun ButtonsSection(
    onNavigateBack: () -> Unit,
    onResetClick: () -> Unit,
) {
    FiltersButton(
        onClick = onNavigateBack,
        text = stringResource(R.string.apply)
    )
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen._8dp)))
    FiltersButton(
        onClick = onResetClick,
        text = stringResource(R.string.reset),
        containerColor = Color.Transparent,
        textColor = Red
    )
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen._24dp)))
}
