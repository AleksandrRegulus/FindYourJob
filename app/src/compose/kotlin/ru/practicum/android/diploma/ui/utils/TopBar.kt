package ru.practicum.android.diploma.ui.utils

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.medium22

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    text: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        expandedHeight = dimensionResource(R.dimen._64dp),
        navigationIcon = navigationIcon,
        actions = actions,
        title = {
            Text(
                text = text,
                style = MaterialTheme.typography.medium22
            )
        },
    )
}
