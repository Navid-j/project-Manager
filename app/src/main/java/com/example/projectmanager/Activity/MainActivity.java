package com.example.projectmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.projectmanager.R;
import com.example.projectmanager.Utils.Projects;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTag";
    Intent intent;
    ImageView backIcon;
    int w, h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        w = getWindowManager().getDefaultDisplay().getWidth();
        h = getWindowManager().getDefaultDisplay().getHeight();

        backIcon = findViewById(R.id.backgrand_icon);

        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_action_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("INBOX", R.drawable.ic_action_message));
//        spaceNavigationView.showBadgeAtIndex(2, 5, getResources().getColor(R.color.badge_background_color));

        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w / 2, (int) (h / 1.5));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        backIcon.setLayoutParams(params);

        AndroidNetworking.get("http://192.168.43.109/mitra/GetData.php")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response != null) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<Projects>() {

                            }.getType();
                            Projects projects = gson.fromJson(response.toString(), type);
                            Log.d(TAG, "onResponse2: " + projects);
                        }
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
}
