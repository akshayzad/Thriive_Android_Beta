package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.SharedData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginMessageActivity extends AppCompatActivity {

    @BindView(R.id.txt_message)
    TextView txt_message;

    @BindView(R.id.btn_close)
    Button btn_close;
    @BindView(R.id.btn_signup)
    Button btn_signup;
    @BindView(R.id.txt_name)
    TextView txt_name;

    @BindView(R.id.img_close)
    ImageView img_close;

    private SharedData sharedData;
    public static LoginPOJO.LandData landDataMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_message);
        ButterKnife.bind(this);
        sharedData = new SharedData(getApplicationContext());

//        if(flag_new_user)
//        {
//            return 0;
//        }
//        else if(flag_under_review)
//        {
//            return 1;
//        }
//        else if(flag_waitlisted)
//        {
//            return 2;
//        }
//        else if(flag_rejected)
//        {
//            return 3;
//        }
//        else if(flag_approved)
//        {
//            return 4;
//        }
//        else
//        {
//            return -1;
//        }

        if (landDataMessage.getFlagStatus() == 0){
          //  txt_message.setText(Html.fromHtml(getApplicationContext().getResources().getString(R.string.land_message_flag_new_user)));
            txt_message.setText(Html.fromHtml("Thriive is an invite-only platform offering high-quality, " +
                    "actionable meetings with the people you need, " +
                    "when you need them. We allow users on the platform only after reviewing their profile.<br/><br/> " +
                    "If you’ve previously registered, please go back and login using the same credentials. <br/><br/><b>OR,</b> please continue to sign-up on Thriive"));
        } else if (landDataMessage.getFlagStatus() == 2){
            //txt_message.setText(Html.fromHtml(getApplicationContext().getResources().getString(R.string.land_message_flag_waitlist)));
            txt_message.setText(Html.fromHtml("You’ve been added to the Thriive’s<b>" +
                    " <font color='#CE593F'> WAIT LIST! </font> </b><br/><br/>Thank you for your interest in joining Thriive. " +
                    "We are currently overwhelmed by the number of join requests to our platform.<br/><br/>Stay tuned for more updates from Thriive!"));

        } else if (landDataMessage.getFlagStatus() == 3){
           // txt_message.setText(Html.fromHtml(getApplicationContext().getResources().getString(R.string.land_message_flag_rejected)));
            txt_message.setText(Html.fromHtml("Thank you for your interest in joining Thriive.<br/><br/><font color='#CE593F'> " +
                    "We regret to inform you that we are unable to accept your join request at this time. </font><br/><br/> " +
                    "We have consciously kept a minimum level of expertise and experience that is required to join Thriive.<br/><br/> " +
                    "We will definitely get back to you when we open Thriive to broader membership."));
        } else if (landDataMessage.getFlagStatus() == 1){
          //  txt_message.setText(Html.fromHtml(getApplicationContext().getResources().getString(R.string.land_message_flag_under_review)));
            txt_message.setText(Html.fromHtml("Thank you for your interest in joining Thriive.<br/><br/>" +
                    "<font color='#CE593F'>We are currently reviewing your profile.</font><br/><br/>You will hear from us very soon."));
        }
        txt_name.setText(landDataMessage.getFirstName());
      //        "show_close_button": false,
//                "show_signup_button": true,
        if (landDataMessage.getShowCloseButton() && landDataMessage.getShowSignupButton()){
            btn_close.setVisibility(View.GONE);
            btn_signup.setVisibility(View.VISIBLE);
            img_close.setVisibility(View.VISIBLE);
        } else if (landDataMessage.getShowSignupButton()){
            btn_signup.setVisibility(View.VISIBLE);
            btn_close.setVisibility(View.GONE);
            img_close.setVisibility(View.VISIBLE);
        } else if (landDataMessage.getShowCloseButton()){
            btn_signup.setVisibility(View.GONE);
            btn_close.setVisibility(View.VISIBLE);
            img_close.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.btn_close, R.id.btn_signup, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;

            case R.id.btn_signup:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW).
                            setData(Uri.parse(sharedData.getStringData(SharedData.REGISTER_URL)));
                    startActivity(intent);
                } catch (Exception e){
                    e.getMessage();
                }
                //  dialog_prefrences(getResources().getString(R.string.expertise));
                break;

            case  R.id.img_close:
                finish();
                break;

        }
    }
}