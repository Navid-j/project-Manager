package com.example.projectmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectmanager.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    Button signupBtn, resetBtn;
    EditText edtName, edtFamily, edtPersonnelCode, edtPhoneNumber, edtUsername, edtPassword, edtVerifyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupBtn = findViewById(R.id.btn_signup_register);
        resetBtn = findViewById(R.id.btn_signup_reset);
        edtName = findViewById(R.id.edt_signup_name);
        edtFamily = findViewById(R.id.edt_signup_family);
        edtPersonnelCode = findViewById(R.id.edt_signup_personnel_code);
        edtPhoneNumber = findViewById(R.id.edt_signup_phone);
        edtUsername = findViewById(R.id.edt_signup_username);
        edtPassword = findViewById(R.id.edt_signup_password);
        edtVerifyPassword = findViewById(R.id.edt_signup_verify_password);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtVerifyPassword.getText().toString().equals(edtPassword.getText().toString())) {
                    signUpUser(edtVerifyPassword.getText().toString());
                } else
                    Toast.makeText(SignupActivity.this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();

            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtName.setText("");
                edtFamily.setText("");
                edtPersonnelCode.setText("");
                edtPhoneNumber.setText("");
                edtUsername.setText("");
                edtPassword.setText("");
                edtVerifyPassword.setText("");
            }
        });
    }

    private void signUpUser(String verifiedPass) {
        AndroidNetworking.get("http://192.168.1.114/mitra/Register.php?first_name={firstName}&last_name={lastName}" +
                "&personnel_code={personnelCode}&phone_number={phoneNumber}&level=0&user={userName}&pass={password}")
                .addPathParameter("firstName", edtName.getText().toString())
                .addPathParameter("lastName", edtFamily.getText().toString())
                .addPathParameter("personnelCode", edtPersonnelCode.getText().toString())
                .addPathParameter("phoneNumber", edtPhoneNumber.getText().toString())
                .addPathParameter("userName", edtUsername.getText().toString())
                .addPathParameter("password", verifiedPass)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(SignupActivity.this, "status:" + response.getString("message"), Toast.LENGTH_SHORT).show();
                            if (response.getString("success").equals("1")) {
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("loginActivity", "onError: " + anError);
                        Log.e("loginActivity", "onError: " + anError.getResponse());

                    }
                });
    }
}
