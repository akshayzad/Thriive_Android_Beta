package com.thriive.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AbdAllahAbdElFattah13.linkedinsdk.ui.LinkedInUser;
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInBuilder;
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInFromActivityBuilder;
import com.bumptech.glide.Glide;
import com.onesignal.OneSignal;
import com.thriive.app.R;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.BaseUrlPOJo;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.Validation;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.imageView)
    ImageView imageView;;
    private SharedData sharedData;
    private APIInterface apiInterface;
    public static final  String  TAG = SplashActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        sharedData = new SharedData(getApplicationContext());
        apiInterface = APIClient.getApiInterface();
        Glide.with(this)
                .load(R.raw.thriive_ani)
                .into(imageView);
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            public void run() {
//
//
//            }
//        }, 5000);
        getBaseUrl();
        //showLongToast(getResources().getString(R.string.call_message));
    }

    public void getBaseUrl() {
        try {
            Call<BaseUrlPOJo> call = apiInterface.GetBaseUrl("application/json", ""+Utility.BASEURL,
                    Utility.getJsonEncode(SplashActivity.this));
            call.enqueue(new Callback<BaseUrlPOJo>() {
                @Override
                public void onResponse(Call<BaseUrlPOJo> call, Response<BaseUrlPOJo> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        BaseUrlPOJo urlPOJo = response.body();
                        try {
                            if (urlPOJo != null) {
                                Log.d(TAG, "" + urlPOJo.getMessage());
                                if (urlPOJo.getOK()) {
                                    sharedData.addStringData(SharedData.API_URL, urlPOJo.getApiUrl());
                                    sharedData.addStringData(SharedData.REGISTER_URL, urlPOJo.getRegisterUrl());
                                    PackageManager manager = getPackageManager();
                                    PackageInfo info = null;
                                    try {
                                        info = manager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
                                    } catch (PackageManager.NameNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    if (urlPOJo.getAndroidVersionCode() > info.versionCode){
                                        dialogAppUpdate();
                                    } else {
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

                                    Log.d(TAG, "Base url " + urlPOJo.getApiUrl());
                                    if (urlPOJo.getEnv().equals("Test")) {
                                        Toast.makeText(SplashActivity.this, "Test Environment", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(SplashActivity.this, "" + urlPOJo.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }

                    } else {
                        Log.d(TAG, " FAIL" + response.toString());
                        Toast.makeText(SplashActivity.this, "" + response.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<BaseUrlPOJo> call, Throwable t) {
                    Toast.makeText(SplashActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void dialogAppUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater =  this.getLayoutInflater();
        // final View dialogView = inflater.inflate(R.layout.popup_pending_meeting, null);

        final View view1 = layoutInflater.inflate(R.layout.dialog_app_update, null);
        builder.setView(view1);

        final AlertDialog dialogs = builder.create();
        builder.setView(view1);
        dialogs.setCancelable(false);

        TextView label_close = view1.findViewById(R.id.label_close);
        TextView label_update = view1.findViewById(R.id.label_update);

        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                finish();
            }
        });

        label_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="+appPackageName)));
                }
            }
        });

        dialogs.show();

    }
}