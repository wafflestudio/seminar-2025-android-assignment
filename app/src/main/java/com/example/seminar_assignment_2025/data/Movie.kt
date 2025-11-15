package com.example.seminar_assignment_2025.data

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val voteAverage: Double,
    val posterPath: String,
    val genres: List<String>,
    val backdropPath: String,
    val overview: String,
    val popularity: Double,
)
