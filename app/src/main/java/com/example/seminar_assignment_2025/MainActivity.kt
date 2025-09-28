package com.example.seminar_assignment_2025

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.workspaceEditText)
        val button = findViewById<Button>(R.id.continueButton)

        // 입력값 변화 감지해서 버튼 활성/비활성
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                button.isEnabled = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        button.setOnClickListener {
            val workspaceUrl = editText.text.toString()
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("WORKSPACE_URL", workspaceUrl)
            }
            startActivity(intent)
        }
    }
}
