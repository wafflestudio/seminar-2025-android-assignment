    package com.example.seminar_assignment_2025

    import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

    class MainActivity: AppCompatActivity(){

        companion object {
            const val EXTRA_WORKSPACE = "com.example.seminar_assignment_2025.EXTRA_WORKSPACE"
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)


            val urlInput = findViewById<EditText>(R.id.urlInput)
            val continueButton = findViewById<Button>(R.id.continueButton)

            continueButton.isEnabled = false

            urlInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    continueButton.isEnabled = !s.isNullOrBlank() && s.toString().endsWith(".slack.com")
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            continueButton.setOnClickListener {
                val inputText = urlInput.text.toString().trim()
                val intent = Intent(this, SecondActivity::class.java).apply {
                    putExtra(EXTRA_WORKSPACE, inputText)
                }
                startActivity(intent)
            }
        }
    }
