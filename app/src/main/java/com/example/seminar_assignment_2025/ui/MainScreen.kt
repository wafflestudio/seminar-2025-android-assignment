package com.example.seminar_assignment_2025.ui
import GameScreen
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

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Search,
                    BottomNavItem.App,
                    BottomNavItem.Game,
                    BottomNavItem.Profile
                )
                items.forEach { item ->
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
            when (selectedRoute) {
                BottomNavItem.Home.route -> PlaceholderScreen("Home")
                BottomNavItem.Search.route -> PlaceholderScreen("Search")
                BottomNavItem.App.route -> PlaceholderScreen("App")
                BottomNavItem.Game.route -> GameScreen()
                BottomNavItem.Profile.route -> ProfileScreen()
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