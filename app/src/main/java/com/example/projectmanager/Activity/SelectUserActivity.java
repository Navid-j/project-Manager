package com.example.projectmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectmanager.Adapter.HomeRvAdapter;
import com.example.projectmanager.Model.Projects;
import com.example.projectmanager.Model.UserModel;
import com.example.projectmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.projectmanager.Activity.LoginActivity.HOST_NAME;

public class SelectUserActivity extends AppCompatActivity {

    private static final String TAG = "SelectUserActivity";
    private List<UserModel> users = new ArrayList<>();
    private ListView lvUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        lvUsers = findViewById(R.id.lv_user_select);
        GetUsers();

        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("username", users.get(i).getUserName());
                intent.putExtra("userId", users.get(i).getUserId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void GetUsers() {
        String link = "mitra/UserList.php";
        AndroidNetworking.get(HOST_NAME + link)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray usersList = response.getJSONArray("users");
                            JSONObject user;
                            for (int i = 0; i < usersList.length(); i++) {
                                user = usersList.getJSONObject(i);

                                users.add(i, new UserModel(user.get("userName").toString(), user.get("producerId").toString()));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }

                        lvUsers.setAdapter(new MyAdap(users));

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d(TAG, "onError: " + anError);
                    }
                });
    }

    private class MyAdap extends BaseAdapter {

        private List<UserModel> list = new ArrayList<>();
        private TextView username;

        public MyAdap(List<UserModel> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View v = LayoutInflater.from(SelectUserActivity.this).inflate(R.layout.user_list, viewGroup, false);

            username = v.findViewById(R.id.tv_user_list);
            username.setText(list.get(i).getUserName());

            return v;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
