package com.example.seminar_assignment_2025

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SecondActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        findViewById<ImageView>(R.id.triangle).setOnClickListener {
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val workspaceUrl = intent.getStringExtra(MainActivity.EXTRA_WORKSPACE) ?: ""

        val sections = listOf(
            Section("Github", "내용내용내용","https://github.com/isosunghoon"),
            Section("슬랙 URL", workspaceUrl),
            Section("학과/학번", "컴퓨터공학부 25학번"),
            Section("신청 이유", "안드로이드 앱 개발이 사람들이 가장 쉽게 접근할 수 있는 플랫폼이라고 생각해서, " +
                    "개발한 것이 눈에 바로바로 보였으면 좋겠어서, " + "금요일/주말에 학교오기 싫어서 안드로이드 세미나 신청했습니다!")
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SectionAdapter(sections)
    }
}
