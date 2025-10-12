package com.example.seminar_assignment_2025

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import kotlin.math.abs
import kotlin.random.Random

class GameFragment : Fragment() {

    private val board = Array(4) { IntArray(4) }
    private val prevBoards = mutableListOf<Array<IntArray>>()
    private lateinit var gridLayout: GridLayout
    private lateinit var scoreText: TextView
    private val tiles = Array(4) { arrayOfNulls<TextView>(4) }
    private var score = 0

    private var startX = 0f
    private var startY = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        gridLayout = view.findViewById(R.id.game_grid)
        scoreText = view.findViewById(R.id.score_text)

        createBoardUI()

        view.findViewById<Button>(R.id.btn_back).setOnClickListener { undoMove() }
        view.findViewById<Button>(R.id.btn_restart).setOnClickListener { resetGame() }

        initGame()
        setupSwipeListener(view)
        return view
    }

    /** --------------------- 초기화 ---------------------- **/
    private fun createBoardUI() {
        gridLayout.post {
            val size = minOf(gridLayout.width, gridLayout.height)
            gridLayout.layoutParams.width = size
            gridLayout.layoutParams.height = size
            gridLayout.requestLayout()
        }

        gridLayout.removeAllViews()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val tile = TextView(requireContext()).apply {
                    textSize = 24f
                    gravity = Gravity.CENTER
                    setBackgroundColor(0xFFCDC1B4.toInt())
                    setTextColor(0xFF776E65.toInt())
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        columnSpec = GridLayout.spec(j, 1f)
                        rowSpec = GridLayout.spec(i, 1f)
                        setMargins(8, 8, 8, 8)
                    }
                }
                tiles[i][j] = tile
                gridLayout.addView(tile)
            }
        }
    }

    private fun initGame() {
        prevBoards.clear()
        score = 0
        updateScore()
        for (i in 0..3) for (j in 0..3) board[i][j] = 0
        addRandomTile()
        addRandomTile()
        updateUI()
    }

    private fun resetGame() = initGame()

    private fun copyBoard(src: Array<IntArray>): Array<IntArray> =
        Array(4) { i -> src[i].clone() }

    /** --------------------- 이동 제어 ---------------------- **/
    @SuppressLint("ClickableViewAccessibility")
    private fun setupSwipeListener(view: View) {
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                }
                MotionEvent.ACTION_UP -> {
                    val dx = event.x - startX
                    val dy = event.y - startY
                    if (abs(dx) > abs(dy)) {
                        if (dx > 100) moveRight()
                        else if (dx < -100) moveLeft()
                    } else {
                        if (dy > 100) moveDown()
                        else if (dy < -100) moveUp()
                    }
                }
            }
            true
        }
    }

    private fun moveLeft() = moveAndAdd { i -> board[i] = merge(board[i]) }
    private fun moveRight() = moveAndAdd { i -> board[i] = merge(board[i].reversedArray()).reversedArray() }
    private fun moveUp() = moveAndAdd { j ->
        val col = IntArray(4) { board[it][j] }
        val merged = merge(col)
        for (i in 0..3) board[i][j] = merged[i]
    }
    private fun moveDown() = moveAndAdd { j ->
        val col = IntArray(4) { board[it][j] }
        val merged = merge(col.reversedArray()).reversedArray()
        for (i in 0..3) board[i][j] = merged[i]
    }

    /** --------------------- 이동 + 검사 ---------------------- **/
    private fun moveAndAdd(action: (Int) -> Unit) {
        val before = board.flatMap { it.asList() }
        val boardCopy = copyBoard(board)
        for (i in 0..3) action(i)
        val after = board.flatMap { it.asList() }

        if (after != before) {
            prevBoards.add(boardCopy)
            addRandomTile()
            updateUI()
            checkGameOver() // ✅ 이동 후 검사
        } else {
            // 🔥 이동이 없고 꽉 찼으며 합칠 수 없으면 종료
            if (isBoardFull() && !canMerge()) {
                showGameOverDialog()
            }
        }
    }

    private fun merge(arr: IntArray): IntArray {
        val list = arr.filter { it != 0 }.toMutableList()
        val newList = mutableListOf<Int>()
        var skip = false
        for (i in list.indices) {
            if (skip) {
                skip = false
                continue
            }
            if (i < list.size - 1 && list[i] == list[i + 1]) {
                val newValue = list[i] * 2
                newList.add(newValue)
                score += newValue
                skip = true
            } else newList.add(list[i])
        }
        while (newList.size < 4) newList.add(0)
        updateScore()
        return newList.toIntArray()
    }

    /** --------------------- 타일 추가 / UI ---------------------- **/
    private fun addRandomTile() {
        val empty = mutableListOf<Pair<Int, Int>>()
        for (i in 0..3) for (j in 0..3)
            if (board[i][j] == 0) empty.add(i to j)
        if (empty.isNotEmpty()) {
            val (x, y) = empty.random()
            board[x][y] = if (Random.nextFloat() < 0.2f) 4 else 2
        }
    }

    private fun updateUI() {
        for (i in 0..3) {
            for (j in 0..3) {
                val value = board[i][j]
                val tile = tiles[i][j]
                tile?.text = if (value == 0) "" else value.toString()
                tile?.setBackgroundColor(getTileColor(value))
            }
        }
    }

    private fun updateScore() {
        scoreText.text = score.toString()
    }

    private fun getTileColor(value: Int): Int {
        return when (value) {
            0 -> 0xFFCDC1B4.toInt()
            2 -> 0xFFEEE4DA.toInt()
            4 -> 0xFFEDE0C8.toInt()
            8 -> 0xFFF2B179.toInt()
            16 -> 0xFFF59563.toInt()
            32 -> 0xFFF67C5F.toInt()
            64 -> 0xFFF65E3B.toInt()
            128 -> 0xFFEDCF72.toInt()
            256 -> 0xFFEDCC61.toInt()
            512 -> 0xFFEDC850.toInt()
            1024 -> 0xFFEDC53F.toInt()
            2048 -> 0xFFEDC22E.toInt()
            else -> 0xFF3C3A32.toInt()
        }
    }

    /** --------------------- Undo + GameOver ---------------------- **/
    private fun undoMove() {
        if (prevBoards.isNotEmpty()) {
            val last = prevBoards.removeLast()
            for (i in 0..3) for (j in 0..3) board[i][j] = last[i][j]
            updateUI()
        }
    }

    private fun isBoardFull(): Boolean {
        for (i in 0..3)
            for (j in 0..3)
                if (board[i][j] == 0)
                    return false
        return true
    }

    private fun canMerge(): Boolean {
        for (i in 0..3) {
            for (j in 0..3) {
                val v = board[i][j]
                if (i < 3 && board[i + 1][j] == v) return true
                if (j < 3 && board[i][j + 1] == v) return true
            }
        }
        return false
    }

    private fun checkGameOver() {
        if (isBoardFull() && !canMerge()) {
            showGameOverDialog()
        }
    }

    private fun showGameOverDialog() {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("게임 종료")
            .setMessage("더 이상 이동할 수 없습니다!\n현재 점수: $score")
            .setPositiveButton("다시하기") { _, _ ->
                resetGame()
            }
            .setNegativeButton("종료") { _, _ ->
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            .setCancelable(false)
            .show()
    }
}
