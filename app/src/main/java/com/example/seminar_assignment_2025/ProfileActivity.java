package com.example.seminar_assignment_2025;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Toolbar 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("프로필"); // 여기서 < 프로필 표시
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        }

        // View 연결
        ImageView profileImage = findViewById(R.id.profileImage);
        TextView nameText = findViewById(R.id.nameText);
        TextView ageText = findViewById(R.id.ageText);
        TextView mbtiText = findViewById(R.id.mbtiText);
        TextView regionText = findViewById(R.id.regionText);
        TextView githubText = findViewById(R.id.githubText);
        TextView reasonText = findViewById(R.id.reasonText);
        TextView slackUrlText = findViewById(R.id.slackUrlText);

        // Intent 값 받기
        String name = getIntent().getStringExtra("name");
        String age = getIntent().getStringExtra("age");
        String mbti = getIntent().getStringExtra("mbti");
        String region = getIntent().getStringExtra("region");
        String github = getIntent().getStringExtra("github");
        String reason = getIntent().getStringExtra("reason");
        String slackUrl = getIntent().getStringExtra("slack_url");

        // 데이터 세팅
        if (name != null) nameText.setText(name);
        if (age != null) ageText.setText(age);
        if (mbti != null) mbtiText.setText("MBTI: " + mbti);
        if (region != null) regionText.setText("사는 지역: " + region);
        if (github != null) githubText.setText("Github ID: " + github);
        if (reason != null) reasonText.setText("신청 이유: " + reason);
        if (slackUrl != null) slackUrlText.setText(slackUrl);

        // 프로필 이미지
        profileImage.setImageResource(R.drawable.my_profile);

        // Github 클릭 → 브라우저 열기
        githubText.setOnClickListener(v -> {
            String url = "https://github.com/rlacjfals110";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }

    // Toolbar 뒤로가기 기능
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
