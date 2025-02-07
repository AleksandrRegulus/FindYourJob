package ru.practicum.android.diploma.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.presentation.favorite.state.FavoritesState
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val vacanciesInteractor: VacanciesInteractor
) : ViewModel() {

    private val _favoritesStateLiveData = MutableLiveData<FavoritesState>()
    val favoritesStateLiveData: LiveData<FavoritesState> get() = _favoritesStateLiveData

    init {
        getFavoriteVacancies()
    }

    private fun getFavoriteVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            vacanciesInteractor.getFavoriteVacancies()
                .onEach {
                    if (it.isEmpty()) {
                        _favoritesStateLiveData.postValue(FavoritesState.Empty)
                    } else {
                        _favoritesStateLiveData.postValue(FavoritesState.Content(it))
                    }
                }
                .catch { _favoritesStateLiveData.postValue(FavoritesState.Error) }
                .collect { }
        }
    }

}
