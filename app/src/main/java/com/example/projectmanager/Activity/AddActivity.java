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
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.projectmanager.Activity.LoginActivity.HOST_NAME;
import static com.example.projectmanager.Activity.LoginActivity.USER_ID;

public class AddActivity extends AppCompatActivity {

    Button signupBtn, resetBtn;
    EditText edtName, edtIntro;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add);

        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_action_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("INBOX", R.drawable.ic_action_message));

        signupBtn = findViewById(R.id.btn_add_new_project);
        resetBtn = findViewById(R.id.btn_addproj_clear);
        edtName = findViewById(R.id.edt_new_project_name);
        edtIntro = findViewById(R.id.edt_new_project_intro);

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
//                intent = new Intent(AddActivity.this, AddActivity.class);
//                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex == 0) {
                    intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(AddActivity.this, InboxActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.getText().toString().equals("") || edtIntro.getText().toString().equals("")) {
                    Toast.makeText(AddActivity.this, "لطفا اطلاعات را کامل وارد کنید!", Toast.LENGTH_SHORT).show();
                } else
                    signUpUser();

            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtName.setText("");
                edtIntro.setText("");
            }
        });
    }

    private void signUpUser() {
        AndroidNetworking.get(HOST_NAME + "mitra/AddProject.php?project_name={projectName}&project_intro={projectIntro}" +
                "&personnel_code={personnelCode}")
                .addPathParameter("projectName", edtName.getText().toString())
                .addPathParameter("projectIntro", edtIntro.getText().toString())
                .addPathParameter("personnelCode", USER_ID)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(AddActivity.this, " " + response.getString("message"), Toast.LENGTH_SHORT).show();
                            if (response.getString("success").equals("1")) {
                                startActivity(new Intent(AddActivity.this, MainActivity.class));
                                finish();

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
