package com.example.projectmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectmanager.Adapter.HomeRvAdapter;
import com.example.projectmanager.R;
import com.example.projectmanager.Utils.Projects;
import com.example.projectmanager.Utils.RecyclerItemClickListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTag";
    Intent intent;
    ImageView backIcon;
    int w, h;
    RecyclerView homeRV;
    ArrayList<Projects> projectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        w = getWindowManager().getDefaultDisplay().getWidth();
        h = getWindowManager().getDefaultDisplay().getHeight();

        backIcon = findViewById(R.id.backgrand_icon);
        homeRV = findViewById(R.id.home_rv);

        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_action_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("INBOX", R.drawable.ic_action_message));
//        spaceNavigationView.showBadgeAtIndex(2, 5, getResources().getColor(R.color.badge_background_color));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w / 2, (int) (h / 1.5));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        backIcon.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(w, h);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ABOVE, R.id.space);
        params.setMargins(w / 22, h / 25, w / 22, 0);
        homeRV.setLayoutParams(params);

        homeRV.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

        homeRV.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, homeRV, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "position " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        AndroidNetworking.get("http://192.168.1.114/mitra/GetData.php")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray fullProjects = response.getJSONArray("projects");
                            JSONObject project;
                            for (int i = 0; i < fullProjects.length(); i++) {
                                project = fullProjects.getJSONObject(i);
                                Log.d(TAG, "onResponsex: " + project);
                                projectList.add(new Projects(project.getInt("id")
                                        , project.getString("projectName")
                                        , project.getString("projectIntro")
                                        , project.getString("producerId")
                                        , project.getString("date")));
                                Log.d(TAG, "onResponse: " + projectList.get(i).getProjectName());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                if (itemIndex == 0) {
//                    intent = new Intent(MainActivity.this, MainActivity.class);
//                    startActivity(intent);
                } else {
                    intent = new Intent(MainActivity.this, AddActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

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
