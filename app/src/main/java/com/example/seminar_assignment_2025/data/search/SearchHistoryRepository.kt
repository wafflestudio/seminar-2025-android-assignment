package com.example.seminar_assignment_2025.data.search

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    val searchHistory: Flow<List<String>>
    suspend fun addSearchTerm(term: String)
    suspend fun removeSearchTerm(term: String)
    suspend fun clearSearchHistory()
}
