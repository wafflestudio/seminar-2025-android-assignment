// ui/detail/MovieDetailViewModel.kt (새 파일)

package com.example.seminar_assignment_2025.ui.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seminar_assignment_2025.data.repository.MovieDetail
import com.example.seminar_assignment_2025.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel // ⬅️ import 추가
import javax.inject.Inject // ⬅️ import 추가

// Hilt 애노테이션이 없는, 순수한 ViewModel 클래스
@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle // NavHost가 ID를 여기에 넣어줍니다.
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail: StateFlow<MovieDetail?> = _movieDetail.asStateFlow()

    init {
        // ViewModel이 생성될 때 "movieId"를 꺼냅니다.
        val movieId: Int? = savedStateHandle["movieId"]

        if (movieId != null) {
            fetchMovieDetail(movieId)
        } else {
            Log.e("MovieDetailViewModel", "Movie ID is null!")
        }
    }

    private fun fetchMovieDetail(id: Int) {
        viewModelScope.launch {
            try {
                _movieDetail.value = movieRepository.getMovieDetail(id)
            } catch (e: Exception) {
                Log.e("MovieDetailViewModel", "Failed to fetch movie detail", e)
                _movieDetail.value = null
            }
        }
    }
}