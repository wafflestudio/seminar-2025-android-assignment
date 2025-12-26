package com.example.seminar_assignment_2025.data.movie

import com.example.seminar_assignment_2025.domainmodel.Movie

interface MovieRepository {
    suspend fun searchByTitle(query: String): List<Movie>
    suspend fun getMovieDetail(movieId: Int): Movie
    fun getGenreName(id: Int): String
}
