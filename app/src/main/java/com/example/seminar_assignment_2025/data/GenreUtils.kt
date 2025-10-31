package com.example.seminar_assignment_2025.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Genre(
    val id: Int,
    val name: String
)

@Serializable
data class GenreResponse(
    val genres: List<Genre>
)

val genreJsonData = """
    {
      "genres": [
        {
          "id": 28,
          "name": "Action"
        },
        {
          "id": 12,
          "name": "Adventure"
        },
        {
          "id": 16,
          "name": "Animation"
        },
        {
          "id": 35,
          "name": "Comedy"
        },
        {
          "id": 80,
          "name": "Crime"
        },
        {
          "id": 99,
          "name": "Documentary"
        },
        {
          "id": 18,
          "name": "Drama"
        },
        {
          "id": 10751,
          "name": "Family"
        },
        {
          "id": 14,
          "name": "Fantasy"
        },
        {
          "id": 36,
          "name": "History"
        },
        {
          "id": 27,
          "name": "Horror"
        },
        {
          "id": 10402,
          "name": "Music"
        },
        {
          "id": 9648,
          "name": "Mystery"
        },
        {
          "id": 10749,
          "name": "Romance"
        },
        {
          "id": 878,
          "name": "Science Fiction"
        },
        {
          "id": 10770,
          "name": "TV Movie"
        },
        {
          "id": 53,
          "name": "Thriller"
        },
        {
          "id": 10752,
          "name": "War"
        },
        {
          "id": 37,
          "name": "Western"
        }
      ]
    }
""".trimIndent()

val genreResponse = Json { ignoreUnknownKeys = true }
    .decodeFromString<GenreResponse>(genreJsonData)

val genreMap: Map<Int, String> = genreResponse.genres.associateBy(
    keySelector = { it.id },        // Key는 'id' (예: 28)
    valueTransform = { it.name }    // Value는 'name' (예: "Action")
)

// 장르 ID 리스트를 -> "SF, 드라마" 형태의 문자열로 변환
fun formatGenres(genreIds: List<Int>): String {
    return genreIds.mapNotNull { genreMap[it] } // genreMap에서 ID로 이름을 찾음
        .take(2) // 최대 2개만
        .joinToString(", ") // "SF, 드라마"로 합치기
}
