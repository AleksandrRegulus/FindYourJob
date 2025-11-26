package ru.practicum.android.diploma.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.theme.medium22
import ru.practicum.android.diploma.ui.theme.regular16

@Composable
fun VacancyCard(
    vacancy: Vacancy,
    onVacancyClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onVacancyClick(vacancy.id) })
            .fillMaxWidth()
            .padding(
                vertical = dimensionResource(R.dimen._8dp),
                horizontal = dimensionResource(R.dimen._16dp)
            )
    ) {
        AsyncImage(
            model = vacancy.employerLogoUrl,
            contentDescription = vacancy.name,
            modifier = Modifier.size(dimensionResource(R.dimen._48dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_placeholder_vacancy_item),
            error = painterResource(R.drawable.ic_placeholder_vacancy_item)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(R.dimen._12dp))
        ) {
            Text(
                text = "${vacancy.name}, ${vacancy.areaName}",
                style = MaterialTheme.typography.medium22
            )
            Text(
                text = vacancy.employerName,
                style = MaterialTheme.typography.regular16
            )
            Text(
                text = getSalaryStringAndSymbol(vacancy.salary),
                style = MaterialTheme.typography.regular16
            )
        }
    }
}
