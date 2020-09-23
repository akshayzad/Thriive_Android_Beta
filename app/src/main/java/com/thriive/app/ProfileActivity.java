package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ssw.linkedinmanager.ui.LinkedInRequestManager;
import com.thriive.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.profile, R.id.preferences, R.id.history, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile:
                //();
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.preferences:

                Intent intent1 = new Intent(getApplicationContext(), PreferencesActivity.class);
                startActivity(intent1);
                break;

            case R.id.history:

                Intent intent2 = new Intent(getApplicationContext(), MeetingsHistoryActivity.class);
                startActivity(intent2);
                //  signInWithLinkLined();
                break;

            case R.id.img_close:
                finish();
                break;
        }
    }
}