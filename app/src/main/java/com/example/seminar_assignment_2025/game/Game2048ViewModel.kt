package com.example.seminar_assignment_2025.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

private const val N = 4

data class GameState(
    val board: IntArray = IntArray(N * N) { 0 }, // 0=빈칸, 2,4,8,...
    val score: Int = 0,
    val gameOver: Boolean = false
)

enum class Direction { LEFT, RIGHT, UP, DOWN }

class Game2048ViewModel : ViewModel() {

    private val _state = MutableStateFlow(newGameState())
    val state = _state.asStateFlow()

    // 한 번 되돌리기용
    private var lastState: GameState? = null

    fun newGame() {
        _state.value = newGameState()
        lastState = null
    }

    fun undo() {
        lastState?.let {
            _state.value = it
            lastState = null
        }
    }

    fun swipe(dir: Direction) {
        val before = _state.value
        val (afterBoard, gained, moved) = move(before.board, dir)
        if (!moved) return  // 유효 변화 없으면 무시

        // 유효 이동 -> 새 타일 스폰
        val spawned = spawn(afterBoard)
        val next = before.copy(
            board = spawned,
            score = before.score + gained,
            gameOver = isGameOver(spawned)
        )
        lastState = before
        _state.value = next
    }

    // --- 내부 로직 ---

    private fun newGameState(): GameState {
        val b = IntArray(N * N) { 0 }
        spawn(b)
        spawn(b)
        return GameState(board = b, score = 0, gameOver = false)
    }

    private fun spawn(board: IntArray): IntArray {
        val empty = board.withIndex().filter { it.value == 0 }.map { it.index }
        if (empty.isEmpty()) return board
        val idx = empty.random()
        val value = if (Random.nextDouble() < 0.2) 4 else 2
        board[idx] = value
        return board
    }

    private data class MoveResult(val board: IntArray, val gained: Int, val moved: Boolean)

    private fun move(origin: IntArray, dir: Direction): MoveResult {
        val b = origin.copyOf()
        var gained = 0
        var movedAny = false

        fun lineGet(i: Int, j: Int) = when (dir) {
            Direction.LEFT  -> i * N + j
            Direction.RIGHT -> i * N + (N - 1 - j)
            Direction.UP    -> j * N + i
            Direction.DOWN  -> (N - 1 - j) * N + i
        }

        for (i in 0 until N) {
            // 1) 라인 추출(이동 방향 기준 정렬)
            val line = IntArray(N) { b[lineGet(i, it)] }.filter { it != 0 }.toMutableList()

            // 2) 병합
            val merged = mutableListOf<Int>()
            var j = 0
            while (j < line.size) {
                if (j + 1 < line.size && line[j] == line[j + 1]) {
                    val v = line[j] * 2
                    merged += v
                    gained += v
                    j += 2
                } else {
                    merged += line[j]
                    j += 1
                }
            }
            while (merged.size < N) merged += 0

            // 3) 라인 돌려쓰기
            for (j2 in 0 until N) {
                val idx = lineGet(i, j2)
                if (b[idx] != merged[j2]) {
                    movedAny = true
                    b[idx] = merged[j2]
                }
            }
        }
        return MoveResult(b, gained, movedAny)
    }

    private fun isGameOver(board: IntArray): Boolean {
        if (board.any { it == 0 }) return false
        // 인접 동일 타일 있으면 아직 끝 아님
        fun v(r: Int, c: Int) = board[r * N + c]
        for (r in 0 until N) for (c in 0 until N) {
            val x = v(r, c)
            if (r + 1 < N && v(r + 1, c) == x) return false
            if (c + 1 < N && v(r, c + 1) == x) return false
        }
        return true
    }
}