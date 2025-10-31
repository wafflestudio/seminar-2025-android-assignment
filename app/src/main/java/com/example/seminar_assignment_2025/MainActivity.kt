package com.example.seminar_assignment_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.seminar_assignment_2025.ui.GameScreen
import com.example.seminar_assignment_2025.ui.SearchScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.seminar_assignment_2025.ui.MovieDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavGenerate()
        }
    }
}

enum class NavItem(val route: String, val label: String, val icon: ImageVector) {
    Home("home", "Home", Icons.Filled.Home),
    Search("search", "Search", Icons.Filled.Search),
    App("app", "App", Icons.Filled.ShoppingCart),
    Game("game", "Game", Icons.Filled.PlayArrow),
    Profile("profile", "Profile", Icons.Filled.Person)
}
@Composable
fun NavGenerate() {
    // jetpack 구현
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavItem.entries.forEach { item ->
                    NavigationBarItem(
                        selected = (currentRoute == item.route),
                        onClick = {
                            navController.navigate(item.route) {
                                // (백스택 관리: 탭 이동 시 스택이 쌓이지 않게 함)
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // 5. 'when' 문을 'NavHost' (내비게이션 지도)로 교체!
        NavHost(
            navController = navController,
            startDestination = NavItem.Home.route, // 시작 탭
            modifier = Modifier.padding(innerPadding)
        ) {
            // (1) 5개의 기본 탭 화면
            composable(NavItem.Home.route) { HomeScreen() }
            composable(NavItem.Search.route) {
                // (2) SearchScreen에 'navController'를 전달!
                SearchScreen(
                    onMovieClick = { movieId ->
                        // (3) MovieItem이 클릭되면, '상세 화면'으로 이동
                        navController.navigate("movieDetail/${movieId}")
                    }
                )
            }
            composable(NavItem.App.route) { AppScreen() }
            composable(NavItem.Game.route) { GameScreen() }
            composable(NavItem.Profile.route) { ProfileScreen() }

            // (4) '영화 상세' 화면 (새 경로)
            composable(
                route = "movieDetail/{movieId}", // 경로와 인자 정의
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) { backStackEntry ->
                // (5) 전달받은 'movieId'를 꺼내서 상세 화면에 전달
                val movieId = backStackEntry.arguments?.getInt("movieId")
                if (movieId != null) {
                    MovieDetailScreen(
                        movieId = movieId,
                        // 1. "뒤로 가기" 버튼이 눌리면 이 함수를 실행하라고 전달
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    // (오류 처리)
                    Text("영화를 찾을 수 없습니다.")
                }
            }
        }
    }
}
@Composable fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Home",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable fun AppScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "App",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable fun ProfileScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Profile",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}