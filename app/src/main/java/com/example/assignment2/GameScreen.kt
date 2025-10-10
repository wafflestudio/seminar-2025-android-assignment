package com.example.assignment2
// GameScreen.kt 상단



import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize

import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

// 스와이프 방향 Enum
enum class Direction { LEFT, RIGHT, UP, DOWN }

// 타일 숫자별 색상 맵
val tileColors = mapOf(
    0 to Color(0xFFCDC1B4),
    2 to Color(0xFFEEE4DA),
    4 to Color(0xFFEDE0C8),
    8 to Color(0xFFF2B179),
    16 to Color(0xFFF59563),
    32 to Color(0xFFF67C5F),
    64 to Color(0xFFF65E3B),
    128 to Color(0xFFEDCF72),
    256 to Color(0xFFEDCC61),
    512 to Color(0xFFEDC850),
    1024 to Color(0xFFEDC53F),
    2048 to Color(0xFFEDC22E)
)
@OptIn(ExperimentalAnimationApi::class)
// GameScreen.kt의 GameScreen 함수

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val board by gameViewModel.board.collectAsState()
    val score by gameViewModel.score.collectAsState()
    val isGameOver by gameViewModel.isGameOver.collectAsState()
    var swipeDirection by remember { mutableStateOf<Direction?>(null) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 상단 UI (점수, 버튼)
            Header(score = score, onUndo = { gameViewModel.undoMove() }, onReset = { gameViewModel.resetGame() })
            Spacer(modifier = Modifier.height(32.dp))

            // 게임 보드
            Box(
                modifier = Modifier
                    // 1. 스와이프 감지를 Column이 아닌 게임 보드 영역으로 옮김
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { swipeDirection = null },
                            onDragEnd = {
                                swipeDirection?.let {
                                    gameViewModel.onSwipe(it)
                                }
                            },
                            onDrag = { change, dragAmount ->
                                if (swipeDirection == null) {
                                    val (dx, dy) = dragAmount
                                    // 스와이프 민감도 조절 (필요 시 값 변경)
                                    if (abs(dx) > 15 || abs(dy) > 15) {
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
                contentAlignment = Alignment.Center
            ) {
                // GameBoard에 애니메이션 적용
                GameBoard(board = board)

                if (isGameOver) {
                    GameOverOverlay(
                        modifier = Modifier.matchParentSize(),
                        onReset = { gameViewModel.resetGame() }
                    )
                }
            }
        }
    }
}


@Composable
fun Header(score: Int, onUndo: () -> Unit, onReset: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween, // SpaceAround에서 SpaceBetween으로 변경하여 양 끝으로 정렬
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. 점수 표시 부분 (변경 없음)
        // Header 함수 내부

// 1. 점수 표시 부분 (수정됨)
        Column(
            modifier = Modifier
                .width(90.dp) // 너비를 지정하여 충분한 공간 확보
                .background(
                    color = Color(0xFFEEE4DA), // 버튼과 동일한 배경색 적용
                    shape = RoundedCornerShape(12.dp) // 버튼과 동일한 둥근 모서리 적용
                )
                .padding(vertical = 4.dp), // 텍스트와 배경의 상하 여백 추가
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Score",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF776E65).copy(alpha = 0.8f) // 배경색과 어울리는 텍스트 색상
            )
            Text(
                text = "$score",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF776E65) // 배경색과 어울리는 텍스트 색상
            )
        }


        // 2. 버튼들을 담을 Row 추가
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) { // 버튼 사이에 8.dp 간격 추가

            // 3. Undo 버튼 -> 아이콘 버튼으로 변경
            IconButton(
                onClick = onUndo,
                modifier = Modifier
                    .size(70.dp) // 버튼 크기 지정
                    .background(
                        color = Color(0xFFEEE4DA), // 배경색 지정
                        shape = RoundedCornerShape(12.dp) // 둥근 모서리 적용
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Clear, // 1단계에서 import한 Clear 아이콘
                    contentDescription = "Undo Move", // 스크린 리더를 위한 설명
                    tint = MaterialTheme.colorScheme.onPrimaryContainer // 아이콘 색상
                )
            }

            // 4. Reset 버튼 -> 아이콘 버튼으로 변경
            IconButton(
                onClick = onReset,
                modifier = Modifier
                    .size(70.dp) // 버튼 크기 지정
                    .background(
                        color = Color(0xFFEEE4DA), // 배경색 지정
                        shape = RoundedCornerShape(12.dp) // 둥근 모서리 적용
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh, // 1단계에서 import한 Refresh 아이콘
                    contentDescription = "Reset Game", // 스크린 리더를 위한 설명
                    tint = MaterialTheme.colorScheme.onPrimaryContainer // 아이콘 색상
                )
            }
        }
    }
}



// GameScreen.kt의 GameBoard 함수

@Composable
fun GameBoard(board: List<List<Int>>) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFBBADA0))            .padding(8.dp)
            // 2. 이 Modifier를 추가하는 것이 핵심입니다.
            .animateContentSize(animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )

            ))
     {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (board.isNotEmpty()) {
                board.forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { value ->
                            Tile(value)
                        }
                    }
                }
            }
        }
    }
}


// GameScreen.kt의 Tile 함수

@Composable
fun Tile(value: Int) {
    Box(        modifier = Modifier
        .size(70.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(tileColors[value] ?: Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        // 값(숫자)이 0이 아닐 때만 Text를 보여주는 간단한 구조로 복원
        if (value != 0) {
            Text(
                text = "$value",
                fontSize = if (value < 100) 28.sp else if (value < 1000) 24.sp else 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (value <= 4) Color(0xFF776E65) else Color.White
            )
        }
    }
}


@Composable
fun GameOverOverlay(modifier: Modifier = Modifier, onReset: () -> Unit) {
    Box(
        // 외부에서 전달된 modifier를 사용하고, 나머지 속성을 추가합니다.
        modifier = modifier
            .clip(RoundedCornerShape(8.dp)) // GameBoard와 동일한 둥근 모서리 적용
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Game Over", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onReset) {
                Text("Try Again", fontSize = 20.sp)
            }
        }
    }
}
