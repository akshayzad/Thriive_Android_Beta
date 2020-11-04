package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ssw.linkedinmanager.ui.LinkedInRequestManager;
import com.thriive.app.R;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonCountryPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {


    private LoginPOJO.ReturnEntity loginPOJO;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_profession)
    TextView txt_profession;
    @BindView(R.id.img_profile)
    CircleImageView img_profile;

    private APIInterface apiInterface;
    private KProgressHUD progressHUD;

    private static final String TAG = ProfileActivity.class.getName();
    private SharedData sharedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        sharedData = new SharedData(getApplicationContext());
        apiInterface = APIClient.getApiInterface();

        loginPOJO = Utility.getLoginData(ProfileActivity.this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            loginPOJO = Utility.getLoginData(ProfileActivity.this);
            txt_name.setText(loginPOJO.getFirstName() + " " + loginPOJO.getLastName());
            txt_profession.setText(loginPOJO.getDesignationName());

            if (loginPOJO.getPicUrl().equals("")){
                Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_medium);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(getResources().getColor( R.color.terracota))
                        .useFont(typeface)
                        .fontSize(40) /* size in px */
                        .bold()
                        .toUpperCase()
                        .width(120)  // width in px
                        .height(120) // height in px
                        .endConfig()
                        .buildRect(Utility.getInitialsName(loginPOJO.getFirstName() + " " + loginPOJO.getLastName()) ,
                                getResources().getColor( R.color.whiteTwo));

                img_profile.setImageDrawable(drawable);
            } else {
                img_profile.setMinimumWidth(60);
                img_profile.setMaxHeight(60);
                img_profile.setMinimumHeight(60);
                img_profile.setMaxWidth(60);
                Glide.with(this)
                        .load(loginPOJO.getPicUrl())
                        .into(img_profile);
            }
        } catch (Exception e){
            e.getMessage();
        }


    }

    @OnClick({R.id.profile, R.id.preferences, R.id.history, R.id.img_close, R.id.txt_logout,R.id.service, R.id.policy})
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

            case R.id.txt_logout:
                logoutApp();
                break;

            case R.id.service:
                Intent intent3 = new Intent(getApplicationContext(), CommonWebviewActivity.class);
                intent3.putExtra("intent_type", Utility.TERMS);
                startActivity(intent3);
                break;

            case R.id.policy:
                Intent intent4 = new Intent(getApplicationContext(), CommonWebviewActivity.class);
                intent4.putExtra("intent_type", Utility.PRIVACY_POLICY);
                startActivity(intent4);
                break;

        }
    }

    public void logoutApp() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_logout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(ProfileActivity.this,R.style.SheetDialog);


        ImageView img_close = dialogView.findViewById(R.id.img_close);
        Button btn_yes = dialogView.findViewById(R.id.btn_yes);
        Button btn_no = dialogView.findViewById(R.id.btn_no);
        dialog.setContentView(dialogView);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getLogoutApp();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //   ratingDialog();
            }
        });
        dialog.show();
    }

    private void getLogoutApp() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.getLogout(sharedData.getStringData(SharedData.API_URL)
                    + "api/AppLogin/logout", loginPOJO.getActiveToken(), loginPOJO.getPrimaryLoginKey());
            call.enqueue(new Callback<CommonPOJO>() {
                @Override
                public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonPOJO reasonPOJO = response.body();
                        //   progressHUD.dismiss();
                        try {
                            if (reasonPOJO != null){
                                Log.d(TAG,""+reasonPOJO.getMessage());
                                if (reasonPOJO.getOK()) {
                                    sharedData.addBooleanData(SharedData.isFirstVisit, false);
                                    sharedData.addBooleanData(SharedData.isLogged, false);
                                    sharedData.clearPref(getApplicationContext());
                                    Utility.clearLogin(getApplicationContext());
                                    Utility.clearMeetingDetails(getApplicationContext());
                                    Handler mHandler = new Handler();
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressHUD.dismiss();
                                          //  Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        }
                                    }, 2000);
                                } else {
                                    Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (Exception e){
                            e.getMessage();
                        }

                    }
                }
                @Override
                public void onFailure(Call<CommonPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    // Toast.makeText(ProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }





    }
}