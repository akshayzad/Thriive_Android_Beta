package com.thriive.app;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.BuildConfig;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;

import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.AbdAllahAbdElFattah13.linkedinsdk.ui.LinkedInUser;
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInBuilder;
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInFromActivityBuilder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.onesignal.OneSignal;
import com.ssw.linkedinmanager.dto.LinkedInAccessToken;
import com.ssw.linkedinmanager.dto.LinkedInEmailAddress;
import com.ssw.linkedinmanager.dto.LinkedInUserProfile;
import com.ssw.linkedinmanager.events.LinkedInManagerResponse;
import com.ssw.linkedinmanager.ui.LinkedInRequestManager;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.Validation;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements  LinkedInManagerResponse {

    private static final String TAG = "LoginActivity" ;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_email)
    EditText edt_email;

    private static final int GOOGLE_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;

//    86oqo8213yzpzp
//            clientid
//    clientsecret
//0GfLX8S6YeciFU8x

    private String CLIENT_ID = "86oqo8213yzpzp";
    private String CLIENT_SECRET = "0GfLX8S6YeciFU8x";
//
//    Client Secret: gaWSVUjMqPu3GU09
    //String REDIRECTION_URL = "http://localhost:4200/";

    private String REDIRECTION_URL = "http://localhost:4200/redirect";

    private LinkedInRequestManager linkedInRequestManager;
    private KProgressHUD progressHUD;


    private String email = "", password = "", login_method = "", app_ver = "", platform_ver= "", token = "", time_stamp;
    private APIInterface apiInterface;

    private SharedData sharedData;
    private  String UUID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife .bind(this);

        sharedData = new SharedData(getApplicationContext());

        apiInterface = APIClient.getApiInterface();

        getOneSignalToken();

        UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();

        Log.d(TAG, " UUID "+ UUID);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
        mGoogleSignInClient.signOut();

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                try{
                    time_stamp =""+ Utility.getTimeStamp();
                } catch (Exception e){

                }
            } else {
                TimeZone timeZone = TimeZone.getDefault();
                Log.d(TAG, "time zone "+ timeZone.getID());
                time_stamp = timeZone.getID();
            }
        } catch(Exception e){
            e.getMessage();
        }




        linkedInRequestManager = new LinkedInRequestManager(LoginActivity.this, this,
                CLIENT_ID, CLIENT_SECRET, REDIRECTION_URL, true);

        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_password.getText().length() > 0 && edt_email.getText().length() > 0){
                    btn_login.setBackground(getResources().getDrawable(R.drawable.filled_circle_terracota));
                } else {
                    btn_login.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_password.getText().length() > 0 && edt_email.getText().length() > 0){
                    btn_login.setBackground(getResources().getDrawable(R.drawable.filled_circle_terracota));
                } else {
                    btn_login.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    boolean isValidateLogin() {
        boolean isValid = true;
        if (!Validation.validEmail(edt_email)) isValid = false;
        if (!Validation.hasText(edt_password)) isValid = false;
        return isValid;
    }



    @OnClick({R.id.btn_login, R.id.btn_google, R.id.btn_linklined, R.id.txt_forgetPassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_forgetPassword:
                dialogForgetPassword();
                break;
            case R.id.btn_login:
                if (isValidateLogin()){
                    login_method = "custom";
                    email = edt_email.getText().toString();
                    password = edt_password.getText().toString();
                    //();

                    getLogin();

                }

                break;

            case R.id.btn_google:

                signInWithGoogle();

                break;

            case R.id.btn_linklined:

                login_method = "linkedin";
                LinkedInFromActivityBuilder.getInstance(LoginActivity.this)
                        .setClientID(CLIENT_ID)
                        .setClientSecret(CLIENT_SECRET)
                        .setRedirectURI(REDIRECTION_URL)
                        .authenticate(100);
             //   linkedInRequestManager.showAuthenticateView(LinkedInRequestManager.MODE_EMAIL_ADDRESS_ONLY);
             //  signInWithLinkLined();
                break;
        }
    }



    private void getOneSignalToken() {
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                if (userId != null) {
                    token = userId;
                } else {
                    getOneSignalToken();
                }
            }
        });
        Log.d("token","one signal token "+token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            if (resultCode == RESULT_OK) {
                //Successfully signed in
                LinkedInUser user = data.getParcelableExtra("social_login");
                login_method = "linkedin";
                //acessing user info
                Log.d(TAG,"LinkedInLogin" + user.getFirstName());
                Log.d(TAG,"LinkedInLogin" + user.getEmail());
                email = user.getEmail();
                getLogin();

            } else {
                if (data.getIntExtra("err_code", 0) == LinkedInBuilder.ERROR_USER_DENIED) {
                    //Handle : user denied access to account
                } else if (data.getIntExtra("err_code", 0) == LinkedInBuilder.ERROR_FAILED) {
                    //Handle : Error in API : see logcat output for details
                    Log.e("LINKEDIN ERROR", data.getStringExtra("err_message"));
                }
            }
        }
               // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        else  if (requestCode == GOOGLE_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }

    }

    private void getLogin() {
        try {
            TimeZone timeZone = TimeZone.getDefault();
            Log.d(TAG, "time zone "+ timeZone.getID());
            UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
            if (UUID  == null) {
                UUID = "";
            }progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<LoginPOJO> call = apiInterface.login(email, password, login_method, BuildConfig.VERSION_NAME,
                    ""+android.os.Build.VERSION.SDK_INT, UUID,
                    UUID, "android",  ""+timeZone.getID(), time_stamp);
            call.enqueue(new Callback<LoginPOJO>() {
                @Override
                public void onResponse(Call<LoginPOJO> call, Response<LoginPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        LoginPOJO loginPOJO = response.body();
                        progressHUD.dismiss();
                        Log.d(TAG, loginPOJO.getMessage());
                        if (loginPOJO != null){
                            if (loginPOJO.getOK()) {
                                if (loginPOJO.getReturnEntity() != null){
                                    Utility.saveLoginData(LoginActivity.this, loginPOJO.getReturnEntity());
                                    Toast.makeText(LoginActivity.this, ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                    sharedData.addBooleanData(SharedData.isLogged, true);
                                    Intent intent = new Intent(getApplicationContext(), QuickGuideActivity.class);
                                    intent.putExtra("intent_type", "FLOW");
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Log.d(TAG,  " FAIL" +  response.toString());
                            Toast.makeText(LoginActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG,  " FAIL" +  response.toString());
                        Toast.makeText(LoginActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<LoginPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                       Toast.makeText(LoginActivity.this,t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }

    }

    private void signInWithGoogle() {
        login_method = "google";
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }



    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            getData(account);
        } catch (ApiException e) {
            progressHUD.dismiss();
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
           // updateUI(null);
        }
    }

    private void getData(GoogleSignInAccount acct) {
        progressHUD.dismiss();
     //   GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
//            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
            email = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
            getLogin();

        }
    }



    public void dialogForgetPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater =  this.getLayoutInflater();
        // final View dialogView = inflater.inflate(R.layout.popup_pending_meeting, null);

        final View view1 = layoutInflater.inflate(R.layout.dialog_forget_password, null);
        builder.setView(view1);

        final AlertDialog dialogs = builder.create();
        builder.setView(view1);
        dialogs.setCancelable(false);

        ImageView img_close = view1.findViewById(R.id.img_close);
        Button   button = view1.findViewById(R.id.btn_submit);
        EditText editText = view1.findViewById(R.id.edt_email);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.validEmail(editText)){
                    dialogs.dismiss();
                    getForgetPassword(editText.getText().toString());
                } else {
                   // Toast.makeText(LoginActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                }

            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });

        dialogs.show();



    }

    private void getForgetPassword(String email_id){
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getForgetPassword(email_id);
        call.enqueue(new Callback<CommonPOJO>() {
            @Override
            public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonPOJO loginPOJO = response.body();
                    progressHUD.dismiss();
                    if (loginPOJO.getOK()) {
                        Toast.makeText(LoginActivity.this, ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onFailure(Call<CommonPOJO> call, Throwable t) {
                progressHUD.dismiss();
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onGetAccessTokenFailed() {

    }

    @Override
    public void onGetAccessTokenSuccess(LinkedInAccessToken linkedInAccessToken) {

    }

    @Override
    public void onGetCodeFailed() {

    }

    @Override
    public void onGetCodeSuccess(String code) {

    }

    @Override
    public void onGetProfileDataFailed() {

    }

    @Override
    public void onGetProfileDataSuccess(LinkedInUserProfile linkedInUserProfile) {

    }

    @Override
    public void onGetEmailAddressFailed() {

    }

    @Override
    public void onGetEmailAddressSuccess(LinkedInEmailAddress linkedInEmailAddress) {

    }
}