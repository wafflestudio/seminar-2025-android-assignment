package com.example.seminar_assignment_2025

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.seminar_assignment_2025.ui.theme.Seminarassignment2025Theme


class MainActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextUrl = findViewById<EditText>(R.id.editTextUrl)
        val continueBtn = findViewById<Button>(R.id.buttonContinue)

        // 처음엔 비활성화
        continueBtn.isEnabled = false

        // Slack URL 정규식 (영문/숫자/하이픈 + .slack.com)
        val slackUrlRegex = Regex("^[a-zA-Z0-9-]+\\.slack\\.com$")

        // 텍스트 변경 감지
        editTextUrl.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().trim()
                continueBtn.isEnabled = input.matches(slackUrlRegex)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        continueBtn.setOnClickListener {
            val url = editTextUrl.text.toString().trim()
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("workspaceUrl", url)   // ★ 전달 포인트
            }
            startActivity(intent)
        }
    }
}
