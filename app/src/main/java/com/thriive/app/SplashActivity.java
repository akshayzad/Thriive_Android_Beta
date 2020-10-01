package com.thriive.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.AbdAllahAbdElFattah13.linkedinsdk.ui.LinkedInUser;
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInBuilder;
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInFromActivityBuilder;
import com.thriive.app.R;
import com.thriive.app.utilities.SharedData;

public class SplashActivity extends AppCompatActivity {
    private String CLIENT_ID = "7884jv1r7np0qe";
    private String CLIENT_SECRET = "gaWSVUjMqPu3GU09";
//
//    Client Secret: gaWSVUjMqPu3GU09
    //String REDIRECTION_URL = "http://localhost:4200/";

    private String REDIRECTION_URL = "http://localhost:4200/redirect";
    private SharedData sharedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedData = new SharedData(getApplicationContext());
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                if (sharedData.getBooleanData(SharedData.isLogged)){
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("intent_type", "FLOW");
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }

            }
        }, 2000);



    }


}