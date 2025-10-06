// GameViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

// 사용자의 스와이프 방향을 나타내는 Enum 클래스
enum class Direction { UP, DOWN, LEFT, RIGHT }

class GameViewModel : ViewModel() {

    // 게임 상태(보드와 점수)를 저장하기 위한 데이터 클래스
    private data class GameState(val board: List<List<Int>>, val score: Int)

    private val _board = MutableStateFlow<List<List<Int>>>(emptyList())
    val board: StateFlow<List<List<Int>>> = _board.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    // '뒤로가기'를 위한 이전 상태들을 저장하는 리스트 (스택처럼 사용)
    private val history = mutableListOf<GameState>()

    // ViewModel이 생성될 때 게임을 초기화
    init {
        resetGame()
    }

    fun resetGame() {
        viewModelScope.launch {
            val initialBoard = List(4) { List(4) { 0 } }
            _board.value = initialBoard
            _score.value = 0
            history.clear() // 새 게임 시작 시 히스토리 초기화
            // 초기 타일 2개 추가
            addNewTile()
            addNewTile()
        }
    }

    fun undo() {
        // 히스토리에 저장된 상태가 있을 경우에만 실행
        if (history.isNotEmpty()) {
            // 가장 마지막에 저장된 상태를 가져오고 리스트에서 제거
            val previousState = history.removeLast()
            _board.value = previousState.board
            _score.value = previousState.score
        }
    }

    fun move(direction: Direction) {
        viewModelScope.launch {
            val boardBeforeMove = _board.value
            val scoreBeforeMove = _score.value

            var moved = false // 유효한 이동이었는지 판별
            var scoreChange = 0 // 이번 이동으로 얻은 점수

            // moveLeft 로직을 기준으로 보드를 회전시켜 다른 방향들을 처리
            val rotatedBoard = when (direction) {
                Direction.LEFT -> boardBeforeMove
                Direction.RIGHT -> boardBeforeMove.map { it.reversed() }
                Direction.UP -> transpose(boardBeforeMove)
                Direction.DOWN -> transpose(boardBeforeMove).map { it.reversed() }
            }

            // 각 행을 이동시키고, 이동 여부와 점수 변화를 집계
            val newBoardParts = rotatedBoard.map { row ->
                val (newRow, rowMoved, rowScoreChange) = moveRowLeft(row)
                if (rowMoved) moved = true
                scoreChange += rowScoreChange
                newRow
            }

            // 보드에 변화가 있었을 경우에만 상태 업데이트 및 히스토리 저장
            if (moved) {
                // 이동 전 상태를 히스토리에 추가
                history.add(GameState(board = boardBeforeMove, score = scoreBeforeMove))

                // 회전했던 보드를 다시 원래 방향으로 복구
                val finalBoard = when (direction) {
                    Direction.LEFT -> newBoardParts
                    Direction.RIGHT -> newBoardParts.map { it.reversed() }
                    Direction.UP -> transpose(newBoardParts)
                    Direction.DOWN -> transpose(newBoardParts.map { it.reversed() })
                }

                _board.value = finalBoard
                _score.update { it + scoreChange } // 얻은 점수만큼만 더함
                addNewTile()
            }
        }
    }

    // 한 줄을 왼쪽으로 밀고 합치는 로직
    // Pair 대신 3개의 값을 반환하는 Triple 사용 (새로운 행, 이동 여부, 점수 변화량)
    private fun moveRowLeft(row: List<Int>): Triple<List<Int>, Boolean, Int> {
        val filtered = row.filter { it != 0 }
        val newRow = mutableListOf<Int>()
        var i = 0
        var scoreChange = 0

        while (i < filtered.size) {
            if (i + 1 < filtered.size && filtered[i] == filtered[i+1]) {
                val mergedValue = filtered[i] * 2
                newRow.add(mergedValue)
                scoreChange += mergedValue // 점수 변화량 계산
                i += 2
            } else {
                newRow.add(filtered[i])
                i += 1
            }
        }

        // 나머지 공간을 0으로 채운 최종 행
        val finalRow = (newRow + List(4 - newRow.size) { 0 })

        // 원본과 비교하여 실제로 타일의 위치나 값이 변했는지 확인
        val moved = row != finalRow

        return Triple(finalRow, moved, scoreChange)
    }

    // 보드의 빈 칸에 2 또는 4를 추가
    private fun addNewTile() {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        _board.value.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, cell ->
                if (cell == 0) {
                    emptyCells.add(rowIndex to colIndex)
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val (row, col) = emptyCells.random()
            val newBoard = _board.value.map { it.toMutableList() }.toMutableList()
            // 0.9 확률로 2, 0.1 확률로 4 (원래 게임 규칙에 더 가깝게)
            newBoard[row][col] = if (Random.nextFloat() < 0.9) 2 else 4
            _board.value = newBoard.map { it.toList() }
        }
    }

    // 행과 열을 바꾸는 유틸리티 함수 (UP, DOWN 이동에 사용)
    private fun transpose(board: List<List<Int>>): List<List<Int>> {
        return List(4) { colIndex ->
            List(4) { rowIndex ->
                board[rowIndex][colIndex]
            }
        }
    }
}