package com.example.assignment2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// MainActivity에서 옮겨온 GameScreen 함수
@Composable
fun GameScreen() {
    // 앞으로 이 공간에 게임 로직과 UI를 구현하게 됩니다.
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Game Screen in a separate file!")
    }
}
