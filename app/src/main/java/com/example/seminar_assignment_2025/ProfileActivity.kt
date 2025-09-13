package com.example.seminar_assignment_2025

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // 1. ID로 뒤로가기 버튼(ImageView)을 찾습니다.
        val toolbarBackButton = findViewById<ImageView>(R.id.toolbar_back_button)

        // 2. 버튼에 클릭 리스너를 설정합니다.
        toolbarBackButton.setOnClickListener {
            // 3. 클릭 시 finish()를 호출하여 현재 화면을 종료합니다.
            finish()
        }

        // 1. XML에 있는 TextView를 ID로 찾아 변수에 연결합니다.
        val slackUrlTextView = findViewById<TextView>(R.id.slack_url_text)

        // 2. MainActivity가 보낸 Intent(소포)를 받습니다.
        val receivedIntent = intent

        // 3. 소포에서 "EXTRA_SLACK_URL" 이름표로 데이터를 꺼냅니다.
        val slackUrl = receivedIntent.getStringExtra(MainActivity.EXTRA_SLACK_URL)

        // 4. 꺼낸 데이터를 TextView에 텍스트로 설정합니다.
        slackUrlTextView.text = slackUrl

        // 1. ID를 이용해 XML의 TextView를 찾아 변수에 연결합니다.
        val githubIdText = findViewById<TextView>(R.id.github_id_text)

        // 2. TextView에 클릭 리스너를 설정합니다.
        githubIdText.setOnClickListener {
            // 3. 웹페이지를 열기 위한 Intent(요청)를 생성합니다.
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://github.com/dgddgd314".toUri()
            )

            // 4. 생성한 Intent를 실행합니다.
            startActivity(intent)
        }
    }
}