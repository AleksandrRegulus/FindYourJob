package ru.practicum.android.diploma.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.White
import ru.practicum.android.diploma.ui.theme.regular16

@Composable
fun FiltersButton(
    onClick: () -> Unit,
    text: String,
    containerColor: Color = Blue,
    textColor: Color = White
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen._16dp))
            .height(dimensionResource(R.dimen._60dp)),
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(R.dimen._12dp)),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.regular16,
            color = textColor,
        )
    }
}
