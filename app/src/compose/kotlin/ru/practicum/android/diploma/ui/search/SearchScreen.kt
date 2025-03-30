package ru.practicum.android.diploma.ui.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.search.state.SearchFragmentState
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.White
import ru.practicum.android.diploma.ui.theme.medium22
import ru.practicum.android.diploma.ui.theme.regular16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {

    Log.d("MYLOG", "id=$viewModel")
    val state by viewModel.searchFragmentScreenState.collectAsState()
    Log.d("MYLOG", "id=$state")

    var searchText by rememberSaveable { mutableStateOf("") }

    val icClose = @Composable {
        IconButton(
            onClick = {
                searchText = ""
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Icon"
            )
        }
    }

    val icSearch = @Composable {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "Icon"
        )
    }

    Column {
        TopAppBar(
            expandedHeight = dimensionResource(R.dimen._64dp),
            title = {
                Text(
                    text = stringResource(R.string.search_vacancy),
                    style = MaterialTheme.typography.medium22
                )
            },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen._16dp),
                    vertical = dimensionResource(R.dimen._8dp)
                )
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensionResource(R.dimen._12dp)),
                value = searchText,
                singleLine = true,
                placeholder = { Text(stringResource(R.string.search)) },
                onValueChange = {
                    searchText = it
                    if (searchText.isNotBlank())
                        viewModel.search(text = searchText, withDelay = true)
                    else viewModel.cancelSearch()
                },
                trailingIcon = if (searchText.isNotBlank()) icClose else icSearch,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }
        Render(state)
    }
}

@Composable
fun Render(state: SearchFragmentState) {
    when (state) {
        is SearchFragmentState.Start -> ShowStart()
        is SearchFragmentState.Loading -> ShowLoading(state.isFirstPage)
        is SearchFragmentState.Content -> ShowContent(state.vacancies, state.found)
        is SearchFragmentState.Empty -> ShowEmpty()
        is SearchFragmentState.Error -> ShowError(state.isFirstPage)
        is SearchFragmentState.NoInternet -> ShowNoInternet(state.isFirstPage)
    }
}

@Composable
private fun ShowStart() {
    RenderPlaceholder(textId = null, paintId = R.drawable.png_search)
}

@Composable
private fun ShowLoading(isFirstPage: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }

//    hideKeyBoard()
//    binding.placeHolderError.visibility = View.GONE
//    binding.ImageSearch.visibility = View.GONE
//    if (isFirstPage) {
//        binding.recyclerViewFoundVacancies.visibility = View.GONE
//        binding.messageFound.visibility = View.GONE
//        binding.progressBarMain.visibility = View.VISIBLE
//    } else {
//        binding.progressBarPaging.visibility = View.VISIBLE
//    }
}

@Composable
private fun ShowError(isFirstPage: Boolean) {
    if (isFirstPage) {
        RenderPlaceholder(textId = R.string.error_server, paintId = R.drawable.error_server)
    } else {
//        onSnack(resources.getString(R.string.error_occurred))
//        viewModel.getContent()
    }

//    hideViews()
//    if (isFirstPage) {
//        binding.recyclerViewFoundVacancies.visibility = View.GONE
//        binding.placeHolderError.visibility = View.VISIBLE
//        binding.messageError.text = getString(R.string.error_server)
//        binding.imageError.setImageResource(R.drawable.error_server)
//    } else {
//        onSnack(resources.getString(R.string.error_occurred))
//        viewModel.getContent()
//    }
}

@Composable
private fun ShowNoInternet(isFirstPage: Boolean) {
    if (isFirstPage) {
        RenderPlaceholder(
            textId = R.string.no_internet,
            paintId = R.drawable.png_no_internet
        )
    } else {
//        onSnack(resources.getString(R.string.check_internet))
//        viewModel.getContent()
    }

//    hideViews()
//    if (isFirstPage) {
//        binding.recyclerViewFoundVacancies.visibility = View.GONE
//        binding.placeHolderError.visibility = View.VISIBLE
//        binding.messageError.text = getString(R.string.no_internet)
//        binding.imageError.setImageResource(R.drawable.png_no_internet)
//    } else {
//        onSnack(resources.getString(R.string.check_internet))
//        viewModel.getContent()
//    }
}

@Composable
fun RenderVacancy(item: Vacancy) {
//    Box(modifier = Modifier.padding(top = 4.dp)) {
//        Card {
//            Column(modifier = Modifier.padding(4.dp)) {
//                Text(text = item.title, fontWeight = FontWeight.Bold)
//                Text(text = item.details)
//            }
//        }
//    }
}

@Composable
private fun ShowEmpty() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen._16dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.nothing_found),
            style = MaterialTheme.typography.regular16,
            maxLines = 1,
            color = White,
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen._4dp))
                .background(color = Blue, shape = RoundedCornerShape(dimensionResource(R.dimen._12dp)))
                .padding(
                    horizontal = dimensionResource(R.dimen._12dp),
                    vertical = dimensionResource(R.dimen._4dp)
                )
        )
        RenderPlaceholder(
            textId = R.string.nothing_found_description,
            paintId = R.drawable.png_nothing_found
        )
    }
}

@Composable
fun RenderPlaceholder(textId: Int?, paintId: Int) {
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

@Composable
private fun ShowContent(vacancies: List<Vacancy>, found: Int) {
//    hideKeyBoard()
//    binding.progressBarMain.visibility = View.GONE
//    binding.progressBarPaging.visibility = View.GONE
//    binding.ImageSearch.visibility = View.GONE
//    binding.placeHolderError.visibility = View.GONE
//    binding.messageFound.text =
//        resources.getQuantityString(R.plurals.vacancy_plurals, found, found)
//    binding.messageFound.visibility = View.VISIBLE
//    adapter.setVacancyList(vacancies)
//    binding.recyclerViewFoundVacancies.visibility = View.VISIBLE
}

//
//@Preview
//@Composable
//fun AboutScreenPreview() {
//    ComposeTheme {
//        SearchScreen()
//    }
//}
