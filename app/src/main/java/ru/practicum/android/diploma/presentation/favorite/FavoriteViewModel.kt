package ru.practicum.android.diploma.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.presentation.favorite.state.FavoritesState
import javax.inject.Inject

class FavoriteViewModel(private val vacanciesInteractor: VacanciesInteractor) : ViewModel() {

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

    @Suppress("UNCHECKED_CAST")
    class FavoriteViewModelFactory @Inject constructor(
        private val vacanciesInteractor: VacanciesInteractor,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(vacanciesInteractor) as T
        }
    }

}
