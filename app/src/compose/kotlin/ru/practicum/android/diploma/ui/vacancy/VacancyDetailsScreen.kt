package ru.practicum.android.diploma.ui.vacancy

import android.text.Html
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.presentation.vacancy.VacancyScreenState
import ru.practicum.android.diploma.presentation.vacancy.VacancyViewModel
import ru.practicum.android.diploma.ui.theme.Black
import ru.practicum.android.diploma.ui.theme.LightGray
import ru.practicum.android.diploma.ui.theme.Red
import ru.practicum.android.diploma.ui.theme.White
import ru.practicum.android.diploma.ui.theme.medium16
import ru.practicum.android.diploma.ui.theme.medium22
import ru.practicum.android.diploma.ui.theme.mediumBold32
import ru.practicum.android.diploma.ui.theme.regular16
import ru.practicum.android.diploma.ui.utils.NavigateBackButton
import ru.practicum.android.diploma.ui.utils.ShowLoading
import ru.practicum.android.diploma.ui.utils.ShowPlaceholder
import ru.practicum.android.diploma.ui.utils.TopBar
import ru.practicum.android.diploma.ui.utils.getSalaryStringAndSymbol

@Composable
fun VacancyDetailsScreen(
    vacancyId: String,
    viewModel: VacancyViewModel,
    onNavigateBack: () -> Unit
) {

    viewModel.getVacancyDetails(vacancyId)
    val vacancyScreenState = viewModel.vacancyScreenState.collectAsState()
    val isFavorite = (vacancyScreenState.value is VacancyScreenState.Content) &&
        (vacancyScreenState.value as VacancyScreenState.Content).vacancy.isFavorite

    Column {
        TopBar(
            text = stringResource(R.string.vacancy),
            navigationIcon = { NavigateBackButton(onNavigateBack) },
            actions = {
                IconButton(
                    onClick = { viewModel.shareVacancy("") },
                    enabled = vacancyScreenState.value is VacancyScreenState.Content
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_sharing),
                        tint = if (isSystemInDarkTheme()) White else Black,
                        contentDescription = "Share"
                    )
                }
                IconButton(
                    onClick = { viewModel.favoriteButtonClick() },
                    enabled = vacancyScreenState.value is VacancyScreenState.Content,
                ) {
                    if (isFavorite) {
                        Icon(
                            painter = painterResource(R.drawable.ic_favorites_on),
                            tint = Red,
                            contentDescription = "Favorite"
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.ic_favorites_off),
                            tint = if (isSystemInDarkTheme()) White else Black,
                            contentDescription = "Favorite"
                        )
                    }
                }
            }
        )

        when (val state = vacancyScreenState.value) {
            is VacancyScreenState.Loading -> {
                ShowLoading()
            }

            is VacancyScreenState.NoInternetConnection -> {
                ShowPlaceholder(textId = R.string.no_internet, paintId = R.drawable.png_no_internet)
            }

            is VacancyScreenState.ServerError -> {
                ShowPlaceholder(textId = R.string.error_server, paintId = R.drawable.error_server)
            }

            is VacancyScreenState.Content -> {
                ShowContent(state.vacancy, viewModel)
            }
        }

    }

}

@Composable
private fun ShowContent(
    vacancy: VacancyDetails,
    viewModel: VacancyViewModel
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen._16dp))
    ) {
        Text(
            text = vacancy.name,
            style = MaterialTheme.typography.mediumBold32,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._4dp)))
        Text(
            text = getSalaryStringAndSymbol(vacancy.salary),
            style = MaterialTheme.typography.medium22,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._24dp)))

        EmployerNameLogoBlock(vacancy)

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._24dp)))
        Text(
            text = stringResource(R.string.experience),
            style = MaterialTheme.typography.medium16,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._4dp)))
        Text(
            text = vacancy.experience,
            style = MaterialTheme.typography.regular16,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._8dp)))
        Text(
            text = stringResource(R.string.employment_schedule, vacancy.employment, vacancy.schedule),
            style = MaterialTheme.typography.regular16,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._32dp)))
        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.medium22,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen._16dp)))
        Text(
            text = Html.fromHtml(vacancy.description, Html.FROM_HTML_MODE_COMPACT).toString(),
            style = MaterialTheme.typography.regular16,
        )

        if (vacancy.keySkills.isNotEmpty()) {
            KeySkillsBlock(keySkillsToString(vacancy.keySkills))
        }

        if (!vacancy.contactEmail.isNullOrEmpty()
            || !vacancy.contactName.isNullOrEmpty()
            || !vacancy.contactPhones.isNullOrEmpty()
        ) {
            ContactsBlock(vacancy, viewModel)
        }

    }
}

@Composable
private fun EmployerNameLogoBlock(vacancy: VacancyDetails) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = LightGray, shape = RoundedCornerShape(dimensionResource(R.dimen._12dp)))
            .padding(all = dimensionResource(R.dimen._16dp))
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
                .padding(start = dimensionResource(R.dimen._8dp))
        ) {
            Text(
                text = vacancy.employerName,
                style = MaterialTheme.typography.medium22,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Black
            )
            Text(
                text = vacancy.address.ifEmpty { vacancy.areaName },
                style = MaterialTheme.typography.regular16,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Black
            )
        }
    }
}

@Composable
private fun KeySkillsBlock(keySkills: String) {
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen._24dp)))
    Text(
        text = stringResource(R.string.skills),
        style = MaterialTheme.typography.medium22,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen._16dp)))
    Text(
        text = keySkills,
        style = MaterialTheme.typography.regular16,
    )
}

private fun keySkillsToString(keySkills: List<String>): String {
    val sb = StringBuilder()

    keySkills.forEach { skill ->
        sb.append("• $skill\n")
    }
    return sb.toString()
}

@Composable
private fun ContactsBlock(vacancy: VacancyDetails, viewModel: VacancyViewModel) {
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen._24dp)))
    Text(
        text = stringResource(R.string.contacts),
        style = MaterialTheme.typography.medium22,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

    if (!vacancy.contactName.isNullOrEmpty()) {
        ContactInfoRender(
            caption = stringResource(R.string.contact),
            text = vacancy.contactName
        )
    }

    if (!vacancy.contactEmail.isNullOrEmpty()) {
        ContactInfoRender(
            caption = stringResource(R.string.email),
            text = vacancy.contactEmail,
            modifier = Modifier.clickable(onClick = { viewModel.openEmail(vacancy.contactEmail) })
        )
    }

    vacancy.contactPhones?.forEach { phone ->
        phone.phone?.let {
            ContactInfoRender(
                caption = stringResource(R.string.phone),
                text = it,
                modifier = Modifier.clickable(onClick = { viewModel.openPhone(it) })
            )
        }
        phone.comment?.let {
            ContactInfoRender(
                caption = stringResource(R.string.comment),
                text = it
            )
        }
    }
}

@Composable
private fun ContactInfoRender(caption: String, text: String, modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen._16dp)))
    Text(
        text = caption,
        style = MaterialTheme.typography.medium16,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen._4dp)))
    Text(
        text = text,
        style = MaterialTheme.typography.regular16,
        modifier = modifier
    )
}

