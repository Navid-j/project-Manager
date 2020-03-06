package com.example.projectmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projectmanager.R;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    int w, h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);

        TextView tvName = findViewById(R.id.tv_profile_name);
        TextView tvId = findViewById(R.id.tv_profile_id);
        Button btnExit = findViewById(R.id.btn_profile_exit);
        LinearLayout layoutParent = findViewById(R.id.profile_parent);

        preferences = getApplicationContext().getSharedPreferences(" ", MODE_PRIVATE);
        edit = preferences.edit();

        w = getWindowManager().getDefaultDisplay().getWidth();
        h = getWindowManager().getDefaultDisplay().getHeight();

        tvName.setText(getIntent().getStringExtra("name"));
        tvId.setText(getIntent().getStringExtra("id"));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (w / 1.5), (int) (h / 3.5));
        params.gravity = Gravity.CENTER;
        layoutParent.setLayoutParams(params);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.clear().apply();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}
