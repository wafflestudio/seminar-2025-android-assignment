package com.example.seminar_assignment_2025;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText slackEdit = findViewById(R.id.slackEditText);
        Button loginBtn = findViewById(R.id.loginBtn);

        // URL 입력 감지 → 버튼 색상 변경
        slackEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    loginBtn.setEnabled(false);
                    loginBtn.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
                } else {
                    loginBtn.setEnabled(true);
                    loginBtn.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_blue_light));
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // "계속" 버튼 → 프로필 화면으로 이동
        loginBtn.setOnClickListener(v -> {
            String slackUrl = slackEdit.getText().toString();

            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            intent.putExtra("slack_url", slackUrl);

            // 샘플 데이터
            intent.putExtra("name", "김철민");
            intent.putExtra("mbti", "ISTP");
            intent.putExtra("region", "서울시 관악구");
            intent.putExtra("github", "rlacjfels110");
            intent.putExtra("reason", "솔직하게 말하자면 spring 세마나를 1순위로 신청하였는데 선착순에서 밀려 3순위인 Android를 수강하게 되었습니다." +
                    "하지만 평소에 앱개발도 하고 싶었고 여기서 배우는 내용이 알차기에 완전 만족하고 있습니다!");

            startActivity(intent);
        });
    }
}
