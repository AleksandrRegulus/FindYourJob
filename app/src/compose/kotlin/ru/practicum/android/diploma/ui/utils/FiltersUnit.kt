package ru.practicum.android.diploma.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Black
import ru.practicum.android.diploma.ui.theme.White
import ru.practicum.android.diploma.ui.theme.regular12
import ru.practicum.android.diploma.ui.theme.regular16

@Composable
fun FiltersUnit(
    titleText: String,
    text: String = "",
    onClick: () -> Unit,
    onCloseClick: () -> Unit = {},
    titleTextColor: Color = Color.Unspecified
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
        if (text.isEmpty()) {
            Text(
                modifier = Modifier.weight(1f),
                text = titleText,
                style = MaterialTheme.typography.regular16,
                maxLines = 2,
                color = titleTextColor,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = titleText,
                    maxLines = 1,
                    style = MaterialTheme.typography.regular12,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = text,
                    maxLines = 2,
                    style = MaterialTheme.typography.regular16,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (text.isBlank()) {
            SettingsIconButton(onClick = onClick, icon = painterResource(R.drawable.ic_arrow_forward))
        } else {
            SettingsIconButton(onClick = onCloseClick, icon = painterResource(R.drawable.ic_close))
        }
    }
}

@Composable
private fun SettingsIconButton(
    onClick: () -> Unit,
    icon: Painter
) {
    IconButton(
        modifier = Modifier
            .height(dimensionResource(R.dimen._48dp))
            .width(dimensionResource(R.dimen._48dp)),
        onClick = onClick
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = if (isSystemInDarkTheme()) White else Black
        )
    }
}
