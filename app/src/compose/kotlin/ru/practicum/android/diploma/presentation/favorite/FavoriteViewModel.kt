package ru.practicum.android.diploma.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.presentation.favorite.state.FavoritesState
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val vacanciesInteractor: VacanciesInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val state: MutableStateFlow<FavoritesState> = _state

    init {
        getFavoriteVacancies()
    }

    private fun getFavoriteVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            vacanciesInteractor.getFavoriteVacancies()
                .onEach { vacancies ->
                    if (vacancies.isEmpty()) {
                        _state.update { FavoritesState.Empty }
                    } else {
                        _state.update { FavoritesState.Content(vacancies) }
                    }
                }
                .catch { _state.update { FavoritesState.Error } }
                .collect { }
        }
    }

}
