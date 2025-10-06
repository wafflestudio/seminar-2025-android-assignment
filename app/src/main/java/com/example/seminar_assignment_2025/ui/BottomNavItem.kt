package com.example.seminar_assignment_2025.ui
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// 각 네비게이션 아이템의 정보를 담는 설계도
sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String // 페이지 이동 시 사용할 고유 경로
) {
    object Home : BottomNavItem("Home", Icons.Filled.Home, "home")
    object Search : BottomNavItem("Search", Icons.Filled.Search, "search")
    object App : BottomNavItem("App", Icons.Filled.Check, "app") // icon 수정 요망
    object Game : BottomNavItem("Game", Icons.Filled.PlayArrow, "game") // icon 수정 요망
    object Profile : BottomNavItem("Profile", Icons.Filled.Person, "profile")
}