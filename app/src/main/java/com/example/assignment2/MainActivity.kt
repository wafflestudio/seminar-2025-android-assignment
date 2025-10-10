package com.example.assignment2

import android.os.Bundle
import com.example.assignment2.GameScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.widget.FrameLayout
import android.view.LayoutInflater
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.assignment2.ui.theme.Assignment2Theme
import androidx.customview.widget.ExploreByTouchHelper.INVALID_ID
import android.view.View

// 1. 네비게이션 아이템 데이터 클래스 정의
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    // 2. 네비게이션 아이템 리스트 생성
    val navItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Search", Icons.Default.Search, "search"),
        BottomNavItem("App", Icons.Default.ShoppingCart, "app"),
        BottomNavItem("Game", Icons.Default.PlayArrow, "game"),
        BottomNavItem("Profile", Icons.Default.AccountCircle, "profile")
    )

    // 3. 현재 선택된 탭의 인덱스를 기억하는 상태 변수
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    // 이전 인덱스를 기억 (애니메이션 방향 계산용)
    var previousItemIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            if (selectedItemIndex != index) {
                                previousItemIndex = selectedItemIndex // 이전 인덱스 저장
                                selectedItemIndex = index // 현재 인덱스 업데이트
                            }
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // 4. 선택된 탭에 따라 화면을 전환 (애니메이션 포함)
        AnimatedContent(
            targetState = selectedItemIndex,
            modifier = Modifier.padding(innerPadding),
            transitionSpec = {
                // 오른쪽으로 이동하는 경우 (index 증가)
                if (targetState > initialState) {
                    slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
                } else { // 왼쪽으로 이동하는 경우 (index 감소)
                    slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
                }
            },
            label = "navigation"
        ) { targetIndex ->
            // 현재 인덱스에 맞는 화면을 보여줌
            when (navItems[targetIndex].route) {
                "home" -> HomeScreen()
                "search" -> SearchScreen()
                "app" -> AppScreen()
                "game" -> GameScreen()
                "profile" -> ProfileScreen()
            }
        }
    }
}

// --- 각 탭에 해당하는 화면들 ---

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home")
    }
}

@Composable
fun SearchScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Search")
    }
}

@Composable
fun AppScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("App")
    }
}


// MainActivity.kt


@Composable
fun ProfileScreen() {
    AndroidView(        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            // LayoutInflater를 사용한 표준적인 inflate 방식
            LayoutInflater.from(context).inflate(R.layout.profile_activity, null)
        },
        update = { /* 뷰 업데이트 로직 */ }
    )
}



