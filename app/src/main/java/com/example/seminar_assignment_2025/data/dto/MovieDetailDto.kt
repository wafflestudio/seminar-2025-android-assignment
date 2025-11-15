package com.example.seminar_assignment_2025.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailDto(
    val id: Int,
    val title: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    val overview: String?,

    // ⭐️ 중요: 상세 API는 ID가 아닌, 장르 객체 목록(List<GenreDto>)을 줍니다.
    val genres: List<GenreDto>,

    val runtime: Int?,
    val tagline: String?,

    @SerialName("adult") val adult: Boolean = false
)