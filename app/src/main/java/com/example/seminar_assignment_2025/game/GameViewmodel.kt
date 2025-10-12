package com.example.seminar_assignment_2025.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class GameViewmodel : ViewModel() {
    var board by mutableStateOf(Array(4) { IntArray(4) })
        private set
    var score by mutableStateOf(0)
        private set
    private val history = mutableListOf<Pair<Array<IntArray>, Int>>()

    init {
        resetGame()
    }
    fun resetGame() {
        board = Array(4) { IntArray(4) }
        score = 0
    }
}