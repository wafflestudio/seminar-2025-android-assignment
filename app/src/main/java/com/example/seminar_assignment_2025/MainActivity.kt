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


class MainActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_SLACK_URL = "EXTRA_SLACK_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. XML에 있던 EditText와 Button을 코드로 가져옵니다.
        val workspaceUrlInput = findViewById<EditText>(R.id.workspace_url_input)
        val continueButton = findViewById<Button>(R.id.continue_button)

        continueButton.setOnClickListener {
            // "계속" 버튼이 클릭되었을 때 실행됩니다.

            continueButton.setOnClickListener {
                // 1. ProfileActivity로 가는 Intent(소포)를 만듭니다.
                val intent = Intent(this, ProfileActivity::class.java)

                // 2. 소포에 "EXTRA_SLACK_URL"이라는 이름표로 URL 텍스트를 담습니다.
                intent.putExtra(EXTRA_SLACK_URL, workspaceUrlInput.text.toString())

                // 3. 소포를 보내 액티비티를 시작합니다.
                startActivity(intent)
            }
        }

        // 2. EditText에 '글자 감시자'를 붙여줍니다.
        workspaceUrlInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            // 3. 글자 입력이 끝났을 때 이 부분이 실행됩니다!
            override fun afterTextChanged(s: Editable?) {
                // 4. 입력된 글(s)이 비어있지 않으면(!isNullOrEmpty) 버튼을 활성화(true)시킵니다.
                val inputText = s.toString()
                continueButton.isEnabled = inputText.endsWith(".slack.com")
            }
        })
    }
}