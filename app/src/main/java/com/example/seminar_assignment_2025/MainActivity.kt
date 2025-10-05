package com.example.seminar_assignment_2025

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.tooling.preview.Preview
import com.example.seminar_assignment_2025.ui.theme.Seminarassignment2025Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 기존의 setContentView(...) 코드를 모두 지우고
        // setContent { ... } 로 변경합니다.
        setContent {
            Seminarassignment2025Theme {
                // 우리가 1단계에서 만든 ProfileScreen() 함수를 여기서 호출합니다.
                ProfileScreen()
            }
        }
    }
}