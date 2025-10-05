// GameScreen.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.abs

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    // ViewModel의 StateFlow를 구독하여 상태가 변경될 때마다 자동으로 Recomposition
    val board by gameViewModel.board.collectAsState()
    val score by gameViewModel.score.collectAsState()

    var swipeDirection by remember { mutableStateOf<Direction?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { swipeDirection = null },
                    onDragEnd = {
                        swipeDirection?.let { dir ->
                            // 드래그가 끝나면 ViewModel에 이동 이벤트 전달
                            gameViewModel.move(dir)
                        }
                    },
                    onDrag = { change, dragAmount ->
                        if (swipeDirection == null) {
                            val (dx, dy) = dragAmount
                            if (abs(dx) > 20 || abs(dy) > 20) {
                                swipeDirection = if (abs(dx) > abs(dy)) {
                                    if (dx > 0) Direction.RIGHT else Direction.LEFT
                                } else {
                                    if (dy > 0) Direction.DOWN else Direction.UP
                                }
                            }
                        }
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GameHeader(score)
        Spacer(modifier = Modifier.height(32.dp))
        GameBoard(board)
        Spacer(modifier = Modifier.height(32.dp))
        GameControls(
            onResetClick = { gameViewModel.resetGame() },
            onUndoClick = { gameViewModel.undo() }
        )
    }
}

@Composable
fun GameHeader(score: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "2048", fontSize = 48.sp, fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .background(Color.Gray, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "SCORE", fontSize = 16.sp, color = Color.White)
                Text(text = "$score", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun GameControls(onResetClick: () -> Unit, onUndoClick: () -> Unit) {
    Row {
        Button(onClick = onUndoClick) {
            Text("뒤로가기")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = onResetClick) {
            Text("다시하기")
        }
    }
}


@Composable
fun GameBoard(board: List<List<Int>>) {
    Box(
        modifier = Modifier
            .background(Color(0xFFBBADA0), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            board.forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { number ->
                        GameCell(number = number)
                    }
                }
            }
        }
    }
}

@Composable
fun GameCell(number: Int) {
    val (backgroundColor, textColor) = getTileColors(number)

    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (number != 0) {
            Text(
                text = "$number",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

// 각 숫자에 맞는 타일 색상을 반환하는 헬퍼 함수
@Composable
fun getTileColors(number: Int): Pair<Color, Color> {
    val backgroundColor = when (number) {
        0 -> Color(0xFFCDC1B4)
        2 -> Color(0xFFEEE4DA)
        4 -> Color(0xFFEDE0C8)
        8 -> Color(0xFFF2B179)
        16 -> Color(0xFFF59563)
        32 -> Color(0xFFF67C5F)
        64 -> Color(0xFFF65E3B)
        128 -> Color(0xFFEDCF72)
        256 -> Color(0xFFEDCC61)
        512 -> Color(0xFFEDC850)
        1024 -> Color(0xFFEDC53F)
        2048 -> Color(0xFFEDC22E)
        else -> Color(0xFF3C3A32)
    }
    val textColor = if (number <= 4) Color(0xFF776E65) else Color.White
    return backgroundColor to textColor
}