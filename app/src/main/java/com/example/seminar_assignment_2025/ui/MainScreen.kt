@file:OptIn(ExperimentalAnimationApi::class)

package com.example.seminar_assignment_2025.ui

import GameScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.seminar_assignment_2025.ui.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithNav() {
    var selectedRoute by remember { mutableStateOf(BottomNavItem.Home.route) }

    // 네비게이션 아이템 리스트를 Scaffold 외부에서 한 번만 정의하여
    // BottomBar와 AnimatedContent에서 모두 참조할 수 있도록 합니다.
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.App,
        BottomNavItem.Game,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = selectedRoute == item.route,
                        onClick = { selectedRoute = item.route },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // AnimatedContent를 사용하여 화면 전환에 애니메이션을 적용합니다.
            AnimatedContent(
                targetState = selectedRoute,
                label = "main_screen_transition", // 디버깅을 위한 레이블
                transitionSpec = {
                    // 이전 화면과 새 화면의 인덱스를 찾아 이동 방향을 결정합니다.
                    val oldIndex = navItems.indexOfFirst { it.route == initialState }
                    val newIndex = navItems.indexOfFirst { it.route == targetState }

                    // 오른쪽으로 이동하는 경우 (예: Home -> Search)
                    if (newIndex > oldIndex) {
                        // 새 화면은 오른쪽에서 들어오고, 이전 화면은 왼쪽으로 나갑니다.
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(300)
                        ) + fadeIn(animationSpec = tween(300)) togetherWith slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(300)
                        ) + fadeOut(animationSpec = tween(300))
                    }
                    // 왼쪽으로 이동하는 경우 (예: Game -> Home)
                    else {
                        // 새 화면은 왼쪽에서 들어오고, 이전 화면은 오른쪽으로 나갑니다.
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(300)
                        ) + fadeIn(animationSpec = tween(300)) togetherWith slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(300)
                        ) + fadeOut(animationSpec = tween(300))
                    }
                }
            ) { route ->
                // targetState(여기서는 route)에 따라 표시할 화면을 결정합니다.
                when (route) {
                    BottomNavItem.Home.route -> PlaceholderScreen("Home")
                    BottomNavItem.Search.route -> PlaceholderScreen("Search")
                    BottomNavItem.App.route -> PlaceholderScreen("App")
                    BottomNavItem.Game.route -> GameScreen()
                    BottomNavItem.Profile.route -> ProfileScreen()
                }
            }
        }
    }
}


@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, fontSize = 24.sp)
    }
}