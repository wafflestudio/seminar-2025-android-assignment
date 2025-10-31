package com.example.seminar_assignment_2025.data

import kotlinx.serialization.Serializable

// @Serializable 어노테이션은 Json 변환에 꼭 필요합니다. 넵.
@Serializable
data class Movie(
    val adult: Boolean,
    val backdrop_path: String?, // null값이 대체 왜 있는거져??
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)