package com.example.seminar_assignment_2025
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import com.example.seminar_assignment_2025.ProfileActivity.Companion.EXTRA_SLACK_URL

@Composable
fun ProfileScreen() {
    // AndroidView를 사용해서 XML 레이아웃을 불러옵니다.
    AndroidView(
        modifier = Modifier.fillMaxSize(),

        // factory: 이 Composable이 처음 생성될 때 딱 한 번 실행됩니다.
        // 여기서 XML 파일을 View 객체로 만들어(inflate) 반환합니다.
        factory = { context ->
            View.inflate(context, R.layout.activity_main, null)
        },

        // update: Composable이 다시 그려질 때마다 실행됩니다.
        // 예를 들어 다른 곳의 상태가 바뀌었을 때,
        // 이 XML 안의 뷰 내용을 바꾸고 싶으면 여기에 코드를 작성합니다.
        // 지금은 필요 없으니 비워둬도 괜찮습니다.
        update = { view ->
            val workspaceUrlInput = view.findViewById<EditText>(R.id.workspace_url_input)
            val continueButton = view.findViewById<Button>(R.id.continue_button)

            continueButton.setOnClickListener {
                val intent = Intent(view.context, ProfileActivity::class.java)

                // 2. 소포에 "EXTRA_SLACK_URL"이라는 이름표로 URL 텍스트를 담습니다.
                intent.putExtra(EXTRA_SLACK_URL, workspaceUrlInput.text.toString())

                // 3. 소포를 보내 액티비티를 시작합니다.
                view.context.startActivity(intent)
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
    )
}
