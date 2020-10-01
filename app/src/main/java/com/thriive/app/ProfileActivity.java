package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ssw.linkedinmanager.ui.LinkedInRequestManager;
import com.thriive.app.R;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {


    private LoginPOJO loginPOJO;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_profession)
    TextView txt_profession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        loginPOJO = Utility.getLoginData(ProfileActivity.this);
        txt_name.setText(loginPOJO.getReturnEntity().getFirstName() + " " + loginPOJO.getReturnEntity().getLastName());

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