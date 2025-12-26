package com.example.seminar_assignment_2025.data.search

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history")

@Singleton
class SearchHistoryRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : SearchHistoryRepository {

    private val dataStore = context.dataStore

    private companion object {
        val RECENT_SEARCHES_KEY = stringPreferencesKey("recent_searches")
    }

    override val searchHistory: Flow<List<String>> = dataStore.data
        .catch { exception ->
            // Handle dataStore read errors
            emit(androidx.datastore.preferences.core.emptyPreferences())
        }
        .map { preferences ->
            val jsonString = preferences[RECENT_SEARCHES_KEY] ?: "[]"
            try {
                Json.decodeFromString<List<String>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }

    override suspend fun addSearchTerm(term: String) {
        dataStore.edit { preferences ->
            val currentSearchesJson = preferences[RECENT_SEARCHES_KEY] ?: "[]"
            val currentSearches = try {
                Json.decodeFromString<MutableList<String>>(currentSearchesJson)
            } catch (e: Exception) {
                mutableListOf()
            }

            currentSearches.remove(term)
            currentSearches.add(0, term)

            val updatedSearches = currentSearches.take(10)

            preferences[RECENT_SEARCHES_KEY] = Json.encodeToString(updatedSearches)
        }
    }

    override suspend fun removeSearchTerm(term: String) {
        dataStore.edit { preferences ->
            val currentSearchesJson = preferences[RECENT_SEARCHES_KEY] ?: "[]"
            val currentSearches = try {
                Json.decodeFromString<MutableList<String>>(currentSearchesJson)
            } catch (e: Exception) {
                mutableListOf()
            }
            currentSearches.remove(term)
            preferences[RECENT_SEARCHES_KEY] = Json.encodeToString(currentSearches)
        }
    }

    override suspend fun clearSearchHistory() {
        dataStore.edit { preferences ->
            preferences[RECENT_SEARCHES_KEY] = "[]"
        }
    }
}