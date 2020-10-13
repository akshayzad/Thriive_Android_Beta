package com.thriive.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AbdAllahAbdElFattah13.linkedinsdk.ui.LinkedInUser;
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInBuilder;
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInFromActivityBuilder;
import com.bumptech.glide.Glide;
import com.thriive.app.R;
import com.thriive.app.utilities.SharedData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.imageView)
    ImageView imageView;;
    private SharedData sharedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        sharedData = new SharedData(getApplicationContext());
        Glide.with(this)
                .load(R.raw.thriive_ani)
                .into(imageView);
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                if (sharedData.getBooleanData(SharedData.isLogged)){
                    if (sharedData.getBooleanData(SharedData.isFirstVisit)){
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("intent_type", "FLOW");
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), QuickGuideActivity.class);
                        intent.putExtra("intent_type", "FLOW");
                        startActivity(intent);
                        finishAffinity();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }

            }
        }, 5000);

        //showLongToast(getResources().getString(R.string.call_message));
    }

}