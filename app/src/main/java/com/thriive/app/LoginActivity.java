package com.thriive.app;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.BuildConfig;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;

import android.text.Html;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.ui.LinkedInUser;
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.ui.linkedin_builder.LinkedInBuilder;
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.ui.linkedin_builder.LinkedInFromActivityBuilder;
import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.onesignal.OneSignal;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.fragments.LoginFragment;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.models.BaseUrlPOJo;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.Validation;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.viewPager_login)
    ViewPager viewPager;
    @BindView(R.id.layout_dot)
    LinearLayout dotsLayout;

    private TextView[] dots;
    private int currentPage = 0;
    private Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;

    private int[] imageArray;
    private MyViewPagerAdapter myViewPagerAdapter;


    private static final String TAG = "LoginActivity" ;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.txt_terms)
    TextView txt_terms;
    @BindView(R.id.txt_privacy)
    TextView txt_privacy;

    private static final int GOOGLE_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;

//    86oqo8213yzpzp
//            clientid
//    clientsecret
//0GfLX8S6YeciFU8x

    private String CLIENT_ID = "7884jv1r7np0qe";
    private String CLIENT_SECRET = "gaWSVUjMqPu3GU09";
//
//    Client Secret: gaWSVUjMqPu3GU09
    //String REDIRECTION_URL = "http://localhost:4200/";

    private String REDIRECTION_URL = "http://localhost:4200/redirect";
    private KProgressHUD progressHUD;


    private String email = "", password = "", login_method = "", time_stamp, first_name = "", last_name = "";
    private APIInterface apiInterface;

    private SharedData sharedData;
    private  String UUID = "";
    CleverTapAPI clevertapDefaultInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife .bind(this);

        sharedData = new SharedData(getApplicationContext());

        apiInterface = APIClient.getApiInterface();

        txt_terms.setText(Html.fromHtml("<u>" + getResources().getString(R.string.terms) + "</u>"));
        txt_privacy.setText(Html.fromHtml("<u>" + getResources().getString(R.string.privacy) + "</u>"));
        UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();

        Log.d(TAG, " UUID "+ UUID);

        imageArray = new int[]{
                R.drawable.login_intro_one,
                R.drawable.login_intro_two,
                R.drawable.login_intro_three,
                R.drawable.login_intro_four,
                R.drawable.login_intro_five};
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        addBottomDots(0);


        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == imageArray.length - 0) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);

            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());


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


        getBaseUrl();

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

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            currentPage = position;
            addBottomDots(position);
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

    };

    public void getBaseUrl() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<BaseUrlPOJo> call = apiInterface.GetBaseUrl("application/json",
                    ""+Utility.BASEURL, Utility.getJsonEncode(LoginActivity.this));
            call.enqueue(new Callback<BaseUrlPOJo>() {
                @Override
                public void onResponse(Call<BaseUrlPOJo> call, Response<BaseUrlPOJo> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        BaseUrlPOJo urlPOJo = response.body();
                        try {
                            if (urlPOJo != null) {
                                Log.d(TAG, "" + urlPOJo.getMessage());
                                progressHUD.dismiss();
                                if (urlPOJo.getOK()) {
                                    sharedData.addStringData(SharedData.API_URL, urlPOJo.getApiUrl());
                                    sharedData.addStringData(SharedData.REGISTER_URL, urlPOJo.getRegisterUrl());
                                    Log.d(TAG, "Base url " + urlPOJo.getApiUrl() + " "+  urlPOJo.getRegisterUrl());
                                    if (urlPOJo.getEnv().equals("Test")) {
                                        Toast.makeText(LoginActivity.this, "Test Environment", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "" + urlPOJo.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }

                    } else {
                        progressHUD.dismiss();
                        Log.d(TAG, " FAIL" + response.toString());
                        Toast.makeText(LoginActivity.this, "" + response.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<BaseUrlPOJo> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[imageArray.length];

        int[] colorsActive = getResources().getIntArray(R.array.view_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.view_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dots[i].setPadding(5, 0, 5, 0);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    boolean isValidateLogin() {
        boolean isValid = true;
        if (!Validation.validEmail(edt_email)) isValid = false;
        if (!Validation.hasText(edt_password)) isValid = false;
        return isValid;
    }



    @OnClick({R.id.btn_login, R.id.btn_google, R.id.btn_linkedin, R.id.txt_forgetPassword, R.id.txt_terms,
            R.id.txt_privacy, R.id.btn_custom, R.id.btn_register})
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

                    getLogin(email, password, login_method);

                }

                break;

            case R.id.btn_google:

                signInWithGoogle();

                break;

            case R.id.btn_linkedin:

                login_method = "linkedin";
                LinkedInFromActivityBuilder.getInstance(LoginActivity.this)
                        .setClientID(CLIENT_ID)
                        .setClientSecret(CLIENT_SECRET)
                        .setRedirectURI(REDIRECTION_URL)
                        .authenticate(100);
             //   linkedInRequestManager.showAuthenticateView(LinkedInRequestManager.MODE_EMAIL_ADDRESS_ONLY);
             //  signInWithLinkLined();
                break;

            case  R.id.txt_privacy:

                Intent intent4 = new Intent(getApplicationContext(), CommonWebviewActivity.class);
                intent4.putExtra("intent_type", Utility.PRIVACY_POLICY);
                startActivity(intent4);

                break;


            case R.id.txt_terms:

                Intent intent3 = new Intent(getApplicationContext(), CommonWebviewActivity.class);
                intent3.putExtra("intent_type", Utility.TERMS);
                startActivity(intent3);

                break;

            case R.id.btn_custom:

                LoginFragment loginFragment =
                        (LoginFragment) LoginFragment.newInstance();
                loginFragment.show(getSupportFragmentManager(), "LoginFragment");

                break;


            case R.id.btn_register:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW).
                            setData(Uri.parse(sharedData.getStringData(SharedData.REGISTER_URL)));
                    startActivity(intent);
                } catch (Exception e){
                    e.getMessage();
                }
//                Intent intent = new Intent(getApplicationContext(), CommonWebviewActivity.class);
//                intent.putExtra("intent_type", Utility.REGISTER);
//                startActivity(intent);
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            if (resultCode == RESULT_OK) {
                //Successfully signed in
                LinkedInUser user = data.getParcelableExtra("social_login");
                login_method = "linkedin";
                first_name = ""+user.getFirstName();
                last_name = ""+user.getLastName();
                //acessing user info
                Log.d(TAG,"LinkedInLogin" + user.getFirstName());
                Log.d(TAG,"LinkedInLogin" + user.getEmail());
                email = user.getEmail();
                getLogin(email, "" , login_method);

            } else {
                Log.d("LINKEDIN ERROR", data.getStringExtra("err_message"));
                if (data.getIntExtra("err_code", 0) == LinkedInBuilder.ERROR_USER_DENIED) {
                    //Handle : user denied access to account
                } else if (data.getIntExtra("err_code", 0) == LinkedInBuilder.ERROR_FAILED) {
                    //Handle : Error in API : see logcat output for details
                    Log.e("LINKEDIN ERROR", data.getStringExtra("err_message"));
                } else {

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


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.view_pager_login, container, false);
            container.addView(view);

            ImageView textView = view.findViewById(R.id.viewPager_image);
            Glide.with(view.getContext())
                    .load(imageArray[position])
                    .into(textView);

            return view;
        }

        @Override
        public int getCount() {
            return imageArray.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }



    public void getLogin(String l_email, String l_password, String l_login_method) {
        try {
            TimeZone timeZone = TimeZone.getDefault();
            Log.d(TAG, "time zone "+ timeZone.getID());
            UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
            if (UUID  == null) {
                UUID = "";
            }
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<LoginPOJO> call = apiInterface.login(sharedData.getStringData(SharedData.API_URL)
                            + "api/AppLogin/app-login",l_email, l_password, l_login_method, BuildConfig.VERSION_NAME,
                    ""+android.os.Build.VERSION.SDK_INT, UUID,
                    UUID, "android",  ""+timeZone.getID(),
                    time_stamp, first_name, last_name, l_email);
            call.enqueue(new Callback<LoginPOJO>() {
                @Override
                public void onResponse(Call<LoginPOJO> call, Response<LoginPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        LoginPOJO loginPOJO = response.body();
                        progressHUD.dismiss();
                        try {
                            if (loginPOJO != null){
                                Log.d(TAG, ""+loginPOJO.getMessage());
                                if (loginPOJO.getOK()) {
                                    if (loginPOJO.getReturnEntity() != null){
                                        Utility.saveLoginData(LoginActivity.this, loginPOJO.getReturnEntity());


                                        HashMap<String, Object> profile = new HashMap<String, Object>();

                                        profile.put("Name", loginPOJO.getReturnEntity().getEntityName());
                                        profile.put("Identity", loginPOJO.getReturnEntity().getRowcode());
                                        profile.put("Email", loginPOJO.getReturnEntity().getEmailId());
                                        profile.put("Platform", "android");
                                        profile.put("Type", l_login_method);
                                        // clevertapDefaultInstance.pushProfile(profileUpdate);
                                        clevertapDefaultInstance.onUserLogin(profile);


                                        HashMap<String, Object> loginEvent = new HashMap<String, Object>();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                        String currentDateandTime = sdf.format(new Date());
                                        loginEvent.put("user_id", loginPOJO.getReturnEntity().getEntityId());
                                  //      loginEvent.put("time_stamp", currentDateandTime);
                                        clevertapDefaultInstance.pushEvent(Utility.User_Profile,loginEvent);


                                     //   Toast.makeText(LoginActivity.this, ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                        sharedData.addBooleanData(SharedData.isLogged, true);
                                        Intent intent = new Intent(getApplicationContext(), QuickGuideActivity.class);
                                        intent.putExtra("intent_type", "FLOW");
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    if (loginPOJO.getLandData() != null){
                                        LoginPOJO.LandData landData = loginPOJO.getLandData();
                                        if (landData.getShowLandingPage()) {
                                            Intent intent = new Intent(LoginActivity.this, LoginMessageActivity.class);
                                            LoginMessageActivity.landDataMessage = landData;
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else {
                                Log.d(TAG,  " FAIL" +  response.toString());
                                Toast.makeText(LoginActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                         //   progressHUD.dismiss();
                            e.getMessage();
                           // Toast.makeText(LoginActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d(TAG,  " FAIL" +  response.toString());
                        Toast.makeText(LoginActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<LoginPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    //Log.d
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
            first_name = ""+acct.getGivenName();
            last_name = ""+acct.getFamilyName();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
            login_method = "google";
            Log.d(TAG, email +" dn " + acct.getDisplayName() + " pgn " + acct.getGivenName() + " fn " + acct.getFamilyName());
            getLogin(email, "", login_method);

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
        Call<CommonPOJO> call = apiInterface.getForgetPassword(sharedData.getStringData(SharedData.API_URL) + "api/AppLogin/forgot-password", email_id);
        call.enqueue(new Callback<CommonPOJO>() {
            @Override
            public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonPOJO loginPOJO = response.body();
                    progressHUD.dismiss();
                    if (loginPOJO.getOK()) {
                       // Toast.makeText(LoginActivity.this, ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                        showCustomToast(""+loginPOJO.getMessage());
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
    private void showCustomToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast=Toast.makeText(getApplicationContext(),msg ,Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,20,20);
                View view=toast.getView();
                TextView view1=(TextView)view.findViewById(android.R.id.message);
                view1.setPadding(10,10,10,10);
                view1.setTextColor(Color.WHITE);
                view.setBackgroundResource(R.drawable.filled_circle_terracota);
                toast.show();

            }
        });
    }

}