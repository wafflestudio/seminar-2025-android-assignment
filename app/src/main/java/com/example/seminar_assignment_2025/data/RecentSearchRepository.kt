package com.example.seminar_assignment_2025.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "recent_searches")

interface RecentSearchRepository {
    val recentSearchesFlow: Flow<List<String>>

    /** 새 검색어 목록에 추가 (중복 시 맨 위로) */
    suspend fun addSearchQuery(query: String)

    /** 특정 검색어를 목록에서 삭제 */
    suspend fun deleteSearchQuery(query: String)

    /** 모든 최근 검색어를 삭제 */
    suspend fun clearAllSearchQueries()
}

class RecentSearchRepositoryImpl(
    private val context: Context
) : RecentSearchRepository {

    companion object {
        // DataStore에서 사용할 '키(Key)'
        private val RECENT_LIST_KEY = stringPreferencesKey("recent_list")
    }

    /** [읽기] 기능 구현 */
    override val recentSearchesFlow: Flow<List<String>> =
        context.dataStore.data.map { preferences ->
            preferences[RECENT_LIST_KEY]?.let { json ->
                Json.decodeFromString<List<String>>(json)
            } ?: emptyList()
        }

    /** [추가] 기능 구현 */
    override suspend fun addSearchQuery(query: String) {
        context.dataStore.edit { preferences ->
            val currentList = getCurrentList(preferences)
            currentList.remove(query) // 중복 제거
            currentList.add(0, query) // 0번 인덱스(맨 위)에 추가

            preferences[RECENT_LIST_KEY] = Json.encodeToString(currentList) // <-- finalList 대신 currentList를 바로 저장
        }
    }

    /** [개별 삭제] 기능 구현 */
    override suspend fun deleteSearchQuery(query: String) {
        context.dataStore.edit { preferences ->
            val currentList = getCurrentList(preferences)
            currentList.remove(query) // 해당 검색어만 삭제
            preferences[RECENT_LIST_KEY] = Json.encodeToString(currentList)
        }
    }

    /** [전체 삭제] 기능 구현 */
    override suspend fun clearAllSearchQueries() {
        context.dataStore.edit { preferences ->
            preferences.remove(RECENT_LIST_KEY) // 키 자체를 삭제
        }
    }

    private fun getCurrentList(preferences: Preferences): MutableList<String> {
        return preferences[RECENT_LIST_KEY]?.let { json ->
            Json.decodeFromString<List<String>>(json).toMutableList()
        } ?: mutableListOf()
    }
}
