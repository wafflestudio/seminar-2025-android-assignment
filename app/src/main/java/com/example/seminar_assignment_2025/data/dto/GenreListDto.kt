package com.example.seminar_assignment_2025.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenreListDto(
    val genres: List<GenreDto>
)

