package com.example.seminar_assignment_2025.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.seminar_assignment_2025.data.Movie
import com.example.seminar_assignment_2025.data.MovieRepository
import com.example.seminar_assignment_2025.data.MovieRepositoryImpl
import com.example.seminar_assignment_2025.data.RecentSearchRepository
import com.example.seminar_assignment_2025.data.RecentSearchRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val recentSearchrepository: RecentSearchRepository =
        RecentSearchRepositoryImpl(application.applicationContext)

    private val movieRepository: MovieRepository = MovieRepositoryImpl()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val recentSearches: StateFlow<List<String>> = recentSearchrepository.recentSearchesFlow
        .stateIn(
            scope = viewModelScope, // ViewModel이 살아있는 동안만
            started = SharingStarted.WhileSubscribed(5000), // 5초간 앱이 꺼지면 멈춤
            initialValue = emptyList() // 처음 값은 빈 리스트
        )

    val searchResults: StateFlow<List<Movie>> = searchQuery
        // 바뀔때마다 이 'map' 블록이 실행
        .map { query ->
            // 'MovieRepository'에 검색을 명령
            movieRepository.searchByTitle(query) // -> 검색된 List<Movie>가 반환됨
        }
        .stateIn( // 이 '변환된 Flow'를 UI가 쓸 수 있게 StateFlow로 만듭니다.
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // UI가 "글자 바뀌었어!"라고 호출할 함수
    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    /** UI가 '검색'을 실행할 때 호출 (검색어 저장) */
    fun saveSearchQuery(query: String) {
        // 빈 값은 저장하지 않음
        if (query.isBlank()) return

        viewModelScope.launch {
            recentSearchrepository.addSearchQuery(query)
        }
    }

    /** UI가 '개별 삭제(X)' 버튼을 누를 때 호출 */
    fun deleteSearch(query: String) {
        viewModelScope.launch {
            recentSearchrepository.deleteSearchQuery(query)
        }
    }

    /** UI가 '전체 삭제' 버튼을 누를 때 호출 */
    fun clearAllSearches() {
        viewModelScope.launch {
            recentSearchrepository.clearAllSearchQueries()
        }
    }

}