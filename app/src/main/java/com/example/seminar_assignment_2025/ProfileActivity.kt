package com.example.seminar_assignment_2025

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val workspaceUrl = intent.getStringExtra("WORKSPACE_URL") ?: "입력되지 않음"

        val slackText = findViewById<TextView>(R.id.slack_url_text)
        slackText.text = "슬랙 URL: $workspaceUrl"

        val githubLink = findViewById<TextView>(R.id.githubLink)
        githubLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://github.com/sisihae") // 자신의 GitHub URL
            startActivity(intent)
        }
    }
}
