package com.example.seminar_assignment_2025.game

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.material3.CardDefaults.outlinedCardColors
import androidx.compose.material3.CardDefaults.outlinedCardElevation
import androidx.compose.material3.CardDefaults.outlinedShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.abs
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min

@Composable
fun Game2048Screen(vm: Game2048ViewModel = viewModel()) {
    val ui by vm.state.collectAsState()

    // 스와이프 방향 결정을 위한 상태
    var swipeDir by remember { mutableStateOf<Direction?>(null) }

    // 베이지 테마 배경
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Bg)
            .padding(16.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { swipeDir = null },
                    onDragEnd = {
                        swipeDir?.let { vm.swipe(it) }
                        swipeDir = null
                    },
                    onDrag = { change, drag ->
                        if (swipeDir == null) {
                            val (dx, dy) = drag
                            if (abs(dx) > 20 || abs(dy) > 20) {
                                swipeDir = if (abs(dx) > abs(dy)) {
                                    if (dx > 0) Direction.RIGHT else Direction.LEFT
                                } else {
                                    if (dy > 0) Direction.DOWN else Direction.UP
                                }
                            }
                        }
                        change.consume()
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Title2048()

            // 상단 카드들: 점수 / Undo / Reset
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreCard(score = ui.score, modifier = Modifier.weight(1f))

                SquareIconButton(
                    icon = Icons.AutoMirrored.Outlined.Undo,
                    contentDesc = "Undo",
                    onClick = vm::undo,
                )
                SquareIconButton(
                    icon = Icons.Outlined.Refresh,
                    contentDesc = "Reset",
                    onClick = vm::newGame
                )
            }

            // 1) 상단 레이아웃에서 보드 감싸는 부분만 이렇게
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),                // 남은 높이를 전부 할당
                contentAlignment = Alignment.Center
            ) {
                Board2048Square(ui.board)       // ⬅️ 아래 새 함수
            }


            if (ui.gameOver) {
                Text(
                    "Game Over!",
                    color = Color(0xFFD35454),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/* ---------- Top UI ---------- */

@Composable
private fun Title2048() {
    Text(
        text = "2048",
        style = MaterialTheme.typography.headlineLarge,
        color = AppColors.Title,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
private fun ScoreCard(score: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(60.dp),
        colors = outlinedCardColors(
            containerColor = AppColors.Card,
            contentColor = AppColors.CardText
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = outlinedCardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Score", style = MaterialTheme.typography.labelMedium)
            Text(
                score.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SquareIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDesc: String,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Card),
        shape = RoundedCornerShape(10.dp),
        elevation = elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = contentDesc, tint = AppColors.CardText)
        }
    }
}

/* ---------- Board ---------- */

@Composable
private fun Board2048(board: IntArray) {
    val outerShape = RoundedCornerShape(14.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(outerShape)
            .background(AppColors.Board)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (r in 0 until 4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (c in 0 until 4) {
                    val v = board[r * 4 + c]
                    Tile(value = v, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// 2) 보드를 남은 공간 안의 "진짜 정사각형"으로 렌더
@Composable
private fun Board2048Square(board: IntArray) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        // 남은 영역의 가로/세로 중 작은 값 사용
        val size = min(maxWidth, maxHeight)
        Board2048Grid(
            board = board,
            side = size,         // 전체 보드 한 변 길이
            gap = 6.dp,          // 타일 사이 간격(원하면 8.dp)
            outerRadius = 12.dp, // 보드 모서리
            cellRadius = 8.dp    // 타일 모서리
        )
    }
}

// 3) 간격/패딩까지 직접 계산해서 정확히 배치
@Composable
private fun Board2048Grid(
    board: IntArray,
    side: Dp,
    gap: Dp,
    outerRadius: Dp,
    cellRadius: Dp
) {
    val n = 4
    val totalGap = gap * (n + 1)              // 바깥 여백 1칸 + 내부 n-1칸 + 끝 여백 1칸
    val cell = (side - totalGap) / n          // 타일 한 변 크기

    Column(
        modifier = Modifier
            .size(side)
            .clip(RoundedCornerShape(outerRadius))
            .background(AppColors.Board)
            .padding(gap),                     // 바깥 한 칸
        verticalArrangement = Arrangement.spacedBy(gap)
    ) {
        for (r in 0 until n) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                for (c in 0 until n) {
                    val v = board[r * n + c]
                    Box(
                        modifier = Modifier
                            .size(cell)       // ← 정확한 타일 크기
                            .clip(RoundedCornerShape(cellRadius))
                            .background(if (v == 0) AppColors.Empty else tileColor(v)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (v != 0) {
                            Text(
                                text = v.toString(),
                                color = if (v <= 4) AppColors.DarkText else Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun Tile(value: Int, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .clip(shape)
            .background(tileColor(value)),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                text = value.toString(),
                color = if (value <= 4) AppColors.DarkText else Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

/* ---------- Colors (베이지 팔레트) ---------- */

private object AppColors {
    val Bg = Color(0xFFFAF8EF)         // 화면 배경
    val Board = Color(0xFFBBADA0)      // 보드 배경
    val Empty = Color(0xFFCDC1B4)      // 빈 칸
    val Title = Color(0xFF7B6F64)

    val Card = Color(0xFFEDE3D6)       // 점수/버튼 카드 배경
    val CardText = Color(0xFF7A6E63)   // 카드 텍스트/아이콘
    val DarkText = Color(0xFF776E65)
}

private fun tileColor(v: Int): Color = when (v) {
    0 -> AppColors.Empty
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
    else -> Color(0xFFEDC22E)
}