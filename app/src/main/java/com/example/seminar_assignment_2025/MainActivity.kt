package com.example.seminar_assignment_2025
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.seminar_assignment_2025.ui.MainScreenWithNav
import com.example.seminar_assignment_2025.ui.theme.Seminarassignment2025Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 기존의 setContentView(...) 코드를 모두 지우고
        // setContent { ... } 로 변경합니다.
        setContent {
            Seminarassignment2025Theme {
                // 우리가 1단계에서 만든 ProfileScreen() 함수를 여기서 호출합니다.
                MainScreenWithNav()
            }
        }
    }
}