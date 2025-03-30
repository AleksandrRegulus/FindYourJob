package ru.practicum.android.diploma.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.ComposeTheme
import ru.practicum.android.diploma.ui.theme.medium16
import ru.practicum.android.diploma.ui.theme.medium22
import ru.practicum.android.diploma.ui.theme.mediumBold32

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    val developers = listOf(
        stringResource(R.string.pasha),
        stringResource(R.string.dasha),
        stringResource(R.string.sasha),
        stringResource(R.string.valera)
    )

    Column {
        TopAppBar(
            expandedHeight = dimensionResource(R.dimen._64dp),
            title = {
                Text(
                    text = stringResource(R.string.team),
                    style = MaterialTheme.typography.medium22
                )
            },
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(
                    start = dimensionResource(R.dimen._16dp),
                    end = dimensionResource(R.dimen._16dp),
                    top = dimensionResource(R.dimen._16dp)
                )
        ) {
            Text(
                text = stringResource(R.string.worked_by),
                style = MaterialTheme.typography.mediumBold32,
                maxLines = 2,
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen._8dp),
                    bottom = dimensionResource(R.dimen._16dp)
                )
            )
            developers.forEach { name ->
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen._16dp)))
                Text(
                    text = name,
                    style = MaterialTheme.typography.medium16,
                )
            }
        }
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    ComposeTheme {
        AboutScreen()
    }
}
