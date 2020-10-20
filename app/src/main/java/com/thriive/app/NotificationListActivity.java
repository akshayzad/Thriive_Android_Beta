package com.thriive.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
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

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.ExperienceListAdapter;
import com.thriive.app.adapters.ExpertiseAdapter;
import com.thriive.app.adapters.PendingNotificationAdapter;
import com.thriive.app.adapters.SlotListAdapter;
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
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.SwipeController;
import com.thriive.app.utilities.SwipeControllerActions;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.rv_notification)
    RecyclerView rv_notification;
    @BindView(R.id.txt_noData)
    TextView txt_noData;
    @BindView(R.id.refresh_view)
    SwipeRefreshLayout refreshView;
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

        refreshView.setOnRefreshListener(this);
        refreshView.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        refreshView.setRefreshing(false);

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
                        try {
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo != null){
                                if (pojo.getOK()) {
                                    if (pojo.getMeetingObject() != null){

                                        detailsMeeting(pojo.getMeetingObject());
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e){
                            e.getMessage();
                        }

                    }
                }
                @Override
                public void onFailure(Call<CommonMeetingPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(getApplicationContext(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            txt_noData.setVisibility(View.GONE);
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
                        try {
                            Log.d(TAG,""+reasonPOJO.getMessage());
                            if (reasonPOJO.getOK()) {
                                if (reasonPOJO.getMeetingList() != null){
                                    //    Toast.makeText(getApplicationContext(), "Success "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                    rv_notification.setAdapter(new PendingNotificationAdapter(NotificationListActivity.this,
                                            reasonPOJO.getMeetingList()));

                                    if (reasonPOJO.getMeetingList().size() == 0){
                                        txt_noData.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_noData.setVisibility(View.GONE);
                                    }
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.getMessage();
                        }
                    }
                    if (refreshView != null) {
                        refreshView.setRefreshing(false);
                    }

                }
                @Override
                public void onFailure(Call<CommonMeetingListPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(NotificationListActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                        try {
                            Log.d(TAG,""+reasonPOJO.getMessage());
                            if (reasonPOJO.getOK()) {
                             //   Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();

                                successDialog();

                            } else {
                                Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.getMessage();
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(NotificationListActivity.this, " " +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                        try {
                            Log.d(TAG,""+reasonPOJO.getMessage());
                            if (reasonPOJO != null){
                                if (reasonPOJO.getOK()) {
                              //      Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(NotificationListActivity.this, "" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            TextView txt_reason  = view1.findViewById(R.id.txt_reason);
            TextView txt_name = view1.findViewById(R.id.txt_name);
            TextView txt_experience = view1.findViewById(R.id.txt_experience);
            TextView txt_profession = view1.findViewById(R.id.txt_profession);
            CircleImageView img_user = view1.findViewById(R.id.img_user);
            //    tv_msg.setText("Session Added Successfully.");
            builder.setView(view1);
            final AlertDialog dialogs = builder.create();
            dialogs.setCancelable(false);
//            if (meetingListPOJO.getRequestorDesignationTags().size() > 0){
//                txt_profession.setText(meetingListPOJO.getRequestorDesignationTags().get(0));
//            } else {
//
//            }
            txt_profession.setText(""+meetingListPOJO.getRequestorSubTitle());
            txt_reason.setText("Meeting for "+meetingListPOJO.getMeetingReason());
            try {
                txt_name.setText(Utility.getEncodedName(meetingListPOJO.getRequestorName()));
            } catch (Exception e){
                txt_name.setText(""+meetingListPOJO.getRequestorName());
            }
            if (meetingListPOJO.getRequestorExperienceTags() != null){
                txt_experience.setText("Total " +meetingListPOJO.getRequestorExperienceTags().get(0));
            }
            if (meetingListPOJO.getRequestorPicUrl().equals("")){
                Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_medium);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(getResources().getColor(R.color.darkGreyBlue))
                        .useFont(typeface)
                        .fontSize(60) /* size in px */
                        .bold()
                        .toUpperCase()
                        .width(140)  // width in px
                        .height(140) // height in px
                        .endConfig()
                        .buildRect(Utility.getInitialsName(meetingListPOJO.getRequestorName()) ,getResources().getColor(R.color.whiteTwo));

//                TextDrawable drawable = TextDrawable.builder().width(60)  // width in px
//                        .height(60)
//                        .buildRound(""+meetingListPOJO.getGiverName().charAt(0), R.color.darkGreyBlue);
                img_user.setImageDrawable(drawable);
            } else {
                img_user.setMinimumWidth(120);
                img_user.setMaxHeight(120);
                img_user.setMinimumHeight(120);
                img_user.setMaxWidth(120);
                Glide.with(this)
                        .load(meetingListPOJO.getRequestorPicUrl())
                        .into(img_user);
            }

            FlexboxLayoutManager manager = new FlexboxLayoutManager(NotificationListActivity.this);
//            manager.setFlexWrap(FlexWrap.WRAP);
//            manager.setJustifyContent(JustifyContent.CENTER);
            rv_experience.setLayoutManager(manager );
            ArrayList<String> array = new ArrayList<>();
          //  array.addAll(meetingListPOJO.getRequestorExperienceTags());
            array.addAll(meetingListPOJO.getRequestorDesignationTags());
           // array.addAll(meetingListPOJO.getRequestorDesignationTags());
            rv_experience.setAdapter(new ExperienceListAdapter(NotificationListActivity.this,array));

            FlexboxLayoutManager manager1 = new FlexboxLayoutManager(NotificationListActivity.this);
//            manager1.setFlexWrap(FlexWrap.WRAP);
//            manager1.setJustifyContent(JustifyContent.CENTER);
            rv_expertise.setLayoutManager(manager1 );
            ArrayList<String> array1 = new ArrayList<>();
            array1.addAll(meetingListPOJO.getRequestorExpertiseTags());
            //array1.addAll(meetingListPOJO.getRequestorSubDomainTags());
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
                        getMeetingRequest();
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
                        try {
                            Log.d(TAG,""+reasonPOJO.getMessage());
                            if (reasonPOJO != null){
                                if (reasonPOJO.getOK()) {
                                    if (reasonPOJO.getEntitySlotList() != null){
                                       // meetingAvailability(reasonPOJO.getEntitySlotList());

                                        meetingEditSlot(reasonPOJO.getEntitySlotList());
                                    }
                                    //.makeText(getApplicationContext(), "Success "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e){
                            e.getMessage();
                        }
                        if (refreshView != null) {
                            refreshView.setRefreshing(false);
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonEntitySlotsPOJO> call, Throwable t) {
                    progressHUD.dismiss();
               //     Toast.makeText(NotificationListActivity.this, "Getting Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void meetingAvailability(List<CommonEntitySlotsPOJO.EntitySlotList> entitySlotList) {
        try {
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
        } catch (Exception e){
            e.getMessage();
        }
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
    public void meetingEditSlot(List<CommonEntitySlotsPOJO.EntitySlotList> entitySlotList) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_availability, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getApplicationContext(), R.style.SheetDialog);
        Button btn_confirm = dialogView.findViewById(R.id.btn_confirm);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        RecyclerView rv_slots = dialogView.findViewById(R.id.rv_slots);
        SlotListAdapter adapter  = new SlotListAdapter(getApplicationContext(), (ArrayList<CommonEntitySlotsPOJO.EntitySlotList>) entitySlotList);
        rv_slots.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_slots.setAdapter(adapter);
//        SwipeableRecyclerView rv = dialogView.findViewById(R.id.rv);
//        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rv.setAdapter(adapter);
//
//        rv.setListener(new SwipeLeftRightCallback.Listener() {
//            @Override
//            public void onSwipedLeft(int position) {
////                mList.remove(position);
//                adapter.notifyItemChanged(position);
//            }
//
//            @Override
//            public void onSwipedRight(int position) {
////                mList.remove(position);
//                adapter.notifyItemChanged(position);
//            }
//        });


        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                dialog.dismiss();
                meetingEditDate();
                //adapter.players.remove(position);
                //adapter.notifyItemRemoved(position);
                // adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }
        });
//
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rv_slots);
        // p.setColor(Color.rgb(16,133,104));

        rv_slots.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
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
                if (entitySlotList.size() == 0) {
                    dialog.dismiss();
                    //  getMeetingSlote();
                    meetingEditDate();
                } else {
                    if (adapter.endTime.equals("")){
                        Toast.makeText(getApplicationContext(), "Please choose slot", Toast.LENGTH_SHORT).show();
                    } else {
                        startTime = adapter.startTime;
                        endTime = adapter.endTime;
                        dialog.dismiss();
                        getAcceptMeeting();
                    }
                }
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();
    }

    public void meetingEditDate() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_date, null);
        BottomSheetDialog dialog = new BottomSheetDialog(NotificationListActivity.this, R.style.SheetDialog);

        Button btn_next = dialogView.findViewById(R.id.btn_next);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        CalendarView calender_view = dialogView.findViewById(R.id.calender);
        calender_view.setMinDate(new Date().getTime());

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
                if(Utility.getCallJoin(startTime)){
                    Toast.makeText(getApplicationContext(), "Please choose current or future time.", Toast.LENGTH_SHORT).show();
                } else {
                    getAcceptMeeting();
                    dialog.dismiss();

                }
//                dialog.dismiss();
//                getAcceptMeeting();
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

    @Override
    public void onRefresh() {
        if (refreshView != null){
            refreshView.setRefreshing(true);
        }
        getMeetingRequest();
    }
}