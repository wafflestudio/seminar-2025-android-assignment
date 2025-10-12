import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class Direction { UP, DOWN, LEFT, RIGHT }

class GameViewModel : ViewModel() {

    private data class GameState(val board: List<List<Int>>, val score: Int)

    private val _board = MutableStateFlow<List<List<Int>>>(emptyList())
    val board: StateFlow<List<List<Int>>> = _board.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val history = mutableListOf<GameState>()

    init {
        resetGame()
    }

    fun resetGame() {
        viewModelScope.launch {
            val initialBoard = List(4) { List(4) { 0 } }
            _board.value = initialBoard
            _score.value = 0
            history.clear()
            addNewTile()
            addNewTile()
        }
    }

    fun undo() {
        if (history.isNotEmpty()) {
            val previousState = history.removeLast()
            _board.value = previousState.board
            _score.value = previousState.score
        }
    }

    fun move(direction: Direction) {
        viewModelScope.launch {
            val boardBeforeMove = _board.value
            val scoreBeforeMove = _score.value

            var moved = false
            var scoreChange = 0

            val rotatedBoard = when (direction) {
                Direction.LEFT -> boardBeforeMove
                Direction.RIGHT -> boardBeforeMove.map { it.reversed() }
                Direction.UP -> transpose(boardBeforeMove)
                Direction.DOWN -> transpose(boardBeforeMove).map { it.reversed() }
            }

            val newBoardParts = rotatedBoard.map { row ->
                val (newRow, rowMoved, rowScoreChange) = moveRowLeft(row)
                if (rowMoved) moved = true
                scoreChange += rowScoreChange
                newRow
            }

            if (moved) {
                history.add(GameState(board = boardBeforeMove, score = scoreBeforeMove))

                val finalBoard = when (direction) {
                    Direction.LEFT -> newBoardParts
                    Direction.RIGHT -> newBoardParts.map { it.reversed() }
                    Direction.UP -> transpose(newBoardParts)
                    Direction.DOWN -> transpose(newBoardParts.map { it.reversed() })
                }

                _board.value = finalBoard
                _score.update { it + scoreChange }
                addNewTile()
            }
        }
    }

    private fun moveRowLeft(row: List<Int>): Triple<List<Int>, Boolean, Int> {
        val filtered = row.filter { it != 0 }
        val newRow = mutableListOf<Int>()
        var i = 0
        var scoreChange = 0

        while (i < filtered.size) {
            if (i + 1 < filtered.size && filtered[i] == filtered[i+1]) {
                val mergedValue = filtered[i] * 2
                newRow.add(mergedValue)
                scoreChange += mergedValue
                i += 2
            } else {
                newRow.add(filtered[i])
                i += 1
            }
        }

        val finalRow = (newRow + List(4 - newRow.size) { 0 })

        val moved = row != finalRow

        return Triple(finalRow, moved, scoreChange)
    }


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
            newBoard[row][col] = if (Random.nextFloat() < 0.9) 2 else 4
            _board.value = newBoard.map { it.toList() }
        }
    }

    private fun transpose(board: List<List<Int>>): List<List<Int>> {
        return List(4) { colIndex ->
            List(4) { rowIndex ->
                board[rowIndex][colIndex]
            }
        }
    }
}