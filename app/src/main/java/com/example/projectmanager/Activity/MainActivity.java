package com.example.projectmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectmanager.Adapter.HomeRvAdapter;
import com.example.projectmanager.R;
import com.example.projectmanager.Model.Projects;
import com.example.projectmanager.Utils.RecyclerItemClickListener;
import com.google.android.material.card.MaterialCardView;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.projectmanager.Activity.LoginActivity.HOST_NAME;
import static com.example.projectmanager.Activity.LoginActivity.USER_FAMILY;
import static com.example.projectmanager.Activity.LoginActivity.USER_ID;
import static com.example.projectmanager.Activity.LoginActivity.USER_LEVEL;
import static com.example.projectmanager.Activity.LoginActivity.USER_NAME;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTag";
    public static Boolean ADMIN = false;
    Intent intent;
    ImageView backIcon;
    MaterialCardView profileCardView;
    TextView tvUserName, tvUserID;
    int w, h;
    RecyclerView homeRV;
    private ArrayList<Projects> projectList = new ArrayList<>();
    SimpleDateFormat inputDate, outputDate;
    SpaceNavigationView spaceNavigationView;
    private int exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        w = getWindowManager().getDefaultDisplay().getWidth();
        h = getWindowManager().getDefaultDisplay().getHeight();

        backIcon = findViewById(R.id.backgrand_icon);
        homeRV = findViewById(R.id.home_rv);
        profileCardView = findViewById(R.id.profile_icon_cardview);
        tvUserName = findViewById(R.id.tv_username);
        tvUserID = findViewById(R.id.tv_user_id);

        inputDate = new SimpleDateFormat("yyy-mm-dd hh:mm:ss");
        outputDate = new SimpleDateFormat("yyy-mm-dd");

        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_action_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("INBOX", R.drawable.ic_action_message));
        spaceNavigationView.changeCurrentItem(0);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w / 2, (int) (h / 1.5));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        backIcon.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(w, h);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ABOVE, R.id.space);
        params.setMargins(w / 22, h / 10, w / 22, 0);
        homeRV.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(w / 10, h / 18);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(0, h / 60, w / 50, 0);
        profileCardView.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.LEFT_OF, R.id.profile_icon_cardview);
        params.setMargins(0, h / 40, w / 60, 0);
        tvUserName.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(w / 40, h / 40, 0, 0);
        tvUserID.setLayoutParams(params);

        homeRV.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

        homeRV.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, homeRV, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "position " + position, Toast.LENGTH_SHORT).show();
                spaceNavigationView.showBadgeAtIndex(1, 3, getResources().getColor(R.color.badge_background_color));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        String link;
        if (USER_LEVEL.equals("1"))
            link = "mitra/GetData.php";
        else
            link = "mitra/GetData.php?pers_code=" + USER_ID;

        tvUserID.setText(USER_ID);
        tvUserName.setText(USER_NAME + " " + USER_FAMILY);

        AndroidNetworking.get(HOST_NAME + link)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray fullProjects = response.getJSONArray("projects");
                            JSONObject project;
                            Date date = new Date();
                            for (int i = 0; i < fullProjects.length(); i++) {
                                project = fullProjects.getJSONObject(i);
                                Log.d(TAG, "onResponse: " + project);
                                try {
                                    date = (inputDate.parse(project.getString("date")));
                                } catch (ParseException e) {
                                    Log.d(TAG, "onErrorResponse: " + e.getMessage());
                                    e.printStackTrace();
                                }
                                projectList.add(new Projects(project.getInt("id")
                                        , project.getString("projectName")
                                        , project.getString("projectIntro")
                                        , project.getString("producerId")
                                        , outputDate.format(date)));
                                Log.d(TAG, "onResponse: " + projectList.get(i).getProjectName());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }
                        homeRV.setAdapter(new HomeRvAdapter(projectList));

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d(TAG, "onError: " + anError);
                    }
                });

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                intent = new Intent(MainActivity.this, AddProjectActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex == 0) {
//                    intent = new Intent(MainActivity.this, MainActivity.class);
//                    startActivity(intent);
                } else {
                    intent = new Intent(MainActivity.this, InboxActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });

        profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("name", USER_NAME + " " + USER_FAMILY);
                intent.putExtra("id", USER_ID);
                startActivity(intent);
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

    @Override
    protected void onStop() {
        super.onStop();
        projectList.clear();
    }

    @Override
    public void onBackPressed() {
        exit++;
        if (exit == 2)
            finishAffinity();
        else {
            Toast.makeText(this, "برای خروج دوباره بزن!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = 0;
                }
            }, 2500);
        }
    }
}
