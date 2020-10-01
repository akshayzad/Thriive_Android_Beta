package com.thriive.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
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
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

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
    private LoginPOJO  loginPOJO;
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
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonMeetingPOJO> call = apiInterface.getMeetingById(loginPOJO.getReturnEntity().getActiveToken(),
                getIntent().getStringExtra("meeting_id"));
        call.enqueue(new Callback<CommonMeetingPOJO>() {
            @Override
            public void onResponse(Call<CommonMeetingPOJO> call, Response<CommonMeetingPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    progressHUD.dismiss();
                    CommonMeetingPOJO pojo = response.body();
                    Log.d(TAG,""+pojo.getMessage());
                    if (pojo.getOK()) {
                        detailsMeeting(pojo.getMeetingObject());
                    } else {
                        Toast.makeText(getApplicationContext(), "Failure "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onFailure(Call<CommonMeetingPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(getApplicationContext(), "Getting Error", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getStringExtra("intent_type").equals("FLOW")){
            getMeetingRequest();
        }

    }

    private void getMeetingRequest() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonMeetingListPOJO> call = apiInterface.getPendingRequest(loginPOJO.getReturnEntity().getActiveToken(),
                loginPOJO.getReturnEntity().getRowcode());
        call.enqueue(new Callback<CommonMeetingListPOJO>() {
            @Override
            public void onResponse(Call<CommonMeetingListPOJO> call, Response<CommonMeetingListPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonMeetingListPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMessage());
                    if (reasonPOJO.getOK()) {
                     //    Toast.makeText(getApplicationContext(), "Success "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                        rv_notification.setAdapter(new PendingNotificationAdapter(NotificationListActivity.this,
                                reasonPOJO.getMeetingList()));

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

    }

    private void getAcceptMeeting() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getAcceptMeeting(loginPOJO.getReturnEntity().getActiveToken(),
                meetingCode, loginPOJO.getReturnEntity().getRowcode(), startTime,endTime);
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

    }

    public void getDeclineMeeting(String meetingCode) {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getRejectMeeting(loginPOJO.getReturnEntity().getActiveToken(),
                meetingCode, loginPOJO.getReturnEntity().getRowcode());
        call.enqueue(new Callback<CommonPOJO>() {
            @Override
            public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMessage());
                    if (reasonPOJO.getOK()) {
                        Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();

                        finish();

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

    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    public void detailsMeeting(CommonMeetingListPOJO.MeetingListPOJO meetingListPOJO) {
        meetingCode = meetingListPOJO.getMeetingCode();
        startTime = meetingListPOJO.getPlanStartTime();
        endTime = meetingListPOJO.getPlanEndTime();
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

        txt_reason.setText(meetingListPOJO.getMeetingReason());
        txt_objective.setText(meetingListPOJO.getRequestorDesignationTags().get(0));
        StringBuilder s = new StringBuilder();
        for (int i = 0; i<meetingListPOJO.getRequestorName().length()- 2; i++){
            s.append("X");
        }
        String temp_no = meetingListPOJO.getRequestorName();
        String string = temp_no.substring(0, temp_no.length() - temp_no.length()) + s;
        txt_requestor.setText(string);



//        if (!meetingListPOJO.getGiverDomains().equals("")){
//            TextView valueTV = new TextView(this);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(5, 5, 5, 5);
//            valueTV.setLayoutParams(params);
//            Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto_medium);
//            valueTV.setTypeface(typeface);
//            valueTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            valueTV.setTextColor(getApplicationContext().getColor(R.color.darkGrey));
//            valueTV.setBackground(getApplicationContext().getDrawable(R.drawable.outline_circle_gray));
//            valueTV.setText(meetingListPOJO.getDomainName() + "");
//            valueTV.setTextSize(11);
//            layout_tags.addView(valueTV);
//
//        }

//        if (!meetingListPOJO.getGiverSubDomains().equals("")){
//            TextView valueTV = new TextView(this);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(5, 5, 5, 5);
//            valueTV.setLayoutParams(params);
//            Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto_medium);
//            valueTV.setTypeface(typeface);
//            valueTV.setTextSize(12);
//            valueTV.setTextColor(getApplicationContext().getColor(R.color.darkGrey));
//            valueTV.setText(meetingListPOJO.getSubDomainName() );
//            valueTV.setGravity(View.TEXT_ALIGNMENT_CENTER);
//            valueTV.setBackground(getApplicationContext().getDrawable(R.drawable.outline_circle_gray));
//            layout_tags.addView(valueTV);
//        }


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
                dialogs.dismiss();
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
        for (int i = 0; i < entitySlotList.size(); i++)
        {
            CommonEntitySlotsPOJO.EntitySlotList slotList = entitySlotList.get(i);
            if(i == 0){
                layout1.setVisibility(View.VISIBLE);
                txt_date1.setText(Utility.getSlotDate(slotList.getSlotDate()));
                txt_time1.setText(Utility.getSlotTime(slotList.getPlanStartTime(), slotList.getPlanEndTime()));
            } else  if(i == 1){
                layout2.setVisibility(View.VISIBLE);
                txt_date2.setText(Utility.getSlotDate(slotList.getSlotDate()));
                txt_time2.setText(Utility.getSlotTime(slotList.getPlanStartTime(), slotList.getPlanEndTime()));

                startTime = slotList.getPlanStartTime();
                endTime = slotList.getPlanEndTime();
                //txt_time2.setText(slotList.getFromHour() + ":" + slotList.getFromMin() +"-" +slotList.getToHour() + ":" + slotList.getToMin());

            } else  if(i == 2){
                layout3.setVisibility(View.VISIBLE);
                txt_date3.setText(Utility.getSlotDate(slotList.getSlotDate()));
                txt_time3.setText(Utility.getSlotTime(slotList.getPlanStartTime(), slotList.getPlanEndTime()));

                // txt_time3.setText(slotList.getFromHour() + ":" + slotList.getFromMin() +"-" +slotList.getToHour() + ":" + slotList.getToMin());

            }
        }

        layout1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                startTime = entitySlotList.get(0).getPlanStartTime();
                endTime = entitySlotList.get(0).getPlanEndTime();
                layout1.setBackground(getDrawable(R.drawable.rectangle_tarccoto_outline));
                layout2.setBackground(getDrawable(R.drawable.rectangle_grey_half_outline));
                layout3.setBackground(getDrawable(R.drawable.reactangle_grey_outline));

                img_date1.setImageDrawable(getDrawable(R.drawable.ic_calender_t));
                img_date2.setImageDrawable(getDrawable(R.drawable.ic_calender));
                img_date3.setImageDrawable(getDrawable(R.drawable.ic_calender));

                img_time1.setImageDrawable(getDrawable(R.drawable.ic_time_t));
                img_time2.setImageDrawable(getDrawable(R.drawable.ic_time));
                img_time3.setImageDrawable(getDrawable(R.drawable.ic_time));

                txt_date1.setTextColor(getColor(R.color.terracota));
                txt_date2.setTextColor(getColor(R.color.darkGreyBlue));
                txt_date3.setTextColor(getColor(R.color.darkGreyBlue));


                txt_time1.setTextColor(getColor(R.color.terracota));
                txt_time2.setTextColor(getColor(R.color.darkGreyBlue));
                txt_time3.setTextColor(getColor(R.color.darkGreyBlue));

            }
        });


        layout2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                startTime = entitySlotList.get(1).getPlanStartTime();
                endTime = entitySlotList.get(1).getPlanEndTime();
                layout1.setBackground(getDrawable(R.drawable.reactangle_grey_outline));
                layout2.setBackground(getDrawable(R.drawable.half_outline_tarracco));
                layout3.setBackground(getDrawable(R.drawable.reactangle_grey_outline));

                img_date1.setImageDrawable(getDrawable(R.drawable.ic_calender));
                img_date2.setImageDrawable(getDrawable(R.drawable.ic_calender_t));
                img_date3.setImageDrawable(getDrawable(R.drawable.ic_calender));

                img_time1.setImageDrawable(getDrawable(R.drawable.ic_time));
                img_time2.setImageDrawable(getDrawable(R.drawable.ic_time_t));
                img_time3.setImageDrawable(getDrawable(R.drawable.ic_time));

                txt_date1.setTextColor(getColor(R.color.darkGreyBlue));
                txt_date2.setTextColor(getColor(R.color.terracota));
                txt_date3.setTextColor(getColor(R.color.darkGreyBlue));


                txt_time1.setTextColor(getColor(R.color.darkGreyBlue));
                txt_time2.setTextColor(getColor(R.color.terracota));
                txt_time3.setTextColor(getColor(R.color.darkGreyBlue));

            }
        });


        layout3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                startTime = entitySlotList.get(2).getPlanStartTime();
                endTime = entitySlotList.get(2).getPlanEndTime();
                layout1.setBackground(getDrawable(R.drawable.reactangle_grey_outline));
                layout2.setBackground(getDrawable(R.drawable.rectangle_grey_half_outline));
                layout3.setBackground(getDrawable(R.drawable.rectangle_tarccoto_outline));

                img_date1.setImageDrawable(getDrawable(R.drawable.ic_calender));
                img_date2.setImageDrawable(getDrawable(R.drawable.ic_calender));
                img_date3.setImageDrawable(getDrawable(R.drawable.ic_calender_t));

                img_time1.setImageDrawable(getDrawable(R.drawable.ic_time));
                img_time2.setImageDrawable(getDrawable(R.drawable.ic_time));
                img_time3.setImageDrawable(getDrawable(R.drawable.ic_time_t));

                txt_date1.setTextColor(getColor(R.color.darkGreyBlue));
                txt_date2.setTextColor(getColor(R.color.darkGreyBlue));
                txt_date3.setTextColor(getColor(R.color.terracota));


                txt_time1.setTextColor(getColor(R.color.darkGreyBlue));
                txt_time2.setTextColor(getColor(R.color.darkGreyBlue));
                txt_time3.setTextColor(getColor(R.color.terracota));

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
                dialog.dismiss();

                getAcceptMeeting();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();


    }

    private void getMeetingSlote() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonEntitySlotsPOJO> call = apiInterface.getEntitySlots(loginPOJO.getReturnEntity().getActiveToken(), loginPOJO.getReturnEntity().getRowcode());
        call.enqueue(new Callback<CommonEntitySlotsPOJO>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<CommonEntitySlotsPOJO> call, Response<CommonEntitySlotsPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonEntitySlotsPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMessage());
                    if (reasonPOJO.getOK()) {
                        //.makeText(getApplicationContext(), "Success "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();


                        meetingAvailability(reasonPOJO.getEntitySlotList());

                    } else {
                        Toast.makeText(getApplicationContext(), "Failure "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }
            @Override
            public void onFailure(Call<CommonEntitySlotsPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(NotificationListActivity.this, "Getting Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void meetingEditDate() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_date, null);
        BottomSheetDialog dialog = new BottomSheetDialog(NotificationListActivity.this, R.style.SheetDialog);

        Button btn_next = dialogView.findViewById(R.id.btn_next);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        CalendarView calender_view = dialogView.findViewById(R.id.calender);
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
                cancelReason = btn_TimeMatch.getText().toString();
             //   getCancelMeeting();
            }
        });
        btm_noTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                cancelReason = btm_noTime.getText().toString();
//                //  meetingCancelConfirmation();
            }
        });
        dialogs.show();

    }
}