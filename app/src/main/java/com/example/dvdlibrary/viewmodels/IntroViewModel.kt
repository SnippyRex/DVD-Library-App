package com.example.dvdlibrary.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dvdlibrary.data.Film
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow

class IntroViewModel(private val films: Flow<List<Film>>) : ViewModel() {

    var currentSortItemState = MutableStateFlow(0)
    var sortOrder = MutableStateFlow(0)

    val sortedFilms: Flow<List<Film>> =
        combine(films, currentSortItemState, sortOrder) { films, currentSortItemState, sortOrder ->
            when (currentSortItemState) {
                0 -> if (sortOrder == 0) films.sortedBy { it.title.removePrefix("The ") } else films.sortedByDescending {
                    it.title.removePrefix(
                        "The "
                    )
                }

                1 -> if (sortOrder == 0) films.sortedWith(
                    compareBy({ it.genre1 },
                        { it.genre2 })
                ) else films.sortedWith(
                    compareBy<Film>({ it.genre1 },
                        { it.genre2 }).reversed()
                )

                2 -> if (sortOrder == 0) films.sortedBy { it.year } else films.sortedByDescending { it.year }
                3 -> if (sortOrder == 0) films.sortedBy { it.runtime } else films.sortedByDescending { it.runtime }
                else -> films.sortedBy { it.title }
            }
        }
}

class IntroViewModelFactory(private val films: Flow<List<Film>>) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IntroViewModel(films = films) as T
    }

}