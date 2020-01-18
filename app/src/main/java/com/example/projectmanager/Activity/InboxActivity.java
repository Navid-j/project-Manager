package com.example.projectmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.projectmanager.R;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class InboxActivity extends AppCompatActivity {

    Intent intent;
    ImageView backIcon;
    int w, h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        w = getWindowManager().getDefaultDisplay().getWidth();
        h = getWindowManager().getDefaultDisplay().getHeight();

        backIcon = findViewById(R.id.backgrand_icon);

        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_action_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("INBOX", R.drawable.ic_action_message));
        spaceNavigationView.setSelected(true);
        spaceNavigationView.isSelected();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w / 2, (int) (h / 1.5));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        backIcon.setLayoutParams(params);

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                intent = new Intent(InboxActivity.this, AddActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex == 0) {
                    intent = new Intent(InboxActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
//                    intent = new Intent(InboxActivity.this, InboxActivity.class);
//                    startActivity(intent);
                }

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });
    }
}
