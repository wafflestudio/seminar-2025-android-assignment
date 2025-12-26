package com.example.seminar_assignment_2025.views.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seminar_assignment_2025.domainmodel.Movie
import com.example.seminar_assignment_2025.data.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<Movie?>(null)
    val movieDetail: StateFlow<Movie?> = _movieDetail.asStateFlow()

    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            val detail = withContext(Dispatchers.IO) {
                movieRepository.getMovieDetail(movieId)
            }
            _movieDetail.value = detail
        }
    }

    fun getGenreName(id: Int): String {
        return movieRepository.getGenreName(id)
    }
}