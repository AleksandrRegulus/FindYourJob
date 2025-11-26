package ru.practicum.android.diploma.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.medium22

@Composable
fun ShowPlaceholder(textId: Int?, paintId: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(paintId),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        if (textId != null) {
            Text(
                text = stringResource(textId),
                style = MaterialTheme.typography.medium22,
                maxLines = 2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen._16dp),
                    start = dimensionResource(R.dimen._46dp),
                    end = dimensionResource(R.dimen._46dp)
                )
            )
        }
    }
}
