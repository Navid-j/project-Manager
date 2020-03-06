package com.example.projectmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectmanager.Adapter.InboxRvAdapter;
import com.example.projectmanager.Model.Messages;
import com.example.projectmanager.R;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import static com.example.projectmanager.Activity.LoginActivity.HOST_NAME;
import static com.example.projectmanager.Activity.LoginActivity.USER_ID;

public class InboxActivity extends AppCompatActivity {

    Intent intent;
    ImageView backgroundIcon;
    int w, h;
    private int exit;
    private RecyclerView rvInbox;
    private ArrayList<Messages> messagesList = new ArrayList<>();
    private String TAG = "InboxActivity";
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_inbox);

        w = getWindowManager().getDefaultDisplay().getWidth();
        h = getWindowManager().getDefaultDisplay().getHeight();

        backgroundIcon = findViewById(R.id.backgrand_icon);
        rvInbox = findViewById(R.id.inbox_rv);

        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_action_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("INBOX", R.drawable.ic_action_message));
        spaceNavigationView.changeCurrentItem(1);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w / 2, (int) (h / 1.5));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        backgroundIcon.setLayoutParams(params);


        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                intent = new Intent(InboxActivity.this, SendMessageActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        intent = new Intent(InboxActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 1:
                        break;
                }

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });

        rvInbox.setLayoutManager(new LinearLayoutManager(InboxActivity.this, RecyclerView.VERTICAL, false));


        link = "mitra/GetMessageList.php?getter_id=" + USER_ID;

    }

    private void getData(String link) {
        messagesList.clear();
        AndroidNetworking.get(HOST_NAME + link)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray fullMessages = response.getJSONArray("messages");
                            JSONObject message;
                            for (int i = 0; i < fullMessages.length(); i++) {
                                message = fullMessages.getJSONObject(i);
                                Log.d(TAG, "onResponse: " + message);

                                messagesList.add(new Messages(message.getInt("id")
                                        , message.getString("messageIntro")
                                        , message.getString("address")
                                        , message.getString("producerId")));
                                Log.d(TAG, "onResponse: " + messagesList.get(i).getMessageIntro());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }
                        rvInbox.setAdapter(new InboxRvAdapter(messagesList));

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d(TAG, "onError: " + anError);
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

    @Override
    protected void onResume() {
        super.onResume();
        messagesList.clear();
        getData(link);
    }
}
