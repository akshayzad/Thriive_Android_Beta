package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ssw.linkedinmanager.dto.LinkedInAccessToken;
import com.ssw.linkedinmanager.dto.LinkedInEmailAddress;
import com.ssw.linkedinmanager.dto.LinkedInUserProfile;
import com.ssw.linkedinmanager.events.LinkedInManagerResponse;
import com.ssw.linkedinmanager.ui.LinkedInRequestManager;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.Validation;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    private String CLIENT_ID = "7884jv1r7np0qe";
    private String CLIENT_SECRET = "gaWSVUjMqPu3GU09";
//
//    Client Secret: gaWSVUjMqPu3GU09
    //String REDIRECTION_URL = "http://localhost:4200/";

    private String REDIRECTION_URL = "http://localhost:4200/redirect";

    private LinkedInRequestManager linkedInRequestManager;
    private KProgressHUD progressHUD;


    private String email = "", password = "", login_method = "";
    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife .bind(this);

        apiInterface = APIClient.getApiInterface();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
        mGoogleSignInClient.signOut();


        linkedInRequestManager = new LinkedInRequestManager(LoginActivity.this, this,
                CLIENT_ID, CLIENT_SECRET, REDIRECTION_URL, true);

    }

    boolean isValidateLogin() {
        boolean isValid = true;
        if (!Validation.validEmail(edt_email)) isValid = false;
        if (!Validation.hasText(edt_password)) isValid = false;
        return isValid;
    }


    @OnClick({R.id.btn_login, R.id.btn_google, R.id.btn_linklined})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
                linkedInRequestManager.showAuthenticateView(LinkedInRequestManager.MODE_EMAIL_ADDRESS_ONLY);
             //  signInWithLinkLined();
                break;
        }
    }

    private void getLogin() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<LoginPOJO> call = apiInterface.login(email, password, login_method);
        call.enqueue(new Callback<LoginPOJO>() {
            @Override
            public void onResponse(Call<LoginPOJO> call, Response<LoginPOJO> response) {
                if(response.isSuccessful()) {
                    LoginPOJO loginPOJO = response.body();
                    progressHUD.dismiss();
                    if (loginPOJO.getOK()) {
                        Toast.makeText(LoginActivity.this, ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onFailure(Call<LoginPOJO> call, Throwable t) {
                progressHUD.dismiss();
             //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Add this line to your existing onActivityResult() method
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
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
     //   GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            email = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            progressHUD.dismiss();
            Toast.makeText(this, ""+email, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
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
        linkedInUserProfile.getImageURL(); // user's Image URL
        linkedInUserProfile.getUserName().getFirstName().getLocalized().getEn_US(); // User's first name
        linkedInUserProfile.getUserName().getLastName().getLocalized().getEn_US(); // User's last name
        linkedInUserProfile.getUserName().getId(); // User's profile ID
        Toast.makeText(this, ""+linkedInUserProfile.getUserName(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onGetEmailAddressFailed() {

    }

    @Override
    public void onGetEmailAddressSuccess(LinkedInEmailAddress linkedInEmailAddress) {
        Toast.makeText(this, ""+linkedInEmailAddress.getEmailAddress(), Toast.LENGTH_SHORT).show();

    }
}