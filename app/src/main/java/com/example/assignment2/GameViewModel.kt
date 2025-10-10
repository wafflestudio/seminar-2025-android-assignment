package com.example.assignment2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {
    // 게임 보드 상태 (UI가 관찰)
    private val _board = MutableStateFlow<List<List<Int>>>(emptyList())
    val board: StateFlow<List<List<Int>>> = _board.asStateFlow()

    // 점수 상태
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    // 게임 오버 상태
    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver.asStateFlow()

    // 뒤로가기를 위한 이전 상태 저장
    private var previousBoard: List<List<Int>> = emptyList()
    private var previousScore: Int = 0

    // 보드 사이즈
    private val boardSize = 4

    init {
        resetGame()
    }

    // 게임 리셋
    fun resetGame() {
        viewModelScope.launch {
            val newBoard = List(boardSize) { List(boardSize) { 0 } }.map{it.toMutableList()}.toMutableList()
            _score.value = 0
            _isGameOver.value = false
            addRandomTile(newBoard)
            addRandomTile(newBoard)
            _board.value = newBoard
        }
    }

    // 뒤로가기
    fun undoMove() {
        if (previousBoard.isNotEmpty()) {
            _board.value = previousBoard
            _score.value = previousScore
        }
    }

    // 스와이프 처리
    fun onSwipe(direction: Direction) {
        if (_isGameOver.value) return

        viewModelScope.launch {
            // 이전 상태 저장
            previousBoard = _board.value.map { it.toList() }
            previousScore = _score.value

            val currentBoard = _board.value.map { it.toMutableList() }.toMutableList()
            var moved = false
            var scoreGained = 0

            // 스와이프 방향에 따라 보드 변환 및 로직 처리
            when (direction) {
                Direction.LEFT -> {
                    for (i in 0 until boardSize) {
                        val (newLine, newMoved, newScore) = processLine(currentBoard[i])
                        if (newMoved) moved = true
                        scoreGained += newScore
                        currentBoard[i] = newLine.toMutableList()
                    }
                }
                Direction.RIGHT -> {
                    for (i in 0 until boardSize) {
                        val reversedLine = currentBoard[i].reversed()
                        val (processedLine, newMoved, newScore) = processLine(reversedLine)
                        if (newMoved) moved = true
                        scoreGained += newScore
                        currentBoard[i] = processedLine.reversed().toMutableList()
                    }
                }
                Direction.UP -> {
                    val transposed = transpose(currentBoard)
                    for (i in 0 until boardSize) {
                        val (newLine, newMoved, newScore) = processLine(transposed[i])
                        if (newMoved) moved = true
                        scoreGained += newScore
                        transposed[i] = newLine.toMutableList()
                    }
                    _board.value = transpose(transposed)
                }
                Direction.DOWN -> {
                    val transposed = transpose(currentBoard)
                    for (i in 0 until boardSize) {
                        val reversedLine = transposed[i].reversed()
                        val (processedLine, newMoved, newScore) = processLine(reversedLine)
                        if (newMoved) moved = true
                        scoreGained += newScore
                        transposed[i] = processedLine.reversed().toMutableList()
                    }
                    _board.value = transpose(transposed)
                }
            }

            // 보드에 변화가 있었을 경우
            if (moved) {
                _score.value += scoreGained
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    // UP/DOWN의 경우 이미 board가 업데이트 되었으므로 복사본을 가져와야 함
                    val boardAfterMove = _board.value.map { it.toMutableList() }.toMutableList()
                    addRandomTile(boardAfterMove)
                    _board.value = boardAfterMove
                } else {
                    addRandomTile(currentBoard)
                    _board.value = currentBoard
                }


                if (checkGameOver(_board.value)) {
                    _isGameOver.value = true
                }
            }
        }
    }


    // 한 줄(Row 또는 Column)을 왼쪽으로 밀고 합치는 로직
    private fun processLine(line: List<Int>): Triple<List<Int>, Boolean, Int> {
        val oldLine = line.toList()
        val newLine = line.filter { it != 0 }.toMutableList()
        var score = 0
        var i = 0
        while (i < newLine.size - 1) {
            if (newLine[i] == newLine[i + 1]) {
                val mergedValue = newLine[i] * 2
                newLine[i] = mergedValue
                score += mergedValue
                newLine.removeAt(i + 1)
            }
            i++
        }

        val result = (newLine + List(boardSize - newLine.size) { 0 })
        val moved = result != oldLine
        return Triple(result, moved, score)
    }


    // 비어있는 칸에 2 또는 4를 추가
    private fun addRandomTile(board: MutableList<MutableList<Int>>) {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        board.forEachIndexed { r, row ->
            row.forEachIndexed { c, value ->
                if (value == 0) emptyCells.add(r to c)
            }
        }
        if (emptyCells.isNotEmpty()) {
            val (r, c) = emptyCells.random()
            board[r][c] = if (Random.nextFloat() < 0.8f) 2 else 4
        }
    }

    // 보드 행/열 변환 (UP/DOWN 스와이프 시 사용)
    private fun transpose(board: List<List<Int>>): MutableList<MutableList<Int>> {
        val newBoard = List(boardSize) { MutableList(boardSize) { 0 } }.toMutableList()
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                newBoard[j][i] = board[i][j]
            }
        }
        return newBoard
    }

    // 게임 오버 조건 확인
    private fun checkGameOver(board: List<List<Int>>): Boolean {
        // 1. 빈 칸이 있는지 확인
        if (board.any { row -> row.contains(0) }) return false

        // 2. 가로로 합쳐질 수 있는 타일이 있는지 확인
        for (r in 0 until boardSize) {
            for (c in 0 until boardSize - 1) {
                if (board[r][c] == board[r][c + 1]) return false
            }
        }

        // 3. 세로로 합쳐질 수 있는 타일이 있는지 확인
        for (c in 0 until boardSize) {
            for (r in 0 until boardSize - 1) {
                if (board[r][c] == board[r + 1][c]) return false
            }
        }
        return true
    }
}