package com.example.projectmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectmanager.R;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    public static String HOST_NAME = "http://192.168.1.114/";
    public static String USER_ID = null;
    public static Boolean isLogin = false;
    public static String USER_LEVEL = "0";
    public static String USER_NAME = " ";
    public static String USER_FAMILY = " ";
    public static String MY_PREF = " ";

    Button loginBtn, signUpBtn;
    EditText usernameBox, passwordBox;
    CardView mainCardView;
    ProgressBar progressBar;
    ImageView loginLogo;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DisplayMetrics displayMetrics = LoginActivity.this.getResources().getDisplayMetrics();

        preferences = getApplicationContext().getSharedPreferences(MY_PREF, MODE_PRIVATE);
        editor = preferences.edit();

        isLogin = preferences.getBoolean("isLogin", false);
        USER_ID = preferences.getString("userID", null);
        USER_LEVEL = preferences.getString("USER_LEVEL", "0");
        USER_NAME = preferences.getString("USER_NAME", " ");
        USER_FAMILY = preferences.getString("USER_FAMILY", " ");

        if (isLogin)
            startActivity(new Intent(LoginActivity.this, MainActivity.class));


        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;

        loginBtn = findViewById(R.id.btn_sign_in);
        signUpBtn = findViewById(R.id.btn_sign_up);
        usernameBox = findViewById(R.id.edt_username);
        passwordBox = findViewById(R.id.edt_pass);
        progressBar = findViewById(R.id.loginProgress);
        mainCardView = findViewById(R.id.login_cardview);
        loginLogo = findViewById(R.id.login_logo);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameBox.getVisibility() == View.VISIBLE)
                    authentication();
                else {
                    usernameBox.setVisibility(View.VISIBLE);
                    passwordBox.setVisibility(View.VISIBLE);
                }
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (w / 1.2), h / 2);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mainCardView.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(w / 4, h / 6);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.topMargin = h / 40;
        loginLogo.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(w / 4, h / 10);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.login_cardview);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.topMargin = h / 40;
        params.leftMargin = -w / 8;
        signUpBtn.setLayoutParams(params);


        params = new RelativeLayout.LayoutParams((int) (w / 1.5), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, R.id.login_logo);
        params.topMargin = h / 30;
        usernameBox.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams((int) (w / 1.5), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, R.id.edt_username);
        params.topMargin = h / 30;
        passwordBox.setLayoutParams(params);

    }

    private void authentication() {

        progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.get(HOST_NAME + "mitra/Login.php?personnelCode={code}&pass={password}")
                .addPathParameter("code", usernameBox.getText().toString())
                .addPathParameter("password", passwordBox.getText().toString())
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(LoginActivity.this, " " + response.getString("message"), Toast.LENGTH_SHORT).show();
                            if (response.getString("success").equals("1")) {
                                USER_ID = usernameBox.getText().toString();
                                USER_LEVEL = response.getString("level");
                                USER_NAME = response.getString("name");
                                USER_FAMILY = response.getString("family");
                                editor.putBoolean("isLogin", true);
                                editor.putString("userID", USER_ID);
                                editor.putString("USER_LEVEL", response.getString("level"));
                                editor.putString("USER_NAME", response.getString("name"));
                                editor.putString("USER_FAMILY", response.getString("family"));
                                editor.apply();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                progressBar.setVisibility(View.GONE);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("loginActivity", "onError: " + anError);
                        Toast.makeText(LoginActivity.this, "" + anError, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    }
                });
    }
}
