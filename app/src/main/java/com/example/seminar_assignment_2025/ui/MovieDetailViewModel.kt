package com.example.seminar_assignment_2025.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seminar_assignment_2025.data.Movie
import com.example.seminar_assignment_2025.data.MovieRepository
import com.example.seminar_assignment_2025.data.MovieRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private val movieRepository: MovieRepository = MovieRepositoryImpl()

    // 1. UI가 관찰할 '영화' 상태
    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie.asStateFlow()

    // 2. UI가 "영화 찾아줘!"라고 호출할 함수
    fun fetchMovieById(id: Int) {
        viewModelScope.launch {
            _movie.value = movieRepository.getMovieById(id)
        }
    }
}