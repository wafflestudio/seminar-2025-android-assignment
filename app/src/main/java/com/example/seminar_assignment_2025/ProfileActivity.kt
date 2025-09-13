package com.example.seminar_assignment_2025

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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