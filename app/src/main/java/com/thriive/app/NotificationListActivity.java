package com.thriive.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.ExpertiseAdapter;
import com.thriive.app.adapters.PendingNotificationAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.models.CommonEntitySlotsPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends AppCompatActivity {

    @BindView(R.id.rv_notification)
    RecyclerView rv_notification;
    private ArrayList<PendingMeetingRequestPOJO>arrayList = new ArrayList<>();

    private APIInterface apiInterface;
    private SharedData sharedData;
    private LoginPOJO.ReturnEntity loginPOJO;
    private KProgressHUD progressHUD;

    String meetingCode ="", startTime ="", endTime ="", cancelReason = "", selectedDate = "";

    public static String TAG = NotificationListActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        ButterKnife.bind(this);

        apiInterface = APIClient.getApiInterface();
        sharedData = new SharedData(getApplicationContext());
        loginPOJO  = Utility.getLoginData(getApplicationContext());

        if (getIntent().getStringExtra("intent_type").equals("NOTI")){
            getMeetingRequestById();

        }


        //getMeetingRequest();
    }

    private void getMeetingRequestById() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonMeetingPOJO> call = apiInterface.getMeetingById(loginPOJO.getActiveToken(),
                    getIntent().getStringExtra("meeting_id"));
            call.enqueue(new Callback<CommonMeetingPOJO>() {
                @Override
                public void onResponse(Call<CommonMeetingPOJO> call, Response<CommonMeetingPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        progressHUD.dismiss();
                        CommonMeetingPOJO pojo = response.body();
                        Log.d(TAG,""+pojo.getMessage());
                        if (pojo != null){
                            if (pojo.getOK()) {
                                if (pojo.getMeetingObject() != null){

                                    detailsMeeting(pojo.getMeetingObject());
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Failure "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonMeetingPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(getApplicationContext(), "Getting Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getStringExtra("intent_type").equals("FLOW")){
            getMeetingRequest();
        }

    }

    private void getMeetingRequest() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonMeetingListPOJO> call = apiInterface.getPendingRequest(loginPOJO.getActiveToken(),
                    loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonMeetingListPOJO>() {
                @Override
                public void onResponse(Call<CommonMeetingListPOJO> call, Response<CommonMeetingListPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonMeetingListPOJO reasonPOJO = response.body();
                        progressHUD.dismiss();
                        Log.d(TAG,""+reasonPOJO.getMessage());
                        if (reasonPOJO.getOK()) {
                            if (reasonPOJO.getMeetingList() != null){
                                //    Toast.makeText(getApplicationContext(), "Success "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                rv_notification.setAdapter(new PendingNotificationAdapter(NotificationListActivity.this,
                                        reasonPOJO.getMeetingList()));
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Failure "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }
                @Override
                public void onFailure(Call<CommonMeetingListPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(NotificationListActivity.this, "Getting Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }


    }

    private void getAcceptMeeting() {
        try {
            startTime = Utility.ConvertUserTimezoneToUTC(startTime);
            endTime  = Utility.ConvertUserTimezoneToUTC(endTime);
            Log.d(TAG, "Accept meeting start" + startTime + " end  "+ endTime );
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.getAcceptMeeting(loginPOJO.getActiveToken(),
                    meetingCode, loginPOJO.getRowcode(), startTime, endTime);
            call.enqueue(new Callback<CommonPOJO>() {
                @Override
                public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonPOJO reasonPOJO = response.body();
                        progressHUD.dismiss();
                        Log.d(TAG,""+reasonPOJO.getMessage());
                        if (reasonPOJO.getOK()) {
                            Toast.makeText(getApplicationContext(), "Success "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();

                            successDialog();

                        } else {
                            Toast.makeText(getApplicationContext(), "Failure "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }
                @Override
                public void onFailure(Call<CommonPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(NotificationListActivity.this, "Getting Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }


    }

    public void getDeclineMeeting(String meetingCode) {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.getRejectMeeting(loginPOJO.getActiveToken(),
                    meetingCode, loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonPOJO>() {
                @Override
                public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonPOJO reasonPOJO = response.body();
                        progressHUD.dismiss();
                        Log.d(TAG,""+reasonPOJO.getMessage());
                        if (reasonPOJO != null){
                            if (reasonPOJO.getOK()) {
                                Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();

                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), "Failure "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }


                    }
                }
                @Override
                public void onFailure(Call<CommonPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(NotificationListActivity.this, "Getting Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }


    }

    public void detailsMeeting(CommonMeetingListPOJO.MeetingListPOJO meetingListPOJO) {
        try {
            meetingCode = meetingListPOJO.getMeetingCode();
            startTime = meetingListPOJO.getPlanStartTime();
            endTime = meetingListPOJO.getPlanEndTime();
        } catch (Exception e ){
            e.getMessage();
        }
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(NotificationListActivity.this, R.style.SheetDialog);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            final View view1 = layoutInflater.inflate(R.layout.dialog_meeting_request_accept, null);
            RecyclerView rv_experience = view1.findViewById(R.id.rv_experience);
            RecyclerView rv_expertise = view1.findViewById(R.id.rv_expertise);
            ImageView img_close = view1.findViewById(R.id.img_close);
            ImageButton accept = view1.findViewById(R.id.img_accept);
            ImageButton decline = view1.findViewById(R.id.img_decline);
            TextView txt_objective = view1.findViewById(R.id.txt_objective);
            TextView txt_reason  = view1.findViewById(R.id.txt_reason);
            FlexboxLayout layout_tags = view1.findViewById(R.id.layout_tags);
            TextView txt_requestor = view1.findViewById(R.id.txt_requestor);
            //    tv_msg.setText("Session Added Successfully.");
            builder.setView(view1);
            final AlertDialog dialogs = builder.create();
            dialogs.setCancelable(true);

            txt_reason.setText("Meeting for "+meetingListPOJO.getMeetingReason());
            try {
                if (meetingListPOJO.getRequestorDesignationTags().size() == 0){
                    txt_objective.setText("");
                } else {
                    txt_objective.setText(meetingListPOJO.getRequestorDesignationTags().get(0));
                }
            } catch (Exception e){
                e.getMessage();
            }


            String[] splited = meetingListPOJO.getRequestorName().trim().split("\\s+");
            StringBuilder s1 = new StringBuilder();
            try {
                String split_one=splited[0];
                for (int i = 0; i < split_one.length(); i++){
                    if (i == 0){
                        s1.append(split_one.charAt(i));
                    } else {
                        s1.append("X");
                    }
                }
            } catch (Exception e){
                e.getMessage();
            }


            StringBuilder s2 = new StringBuilder();
            try {
                String split_second=splited[1];
                for (int i = 0; i < split_second.length(); i++){
                    if (i == 0){
                        s2.append(split_second.charAt(i));
                    } else {
                        s2.append("X");
                    }
                }
            } catch (Exception e){
                e.getMessage();
            }
            txt_requestor.setText(s1 + "  " + s2);

            FlexboxLayoutManager manager = new FlexboxLayoutManager(NotificationListActivity.this);
            manager.setFlexWrap(FlexWrap.WRAP);
            manager.setJustifyContent(JustifyContent.CENTER);
            rv_experience.setLayoutManager(manager );
            ArrayList<String> array = new ArrayList<>();
            array.addAll(meetingListPOJO.getRequestorObjectiveTags());
            array.addAll(meetingListPOJO.getRequestorDesignationTags());
            rv_experience.setAdapter(new ExperienceAdapter(NotificationListActivity.this,array));

            FlexboxLayoutManager manager1 = new FlexboxLayoutManager(NotificationListActivity.this);
            manager1.setFlexWrap(FlexWrap.WRAP);
            manager1.setJustifyContent(JustifyContent.CENTER);
            rv_expertise.setLayoutManager(manager1 );
            ArrayList<String> array1 = new ArrayList<>();
            array1.addAll(meetingListPOJO.getRequestorDomainTags());
            array1.addAll(meetingListPOJO.getRequestorSubDomainTags());
            // rv_expertise.setLayoutManager(new FlexboxLayoutManager(NotificationListActivity.this) );
            rv_expertise.setAdapter(new ExpertiseAdapter(NotificationListActivity.this, array1));
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getIntent().getStringExtra("intent_type").equals("NOTI")){
                        dialogs.dismiss();
                        getMeetingRequest();
                    } else {
                        dialogs.dismiss();
                    }
                    //   ratingDialog();
                }
            });
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogs.dismiss();
                    getDeclineMeeting(meetingCode);
                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogs.dismiss();
                    getMeetingSlote();


                    //s successDialog();
                }
            });
            dialogs.show();
        } catch(Exception e){
            e.getMessage();
        }

    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent i = new Intent(NotificationListActivity.this, HomeActivity.class);
            i.putExtra("intent_type", "FLOW");
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void getMeetingSlote() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonEntitySlotsPOJO> call = apiInterface.getEntitySlots(loginPOJO.getActiveToken(),
                    loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonEntitySlotsPOJO>() {
                @SuppressLint("NewApi")
                @Override
                public void onResponse(Call<CommonEntitySlotsPOJO> call, Response<CommonEntitySlotsPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonEntitySlotsPOJO reasonPOJO = response.body();
                        progressHUD.dismiss();
                        Log.d(TAG,""+reasonPOJO.getMessage());
                        if (reasonPOJO != null){
                            if (reasonPOJO.getOK()) {
                                //.makeText(getApplicationContext(), "Success "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                meetingAvailability(reasonPOJO.getEntitySlotList());

                            } else {
                                Toast.makeText(getApplicationContext(), "Failure "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonEntitySlotsPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(NotificationListActivity.this, "Getting Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void meetingAvailability(List<CommonEntitySlotsPOJO.EntitySlotList> entitySlotList) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_availability, null);
        BottomSheetDialog dialog = new BottomSheetDialog(NotificationListActivity.this, R.style.SheetDialog);

        Button btn_confirm = dialogView.findViewById(R.id.btn_confirm);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        LinearLayout edit = dialogView.findViewById(R.id.edit);
        LinearLayout layout1 = dialogView.findViewById(R.id.layout1);
        LinearLayout layout2 = dialogView.findViewById(R.id.layout2);
        LinearLayout layout3 = dialogView.findViewById(R.id.layout3);



        ImageView img_date1 = dialogView.findViewById(R.id.img_date1);
        ImageView img_date2 = dialogView.findViewById(R.id.img_date2);
        ImageView img_date3 = dialogView.findViewById(R.id.img_date3);
        ImageView img_time1 = dialogView.findViewById(R.id.img_time1);
        ImageView img_time2 = dialogView.findViewById(R.id.img_time2);
        ImageView img_time3 = dialogView.findViewById(R.id.img_time3);


        TextView txt_date1 = dialogView.findViewById(R.id.txt_date1);
        TextView txt_date2 = dialogView.findViewById(R.id.txt_date2);
        TextView txt_date3 = dialogView.findViewById(R.id.txt_date3);
        TextView txt_time1 = dialogView.findViewById(R.id.txt_time1);
        TextView txt_time2 = dialogView.findViewById(R.id.txt_time2);
        TextView txt_time3 = dialogView.findViewById(R.id.txt_time3);


        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
        try {
            if (entitySlotList.size() == 0) {
                edit.setVisibility(View.GONE);
            } else if (entitySlotList.size() == 1){
                edit.setVisibility(View.VISIBLE);
                for (int i = 0; i < entitySlotList.size(); i++)
                {

                    CommonEntitySlotsPOJO.EntitySlotList slotList = entitySlotList.get(i);
                    layout2.setVisibility(View.VISIBLE);
                    txt_date2.setText(Utility.getSlotDate(Utility.ConvertUTCToUserTimezone(slotList.getSlotDate())));
                    txt_time2.setText(Utility.getSlotTime(Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime()),
                            Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime())));

                    startTime = Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime());
                    endTime = Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime());
                }
            } else {
                edit.setVisibility(View.VISIBLE);
                for (int i = 0; i < entitySlotList.size(); i++)
                {
                    CommonEntitySlotsPOJO.EntitySlotList slotList = entitySlotList.get(i);
                    if(i == 0){
                        layout1.setVisibility(View.VISIBLE);
                        txt_date1.setText(Utility.getSlotDate(Utility.ConvertUTCToUserTimezone(slotList.getSlotDate())));
                        txt_time1.setText(Utility.getSlotTime(Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime()),
                                Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime())));
                    } else  if(i == 1){
                        layout2.setVisibility(View.VISIBLE);
                        txt_date2.setText(Utility.getSlotDate(Utility.ConvertUTCToUserTimezone(slotList.getSlotDate())));
                        txt_time2.setText(Utility.getSlotTime(Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime()),
                                Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime())));

                        startTime = Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime());
                        endTime = Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime());
                        //txt_time2.setText(slotList.getFromHour() + ":" + slotList.getFromMin() +"-" +slotList.getToHour() + ":" + slotList.getToMin());

                    } else  if(i == 2){
                        layout3.setVisibility(View.VISIBLE);
                        txt_date3.setText(Utility.getSlotDate(Utility.ConvertUTCToUserTimezone(slotList.getSlotDate())));
                        txt_time3.setText(Utility.getSlotTime(Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime()),
                                Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime())));

                        // txt_time3.setText(slotList.getFromHour() + ":" + slotList.getFromMin() +"-" +slotList.getToHour() + ":" + slotList.getToMin());

                    }
                }
            }
        } catch (Exception e){
            e.getMessage();
        }



        layout1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                startTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(0).getPlanStartTime());
                endTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(0).getPlanEndTime());
//                startTime = entitySlotList.get(0).getPlanStartTime();
//                endTime = entitySlotList.get(0).getPlanEndTime();
                setUnSelectedDate(txt_date3, txt_time3, img_date3, img_time3, layout3);
                selectDate(txt_date1, txt_time1, img_date1, img_time1, layout1);
                setUnSelectedDate(txt_date2, txt_time2, img_date2, img_time2, layout2);

            }
        });


        layout2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                if (entitySlotList.size() == 1)
                {
                    startTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(0).getPlanStartTime());
                    endTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(0).getPlanEndTime());
                } else {
                    startTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(1).getPlanStartTime());
                    endTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(1).getPlanEndTime());
                }
                setUnSelectedDate(txt_date3,txt_time3, img_date3, img_time3, layout3);
                setUnSelectedDate(txt_date1,txt_time1, img_date1, img_time1, layout1);
                selectDate(txt_date2,txt_time2, img_date2, img_time2, layout2);

            }
        });


        layout3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                startTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(2).getPlanStartTime());
                endTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(2).getPlanEndTime());
                selectDate(txt_date3,txt_time3, img_date3, img_time3, layout3);
                setUnSelectedDate(txt_date1,txt_time1, img_date1, img_time1, layout1);
                setUnSelectedDate(txt_date2,txt_time2, img_date2, img_time2, layout2);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
              //  getMeetingSlote();
                meetingEditDate();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entitySlotList.size() == 0) {
                    dialog.dismiss();
                    //  getMeetingSlote();
                    meetingEditDate();
                } else {
                    dialog.dismiss();

                    getAcceptMeeting();
                }

            }
        });
        dialog.setContentView(dialogView);
        dialog.show();


    }

    private void selectDate(TextView textDate,  TextView textTime, ImageView imageDate, ImageView imageTime, LinearLayout linearLayout){
        textDate.setTextColor(getApplicationContext().getResources().getColor(R.color.terracota));
        textTime.setTextColor(getApplicationContext().getResources().getColor(R.color.terracota));
        imageDate.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_calender_t));
        imageTime.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_time_t));
        linearLayout.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.rectangle_tarccoto_outline));

    }

    private void setUnSelectedDate(TextView textDate,  TextView textTime, ImageView imageDate, ImageView imageTime, LinearLayout linearLayout){
        textDate.setTextColor(getApplicationContext().getResources().getColor(R.color.darkGreyBlue));
        textTime.setTextColor(getApplicationContext().getResources().getColor(R.color.darkGreyBlue));
        imageDate.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_calender));
        imageTime.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_time));
        linearLayout.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.reactangle_grey_outline));

    }

    public void meetingEditDate() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_date, null);
        BottomSheetDialog dialog = new BottomSheetDialog(NotificationListActivity.this, R.style.SheetDialog);

        Button btn_next = dialogView.findViewById(R.id.btn_next);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        CalendarView calender_view = dialogView.findViewById(R.id.calender);

        selectedDate = DateFormat.format("yyyy-MM-dd", calender_view.getDate()).toString();
        Log.d("NEW_DATE", selectedDate);
        calender_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                month = month + 1;
                // output to log cat **not sure how to format year to two places here**
                String newDate = year+"-"+month+"-"+date;
                Log.d("NEW_DATE", newDate);
                selectedDate = newDate;
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                meetingEditTime();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();

    }

    public void meetingEditTime() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_time, null);
        BottomSheetDialog dialog = new BottomSheetDialog(NotificationListActivity.this, R.style.SheetDialog);

        Button btn_confirm = dialogView.findViewById(R.id.btn_confirm);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        TimePicker time_picker = dialogView.findViewById(R.id.time_picker);
        int hour = time_picker.getCurrentHour();
        int min = time_picker.getCurrentMinute();


        startTime = Utility.convertDate(selectedDate + " " + new StringBuilder().append(hour).append(":").append(min)
                .append(":").append("00"));
        Log.d(TAG, startTime);

        endTime = Utility.getOneHour(startTime) ;
        Log.d(TAG, endTime);

        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                int hour = time_picker.getCurrentHour();
                int min = time_picker.getCurrentMinute();
                startTime = Utility.convertDate(selectedDate + " " + new StringBuilder().append(hour).append(":").append(min)
                        .append(":").append("00"));
                Log.d(TAG, startTime);

                endTime = Utility.getOneHour(startTime) ;
                Log.d(TAG, endTime);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getAcceptMeeting();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();


    }


    @OnClick({R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;


        }
    }
    public void successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationListActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_meeting_confirmed, null);
        TextView label_close = view1.findViewById(R.id.label_close);

        //    tv_msg.setText("Session Added Successfully.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                getMeetingRequest();
            }
        });
        dialogs.show();
    }

}