package com.example.seminar_assignment_2025.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchMovieResponse(
    val results: List<MovieListItemDto>
)

@Serializable
data class MovieListItemDto(
    val id: Int,
    val title: String,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerialName("backdrop_path") val backdropPath: String? = null,
    val overview: String? = null,
    val popularity: Double = 0.0,
    val adult: Boolean = false
)

@Serializable
data class MovieDetailDto(
    val id: Int,
    val title: String,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("poster_path") val posterPath: String? = null,
    val genres: List<GenreDto> = emptyList(),
    @SerialName("backdrop_path") val backdropPath: String? = null,
    val overview: String? = null,
    val popularity: Double = 0.0,
    val adult: Boolean = false,
    val runtime: Int? = null,
    @SerialName("original_title") val originalTitle: String? = null,
    val status: String? = null,
    val budget: Long? = null,
    val revenue: Long? = null
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
)
