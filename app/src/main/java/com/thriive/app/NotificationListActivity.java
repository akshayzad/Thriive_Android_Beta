package com.thriive.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ssw.linkedinmanager.ui.LinkedInRequestManager;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.ExpertiseAdapter;
import com.thriive.app.adapters.PendingNotificationAdapter;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationListActivity extends AppCompatActivity {

    @BindView(R.id.rv_notification)
    RecyclerView rv_notification;
    private ArrayList<CommonRequesterPOJO>arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        ButterKnife.bind(this);

        arrayList.add(new CommonRequesterPOJO());
        rv_notification.setAdapter(new PendingNotificationAdapter(NotificationListActivity.this, arrayList));
    }


    public void detailsMeeting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationListActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_meeting_request_accept, null);
        RecyclerView rv_experience = view1.findViewById(R.id.rv_experience);
        RecyclerView rv_expertise = view1.findViewById(R.id.rv_expertise);
        ImageView img_close = view1.findViewById(R.id.img_close);

        ImageButton accept = view1.findViewById(R.id.img_accept);
        ImageButton decline = view1.findViewById(R.id.img_decline);


        //    tv_msg.setText("Session Added Successfully.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(true);
        ArrayList<CommonRequesterPOJO> arrayList   = new ArrayList<>();
        arrayList.add(new CommonRequesterPOJO("Expert"));
        arrayList.add(new CommonRequesterPOJO("Data Analyst"));
        arrayList.add(new CommonRequesterPOJO("Marketing"));
        arrayList.add(new CommonRequesterPOJO("Business"));
        arrayList.add(new CommonRequesterPOJO("Data Science"));
        FlexboxLayoutManager manager = new FlexboxLayoutManager(NotificationListActivity.this);
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setJustifyContent(JustifyContent.CENTER);
        rv_experience.setLayoutManager(manager );
        rv_experience.setAdapter(new ExperienceAdapter(NotificationListActivity.this, arrayList));

        FlexboxLayoutManager manager1 = new FlexboxLayoutManager(NotificationListActivity.this);
        manager1.setFlexWrap(FlexWrap.WRAP);
        manager1.setJustifyContent(JustifyContent.CENTER);
        rv_expertise.setLayoutManager(manager1 );
       // rv_expertise.setLayoutManager(new FlexboxLayoutManager(NotificationListActivity.this) );
        rv_expertise.setAdapter(new ExpertiseAdapter(NotificationListActivity.this, arrayList));
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
             //   ratingDialog();
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                meetingCancel();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
              //  selectDate();
            }
        });
        dialogs.show();
    }
    @OnClick({R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;


        }
    }



    public void meetingCancel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationListActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_cancel_meeting, null);

        Button accept = view1.findViewById(R.id.txt_10);
        Button decline = view1.findViewById(R.id.txt_11);
        ImageView img_close = view1.findViewById(R.id.img_close);


        //    tv_msg.setText("Session Added Successfully.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(true);

        // rv_expertise.setLayoutManager(new FlexboxLayoutManager(NotificationListActivity.this) );
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                //   ratingDialog();
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                meetingCancelConfirmation();
            }
        });
        dialogs.show();
    }

    private void meetingCancelConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationListActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_meeting_cancel_alert, null);

        Button btn_TimeMatch = view1.findViewById(R.id.btn_TimeMatch);
        Button btm_noTime = view1.findViewById(R.id.btm_noTime);
        ImageView img_close = view1.findViewById(R.id.img_close);


        //    tv_msg.setText("Session Added Successfully.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(true);

        // rv_expertise.setLayoutManager(new FlexboxLayoutManager(NotificationListActivity.this) );
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                //   ratingDialog();
            }
        });
        btn_TimeMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        btn_TimeMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                //  meetingCancelConfirmation();
            }
        });
        dialogs.show();

    }





}