package ru.practicum.android.diploma.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Black
import ru.practicum.android.diploma.ui.theme.Gray
import ru.practicum.android.diploma.ui.theme.White

@Composable
fun TextFieldSearch(
    text: String,
    placeholderText: String,
    onTextChange: (String) -> Unit,
) {
    val icClose = @Composable {
        IconButton(
            onClick = {
                onTextChange("")
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                tint = Black,
                contentDescription = "Icon"
            )
        }
    }

    val icSearch = @Composable {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            tint = Black,
            contentDescription = "Icon"
        )
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen._16dp),
                vertical = dimensionResource(R.dimen._8dp)
            ),
        shape = RoundedCornerShape(dimensionResource(R.dimen._12dp)),
        value = text,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholderText,
                color = if (isSystemInDarkTheme()) White else Black,
            )
        },
        onValueChange = { newText -> onTextChange(newText) },
        trailingIcon = if (text.isNotEmpty()) icClose else icSearch,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedTextColor = Black,
            focusedTextColor = Black,
            focusedContainerColor = Gray,
            unfocusedContainerColor = Gray
        )
    )
}
