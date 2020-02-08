package com.example.projectmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    public static String USER_LEVEL = "0";
    public static String USER_NAME = " ";
    public static String USER_FAMILY = " ";

    Button loginBtn, signUpBtn;
    EditText usernameBox, passwordBox;
    TextView usernameHint, passwordHint;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.btn_sign_in);
        signUpBtn = findViewById(R.id.btn_sign_up);
        usernameBox = findViewById(R.id.edt_username);
        passwordBox = findViewById(R.id.edt_pass);
        usernameHint = findViewById(R.id.hint_username);
        passwordHint = findViewById(R.id.hint_password);
        progressBar = findViewById(R.id.loginProgress);

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
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                progressBar.setVisibility(View.GONE);

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
