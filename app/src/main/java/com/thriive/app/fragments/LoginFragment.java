package com.thriive.app.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.ui.linkedin_builder.LinkedInFromActivityBuilder;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.onesignal.OneSignal;
import com.thriive.app.CommonWebviewActivity;
import com.thriive.app.HomeActivity;
import com.thriive.app.LoginActivity;
import com.thriive.app.R;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.Validation;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends BottomSheetDialogFragment {

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
    @BindView(R.id.cb_remember)
    CheckBox cb_remember;

    SharedData sharedData;
    Unbinder unbinder;
    private String email = "", password = "", login_method = "", time_stamp;

    //private SharedData sharedData;
    private KProgressHUD progressHUD;
    //private LoginPOJO.ReturnEntity loginPOJO;
   private APIInterface apiInterface;
   private boolean isPasswordVisible ;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);

        sharedData = new SharedData(getActivity());
        txt_terms.setText(Html.fromHtml("<u>" + getResources().getString(R.string.terms) + "</u>"));
        txt_privacy.setText(Html.fromHtml("<u>" + getResources().getString(R.string.privacy) + "</u>"));
       // UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
      //  sharedData = new SharedData(getActivity());
        apiInterface = APIClient.getApiInterface();
        if (sharedData.getBooleanData(SharedData.IS_REMEMBER_ME)){
            cb_remember.setChecked(true);
            edt_password.setText(sharedData.getStringData(SharedData.ENTITY_PASSWORD));
            edt_email.setText(sharedData.getStringData(SharedData.ENTITY_EMAIL));
        }


        edt_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edt_password.getRight() - edt_password.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = edt_password.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            edt_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_visibility_off, 0);
                            // hide Password
                            edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordVisible = false;
                        } else  {
                            // set drawable image
                            edt_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_visible, 0);
                            // show Password
                            edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        edt_password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.SheetDialog);
    }

    boolean isValidateLogin() {
        boolean isValid = true;
        if (!Validation.validEmail(edt_email)) isValid = false;
        if (!Validation.hasText(edt_password)) isValid = false;
        return isValid;
    }



    @OnClick({R.id.btn_login, R.id.txt_forgetPassword, R.id.txt_terms, R.id.txt_privacy, R.id.img_close})
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
                    boolean is_remember = false;

                    if (cb_remember.isChecked()){
                        Log.d(TAG, "Cha");
                        is_remember = true;
                     //   Toast.makeText(getActivity(), "cheked", Toast.LENGTH_SHORT).show();

                    } else {
                        is_remember = false;
                    }
//                    dismiss();

                    ((LoginActivity)getActivity()).getLogin(email, password, "custom", is_remember);
                   // getLogin();

                }

                break;



            case  R.id.txt_privacy:

                Intent intent4 = new Intent(getActivity(), CommonWebviewActivity.class);
                intent4.putExtra("intent_type", Utility.PRIVACY_POLICY);
                startActivity(intent4);

                break;


            case R.id.txt_terms:

                Intent intent3 = new Intent(getActivity(), CommonWebviewActivity.class);
                intent3.putExtra("intent_type", Utility.TERMS);
                startActivity(intent3);

                break;

            case R.id.img_close:
                dismiss();

                break;
        }
    }

    public void dialogForgetPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
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
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getForgetPassword(sharedData.getStringData(SharedData.API_URL) + "api/AppLogin/forgot-password",email_id);
        call.enqueue(new Callback<CommonPOJO>() {
            @Override
            public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonPOJO loginPOJO = response.body();
                    progressHUD.dismiss();
                    if (loginPOJO.getOK()) {
                        Toast.makeText(getActivity(), ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), ""+loginPOJO.getMessage(), Toast.LENGTH_SHORT).show();
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


}