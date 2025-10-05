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

    private val _board = MutableStateFlow<List<List<Int>>>(emptyList())
    val board: StateFlow<List<List<Int>>> = _board.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    // '뒤로가기'를 위한 이전 상태 저장
    private var previousBoard: List<List<Int>> = emptyList()
    private var previousScore: Int = 0

    // ViewModel이 생성될 때 게임을 초기화
    init {
        resetGame()
    }

    fun resetGame() {
        viewModelScope.launch {
            val initialBoard = List(4) { List(4) { 0 } }
            _board.value = initialBoard
            _score.value = 0
            // 초기 타일 2개 추가
            addNewTile()
            addNewTile()
            // 초기 상태를 뒤로가기 상태로 저장
            previousBoard = _board.value
            previousScore = _score.value
        }
    }

    fun undo() {
        _board.value = previousBoard
        _score.value = previousScore
    }

    fun move(direction: Direction) {
        viewModelScope.launch {
            // 이동 전 상태를 '뒤로가기' 용으로 저장
            previousBoard = _board.value
            previousScore = _score.value

            val currentBoard = _board.value
            var moved = false // 유효한 이동이었는지 판별

            // moveLeft 로직을 기준으로 보드를 회전시켜 다른 방향들을 처리
            val rotatedBoard = when (direction) {
                Direction.LEFT -> currentBoard
                Direction.RIGHT -> currentBoard.map { it.reversed() }
                Direction.UP -> transpose(currentBoard)
                Direction.DOWN -> transpose(currentBoard).map { it.reversed() }
            }

            val (newBoard, scoreChange) = rotatedBoard.map { row ->
                val (newRow, rowMoved) = moveRowLeft(row)
                if (rowMoved) moved = true
                newRow
            }.let { board ->
                val currentScore = board.flatten().sum() // 임시 점수 계산 (실제 합쳐진 점수와 다름)
                board to currentScore // 실제 점수는 moveRowLeft에서 계산
            }

            val finalBoard = when (direction) {
                Direction.LEFT -> newBoard
                Direction.RIGHT -> newBoard.map { it.reversed() }
                Direction.UP -> transpose(newBoard)
                Direction.DOWN -> transpose(newBoard.map { it.reversed() })
            }

            // 보드에 변화가 있었을 경우에만 새 타일 추가
            if (moved) {
                _board.value = finalBoard
                addNewTile()
            }
        }
    }

    // 한 줄을 왼쪽으로 밀고 합치는 로직
    private fun moveRowLeft(row: List<Int>): Pair<List<Int>, Boolean> {
        val filtered = row.filter { it != 0 }
        val newRow = mutableListOf<Int>()
        var i = 0
        var moved = false

        while (i < filtered.size) {
            if (i + 1 < filtered.size && filtered[i] == filtered[i+1]) {
                val mergedValue = filtered[i] * 2
                newRow.add(mergedValue)
                _score.update { it + mergedValue } // 점수 업데이트
                i += 2
                moved = true
            } else {
                newRow.add(filtered[i])
                i += 1
            }
        }

        // 원본과 비교하여 이동 여부 확인
        if (!moved) {
            moved = row != (newRow + List(4 - newRow.size) { 0 })
        }

        // 나머지 공간을 0으로 채움
        while (newRow.size < 4) {
            newRow.add(0)
        }
        return newRow to moved
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
            // 0.8 확률로 2, 0.2 확률로 4
            newBoard[row][col] = if (Random.nextFloat() < 0.8) 2 else 4
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