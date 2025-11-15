package com.example.seminar_assignment_2025.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("genre_ids") val genreIds: List<Int>,
    @SerialName("backdrop_path") val backdropPath: String?,
    val overview: String?,
    val popularity: Double
)