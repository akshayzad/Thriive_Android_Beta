package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {


    private LoginPOJO loginPOJO;
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
        txt_name.setText(loginPOJO.getReturnEntity().getFirstName() + " " + loginPOJO.getReturnEntity().getLastName());
        txt_profession.setText(loginPOJO.getReturnEntity().getDesignationName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_edit_profile);
        requestOptions.error(R.drawable.ic_edit_profile);
        Glide.with(this)
                .load(loginPOJO.getReturnEntity().getPicUrl())
                .apply(requestOptions)
                .into(img_profile);

    }

    @OnClick({R.id.profile, R.id.preferences, R.id.history, R.id.img_close, R.id.txt_logout})
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
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
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
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        }, 2000);

    }
}