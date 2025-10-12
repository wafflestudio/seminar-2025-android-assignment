package com.example.seminar_assignment_2025
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.seminar_assignment_2025.ui.MainScreenWithNav
import com.example.seminar_assignment_2025.ui.theme.Seminarassignment2025Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Seminarassignment2025Theme {
                MainScreenWithNav()
            }
        }
    }
}