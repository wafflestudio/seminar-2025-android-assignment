package com.example.seminar_assignment_2025.domainmodel

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val voteAverage: Double,
    val posterPath: String,
    val genreIds: List<Int>,
    val backdropPath: String,
    val overview: String,
    val popularity: Double,
    val adult: Boolean,
    val runtime: Int?,
    val originalTitle: String,
    val status: String?,
    val budget: Long?,
    val revenue: Long?
)