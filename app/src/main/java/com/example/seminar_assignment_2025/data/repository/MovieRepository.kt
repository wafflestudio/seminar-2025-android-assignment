package com.example.seminar_assignment_2025.data.repository

import com.example.seminar_assignment_2025.data.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface MovieRepository {
    suspend fun searchByTitle(query: String): List<Movie>
    suspend fun getGenreName(id: Int): String
    suspend fun getMovieDetail(id: Int): MovieDetail

}

data class MovieDetail(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val voteAverage: Double,
    val posterPath: String,
    val backdropPath: String,
    val overview: String,
    val genres: List<String>, // ⭐️ DTO와 달리 장르 '이름' 리스트
    val runtime: Int,
    val tagline: String,
    val adult: Boolean
)
