package com.thriive.app.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.provider.CalendarContract;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.onesignal.OneSignal;
import com.thriive.app.MeetingJoinActivity;
import com.thriive.app.R;
import com.thriive.app.adapters.RequestPagerAdapter;
import com.thriive.app.adapters.SchedulePagerAdapter;
import com.thriive.app.adapters.SlotListFragmentAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonEntitySlotsPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonScheduleMeetingPOJO;
import com.thriive.app.models.CommonStartMeetingPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.SwipeController;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thriive.app.utilities.Utility.checkInternet;


public class MeetingsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_noRequest)
    TextView txt_noRequest;
    @BindView(R.id.txt_noSchedule)
    TextView txt_noSchedule;
    @BindView(R.id.refresh_view)
    SwipeRefreshLayout refreshView;
    Unbinder unbinder;
    @BindView(R.id.txt_schedule)
    TextView txt_schedule;
    @BindView(R.id.txt_request)
    TextView txt_request;
    @BindView(R.id.viewpager_schedule)
    ViewPager viewpager_schedule;
    @BindView(R.id.layout_dotSchedule)
    LinearLayout layout_dotSchedule;
    TextView[] dotSchedule;

    @BindView(R.id.layout_dotRequest)
    LinearLayout layout_dotRequest;
    @BindView(R.id.viewpager_request)
    ViewPager viewpager_request;
    TextView[] dotRequest;

    private BottomSheetDialog dialogEditSlot;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_CALENDAR
    };


    private APIInterface apiInterface;
    private LoginPOJO.ReturnEntity loginPOJO;
    private SharedData sharedData;

    public static final String TAG = MeetingsFragment.class.getName();
    private  String startTime, endTime, meetingCode, selectedDate, personaName, region_name, user_region = "";

    private  String  meetingReason, title;
    private long lnsTime, lneTime;
    private KProgressHUD progressHUD;
    private CommonStartMeetingPOJO.MeetingDataPOJO meetingDataPOJO;

    private ArrayList<CommonMeetingListPOJO.MeetingListPOJO> meetingListSchedule;
    private ArrayList<PendingMeetingRequestPOJO.MeetingRequestList> meetingListRequest;

    private String schedule_date = "", request_date = "", time_stamp = "";

    private SchedulePagerAdapter schedulePagerAdapter;
    private RequestPagerAdapter requestPagerAdapter;
    private SwipeController swipeController = null;
    public MeetingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe()
    public void onMessageEvent(EventBusPOJO event) {
        if (event.getEvent() == Utility.MEETING_CANCEL){
            onResume();
          //  ((MeetingsFragment()).onResume();
        }
        if (event.getEvent() == Utility.MEETING_BOOK){
            onResume();
            //  ((MeetingsFragment()).onResume();
        }
    }


    public static Fragment newInstance() {
        Fragment fragment = new MeetingsFragment();
        return fragment;
    }


    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_meeetings, container, false);
        unbinder = ButterKnife.bind(this, view);

        loginPOJO = Utility.getLoginData(getActivity());
        apiInterface = APIClient.getApiInterface();
        sharedData = new SharedData(getActivity());

        Log.d(TAG, loginPOJO.getActiveToken()   + "  " + loginPOJO.getRowcode());
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
        txt_name.setText("Welcome, " + loginPOJO.getFirstName());
        refreshView.setOnRefreshListener(this);
        refreshView.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        refreshView.setRefreshing(false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCallData();
    }

    @Override
    public void onRefresh() {
        NetworkInfo networkInfo = checkInternet(getActivity());
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            if (refreshView != null){
                refreshView.setRefreshing(true);
            }
            getCallData();
        }

    }

    private void getCallData(){
        NetworkInfo networkInfo = checkInternet(getActivity());
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            getScheduledMeeting();
            getMeetingRequest();
        } else {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void setScheduleData() {
        //  To show preview of left and right pages set the following two values
        viewpager_schedule.setPadding(0,0,30,0);
//        viewpager_schedule.setPageMargin(10);
        addDotSchedule(0);

        // whenever the page changes
        viewpager_schedule.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int i) {
                try {
                    addDotSchedule(i);
                    schedule_date = schedulePagerAdapter.getDate(i);
                    txt_schedule.setText(Utility.getScheduleMeetingDate(Utility.ConvertUTCToUserTimezone(schedule_date)));

                } catch (Exception e){
                    e.getMessage();
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void addDotSchedule(int page_position) {
        try {
            dotSchedule = new TextView[meetingListSchedule.size()];
            layout_dotSchedule.removeAllViews();

            for (int i = 0; i < dotSchedule.length; i++) {;
                dotSchedule[i] = new TextView(getActivity());
                dotSchedule[i].setText(Html.fromHtml("&#9679;"));
                dotSchedule[i].setTextSize(20);
                dotSchedule[i].setTextColor(getResources().getColor(R.color.pinkishGreyTwo));
                layout_dotSchedule.addView(dotSchedule[i]);
            }
            //active dot
            dotSchedule[page_position].setTextColor(getResources().getColor(R.color.battleshipGrey));
        } catch (Exception e){
            e.getMessage();
        }

    }

    private void setRequestData() {
        viewpager_request.setPadding(0, 0, 30, 0);
        addDotRequest(0);
        // whenever the page changes
        viewpager_request.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                try {
                    addDotRequest(i);
                    request_date = requestPagerAdapter.getDate(i);
                    txt_request.setText(Utility.getScheduleMeetingDate(Utility.ConvertUTCToUserTimezone(request_date)));

                } catch (Exception e){
                    e.getMessage();
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void addDotRequest(int page_position) {
        try {
            dotRequest = new TextView[meetingListRequest.size()];
            layout_dotRequest.removeAllViews();
            for (int i = 0; i < dotRequest.length; i++) {;
                dotRequest[i] = new TextView(getActivity());
                dotRequest[i].setText(Html.fromHtml("&#9679;"));
                dotRequest[i].setTextSize(20);
                dotRequest[i].setTextColor(getResources().getColor(R.color.pinkishGreyTwo));
                layout_dotRequest.addView(dotRequest[i]);
            }
            //active dot
            dotRequest[page_position].setTextColor(getResources().getColor(R.color.battleshipGrey));
        } catch (Exception e){
            e.getMessage();
        }
    }

    private void getScheduledMeeting() {
        try {
            TimeZone timeZone = TimeZone.getDefault();
            Log.d(TAG, "time zone "+ timeZone.getID());
            String UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
            if (UUID  == null) {
                UUID = "";
            }
            Call<CommonScheduleMeetingPOJO> call = apiInterface.getScheduledMeeting(sharedData.getStringData(SharedData.API_URL)
                            + "api/Meeting/get-scheduled-meetings", loginPOJO.getActiveToken(),
                    loginPOJO.getRowcode(),  UUID, ""+timeZone.getID(), time_stamp);
            call.enqueue(new Callback<CommonScheduleMeetingPOJO>() {
                @Override
                public void onResponse(Call<CommonScheduleMeetingPOJO> call, Response<CommonScheduleMeetingPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonScheduleMeetingPOJO pojo = response.body();
                        Log.d(TAG,""+pojo.getMessage());
                        if (pojo != null){
                            if (pojo.getOK()) {
                                if (pojo.getMeetingList() != null){
                                    meetingListSchedule = new ArrayList<>();
                                    meetingListSchedule.addAll(pojo.getMeetingList());
                                    viewpager_schedule.setVisibility(View.VISIBLE);
                                    layout_dotSchedule.setVisibility(View.VISIBLE);
                                    schedulePagerAdapter = new SchedulePagerAdapter(getActivity(),
                                            MeetingsFragment.this, meetingListSchedule);
                                    viewpager_schedule.setAdapter(schedulePagerAdapter);
//                                    schedulePagerAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            if (pojo.getMeetingList() == null){
                                txt_noSchedule.setVisibility(View.VISIBLE);
                                txt_schedule.setText("");
                                viewpager_schedule.setVisibility(View.GONE);
                                layout_dotSchedule.setVisibility(View.GONE);
                            } else if (pojo.getMeetingList().size() == 0){
                                txt_noSchedule.setVisibility(View.VISIBLE);
                                txt_schedule.setText("");
                                viewpager_schedule.setVisibility(View.GONE);
                                layout_dotSchedule.setVisibility(View.GONE);
                            } else {
                                txt_noSchedule.setVisibility(View.GONE);
                                schedule_date = schedulePagerAdapter.getDate(0);
                                txt_schedule.setText(Utility.getScheduleMeetingDate(Utility.ConvertUTCToUserTimezone(schedule_date)));
                                setScheduleData();
                            }

                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonScheduleMeetingPOJO> call, Throwable t) {
                    //  progressHUD.dismiss();
                   // Toast.makeText(getContext(), "Getting Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }


    }

    private void getMeetingRequest() {
        try {
            Call<PendingMeetingRequestPOJO> call = apiInterface.getPendingMeeting(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/get-pending-meetings", loginPOJO.getActiveToken(),
                    loginPOJO.getRowcode());
            call.enqueue(new Callback<PendingMeetingRequestPOJO>() {
                @Override
                public void onResponse(Call<PendingMeetingRequestPOJO> call, Response<PendingMeetingRequestPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        PendingMeetingRequestPOJO pojo = response.body();
                        Log.d(TAG,""+pojo.getMessage());
                        if (pojo != null){
                            if (pojo.getOK()) {
                                if (pojo.getMeetingRequestList() != null){
                                    meetingListRequest = new ArrayList<>();
                                    meetingListRequest.addAll(pojo.getMeetingRequestList());
                                    requestPagerAdapter = new RequestPagerAdapter(getActivity(), MeetingsFragment.this, meetingListRequest);
                                    viewpager_request.setAdapter(requestPagerAdapter);
                                 //   requestPagerAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            if (pojo.getMeetingRequestList() == null){
                                viewpager_request.setVisibility(View.GONE);
                                layout_dotRequest.setVisibility(View.GONE);
                                txt_noRequest.setVisibility(View.VISIBLE);
                                txt_request.setText("");
                            } else {
                                if (pojo.getMeetingRequestList().size() == 0) {
                                    txt_noRequest.setVisibility(View.VISIBLE);
                                    txt_request.setText("");
                                    viewpager_request.setVisibility(View.GONE);
                                    layout_dotRequest.setVisibility(View.GONE);
                                } else {
                                    viewpager_request.setVisibility(View.VISIBLE);
                                    layout_dotRequest.setVisibility(View.VISIBLE);
                                    txt_noRequest.setVisibility(View.GONE);
                                    request_date = requestPagerAdapter.getDate(0);
                                    txt_request.setText(Utility.getScheduleMeetingDate(Utility.ConvertUTCToUserTimezone(request_date)));
                                    setRequestData();

                                }
                            }
                        }
                        if (refreshView != null) {
                            refreshView.setRefreshing(false);
                        }
                    }
                }
                @Override
                public void onFailure(Call<PendingMeetingRequestPOJO> call, Throwable t) {
                    //progressHUD.dismiss();
                    //Toast.makeText(getContext(), "Getting Error", Toast.LENGTH_SHORT).show();
                }
            });

        } catch(Exception e){
            e.getMessage();
        }

    }


    public void startMeeting(int meeting_id) {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonStartMeetingPOJO> call = apiInterface.getMeetingStart(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/meeting-start", loginPOJO.getActiveToken(),
                meeting_id, true, loginPOJO.getRowcode());
        call.enqueue(new Callback<CommonStartMeetingPOJO>() {
            @Override
            public void onResponse(Call<CommonStartMeetingPOJO> call, Response<CommonStartMeetingPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d("TAG", response.toString());
                    CommonStartMeetingPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                     Log.d(TAG,""+reasonPOJO.getMessage());
                     if (reasonPOJO != null){
                         if (reasonPOJO.getOK()) {
                             meetingDataPOJO = reasonPOJO.getMeetingData();
                             sharedData.addStringData(SharedData.MEETING_TOKEN, meetingDataPOJO.getMeetingToken());
                             if (sharedData.getIntData(SharedData.USER_ID) == reasonPOJO.getMeetingData().getGiverId()){
                                 sharedData.addStringData(SharedData.MEETING_PARSON_NAME, reasonPOJO.getMeetingData().getRequestorName());
                             }else {
                                 sharedData.addStringData(SharedData.MEETING_PARSON_NAME, reasonPOJO.getMeetingData().getGiverName());
                             }
                             callMeeting();
                            // Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                         } else {
                             Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                             EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_CANCEL));
                         }
                     }
                }
            }
            @Override
            public void onFailure(Call<CommonStartMeetingPOJO> call, Throwable t) {
                progressHUD.dismiss();
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void callMeeting(){
        Intent intent = new Intent(getContext(), MeetingJoinActivity.class);
        Log.d(TAG, "" + meetingDataPOJO.getMeetingId());
        intent.putExtra("meeting_id", ""+meetingDataPOJO.getMeetingId());
        intent.putExtra("meeting_channel", meetingDataPOJO.getMeetingChannel());
        intent.putExtra("meeting_token", meetingDataPOJO.getMeetingToken());
        intent.putExtra("meeting_code", meetingDataPOJO.getMeetingCode());
        intent.putExtra("start_time", meetingDataPOJO.getPlanStartTime());
        intent.putExtra("end_time", meetingDataPOJO.getPlanEndTime());
        intent.putExtra("intent_type", "FLOW");

        Log.d(TAG,  meetingDataPOJO.getPlanStartTime() + " "+  meetingDataPOJO.getPlanEndTime());
        Log.d(TAG,  meetingDataPOJO.getActualStartTime() + " "+  meetingDataPOJO.getAcutalEndTime());
        startActivityForResult(intent,123);
    }



    public void getMeetingSlot(String meetingCode, String s, String m, String r_name, String user_country) {
        this.meetingCode = ""+meetingCode;
        meetingReason = m;
        personaName = s;
        region_name = r_name;
        user_region = user_country;
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonEntitySlotsPOJO> call = apiInterface.getEntitySlots(sharedData.getStringData(SharedData.API_URL) + "api/Entity/get-entity-slots", ""+loginPOJO.getActiveToken(), ""+loginPOJO.getRowcode());
        call.enqueue(new Callback<CommonEntitySlotsPOJO>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<CommonEntitySlotsPOJO> call, Response<CommonEntitySlotsPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonEntitySlotsPOJO pojo = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+pojo.getMessage());
                    if (pojo != null){
                        if (pojo.getOK()) {
                            //.makeText(getApplicationContext(), "Success "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                           // meetingAvailability(pojo.getEntitySlotList());
                            meetingEditSlot(pojo.getEntitySlotList());
                        } else {
                            Toast.makeText(getContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }


                }
            }
            @Override
            public void onFailure(Call<CommonEntitySlotsPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(getContext(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void meetingEditSlot(List<CommonEntitySlotsPOJO.EntitySlotList> entitySlotList) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_availability, null);
        dialogEditSlot = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
        Button btn_confirm = dialogView.findViewById(R.id.btn_newSlot);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        RecyclerView rv_slots = dialogView.findViewById(R.id.rv_slots);
        ArrayList<CommonEntitySlotsPOJO.EntitySlotList> arrayList = new ArrayList<>();

        if (entitySlotList.size() > 3){
            for (int i = 0; i <= 2; i++) {
                arrayList.add(entitySlotList.get(i));
            }
        } else {
            arrayList.addAll(entitySlotList);
        }

        SlotListFragmentAdapter adapter  = new SlotListFragmentAdapter(getActivity(), MeetingsFragment.this,
                (ArrayList<CommonEntitySlotsPOJO.EntitySlotList>) entitySlotList, "MEETING");
        rv_slots.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        rv_slots.setAdapter(adapter);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditSlot.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // dialogEditSlot.dismiss();
                //  getMeetingSlote();
                meetingEditDate();
            }
        });
        dialogEditSlot.setContentView(dialogView);
        dialogEditSlot.show();
    }



    public void meetingConfirmation(String s_time, String e_time){
        startTime = s_time;
        endTime = e_time;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_meeting_confirmation, null);

        Button accept = view1.findViewById(R.id.btn_yes);
        Button decline = view1.findViewById(R.id.btn_no);
        TextView txt_title = view1.findViewById(R.id.txt_title);
        TextView txt_subTitle = view1.findViewById(R.id.txt_subTitle);
        TextView txt_date = view1.findViewById(R.id.txt_date);
        TextView txt_time = view1.findViewById(R.id.txt_time);
        TextView txt_country = view1.findViewById(R.id.txt_country);


        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);

        txt_title.setText(Html.fromHtml("You’re rescheduling a meeting with"));
        txt_subTitle.setText(Html.fromHtml(personaName + " for " + meetingReason + ""));
        txt_time.setText(Utility.getMeetingTime(startTime, endTime) + " ("+ user_region + " time)");
        txt_date.setText(Utility.getMeetingDate(startTime));

        txt_country.setText("from " + region_name);
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
                startTime = s_time;
                endTime = e_time;
                getResheduledMeeting();
            }
        });
        dialogs.show();
    }
    public void meetingEditDate() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_date, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
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
               // Toast.makeText(getContext(),date+ "/"+month+"/"+year + "  "+arg0.getDate(),Toast.LENGTH_LONG).show();
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
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
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
                startTime = Utility.convertDate(selectedDate + " "+ new StringBuilder().append(hour).append(":").append(min)
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
                if(Utility.getCheckSlotTime(startTime)){
                    Toast.makeText(getContext(), "Please choose current or future time.", Toast.LENGTH_LONG).show();
                } else {
                    //getResheduledMeeting();
                    dialog.dismiss();

                    meetingConfirmation(startTime, endTime);

                }
               // dialog.dismiss();
               // getResheduledMeeting();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();


    }

    private void getResheduledMeeting() {
        startTime = Utility.ConvertUserTimezoneToUTC(startTime);
        endTime = Utility.ConvertUserTimezoneToUTC(endTime);
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getRescheduleMeeting(sharedData.getStringData(SharedData.API_URL)
                        + "api/Meeting/reschedule-meeting", loginPOJO.getActiveToken(),
                meetingCode, loginPOJO.getRowcode(), startTime, endTime);
        call.enqueue(new Callback<CommonPOJO>() {
            @Override
            public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG, "" + reasonPOJO.getMessage());
                    if (reasonPOJO.getOK()) {
                        if (dialogEditSlot != null){
                            dialogEditSlot.dismiss();
                        }
                        EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_CANCEL));
                        Toast.makeText(getContext(), "" + reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                        // successDialog();
                    } else {
                        Toast.makeText(getContext(), " " + reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(Call<CommonPOJO> call, Throwable t) {
                progressHUD.dismiss();
                //  Toast.makeText(NotificationListActivity.this, "Getting Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getAddCalenderEvent(String e_title, String e_meetingReason, long lns_Time, long lne_Time) {
        title = e_title;
        meetingReason = e_meetingReason;
        lneTime = lne_Time;
        lnsTime = lns_Time;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.alert_dialog_yesno, null);
        TextView txt_add = view1.findViewById(R.id.txt_add);
        TextView txt_cancel = view1.findViewById(R.id.txt_cancel);
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                getPermission();
            }
        });
        dialogs.show();
    }

    public boolean getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR},
                        111);
            } else {
                getAddCalender();
                //return true;
            }
        } else {
            getAddCalender();
            // return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 111:
                getPermission();
                break;

        }
    }

    private void getAddCalender() {
        try {
            String[] proj =
                    new String[]{
                            CalendarContract.Instances._ID,
                            CalendarContract.Instances.BEGIN,
                            CalendarContract.Instances.END,
                            CalendarContract.Instances.EVENT_ID};
            Cursor cursor =
                    CalendarContract.Instances.query(getActivity().getContentResolver(),
                            proj, lnsTime, lneTime,title);
            if (cursor.getCount() > 0) {
                successDialog();
                // deal with conflict
              //  Toast.makeText(getActivity(), "Already exist", Toast.LENGTH_SHORT).show();
            } else {
//                TimeZone timeZone = TimeZone.getDefault();
//                ContentResolver cr = getActivity().getContentResolver();
//                ContentValues values = new ContentValues();
//                values.put(CalendarContract.Events.DTSTART, lnsTime);
//                values.put(CalendarContract.Events.DTEND, lneTime);
//                values.put(CalendarContract.Events.TITLE, title);
//                values.put(CalendarContract.Events.DESCRIPTION, "Meeting for "+ meetingReason);
//                values.put(CalendarContract.Events.CALENDAR_ID, 1);
//                values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
//                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
//                //  successDialog();
////            Uri eventUri = getActivity().getApplicationContext()
////                    .getContentResolver()
////                    .insert(uri, values);
//                //  successDialog();
//// get the event ID that is the last element in the Uri
//                long eventID = Long.parseLong(uri.getLastPathSegment());
//                Log.d(TAG, "eventID "+ eventID);
//                if (eventID != -1){
//                    successDialog();
//                }


                try {
                    if (Build.VERSION.SDK_INT >= 14) {
                        Intent intent = new Intent(Intent.ACTION_INSERT)
                                .setData(CalendarContract.Events.CONTENT_URI)
                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, lnsTime)
                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, lneTime)
                                .putExtra(CalendarContract.Events.TITLE, title)
                                .putExtra(CalendarContract.Events.DESCRIPTION, "Meeting for " + meetingReason)
                                //   .putExtra(Events.EVENT_LOCATION, "The gym")
                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                        //  .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
                        getContext().startActivity(intent);
                    } else {
                        //    Calendar cal = Calendar.getInstance();
                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra("beginTime", lnsTime);
                        intent.putExtra("allDay", true);
                        intent.putExtra("rrule", "FREQ=YEARLY");
                        intent.putExtra("endTime", lneTime);
                        intent.putExtra("title", title);
                        getContext().startActivity(intent);
                    }
                } catch (Exception e){
                    e.getMessage();
                }
            }
        } catch (Exception e){
            Log.d(TAG, " "+  e.getLocalizedMessage());
            e.getMessage();
        }


    }

    public void successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_request_meeting_success, null);
        TextView label_close = view1.findViewById(R.id.label_close);
        TextView label_title = view1.findViewById(R.id.label_title);

        label_title.setText("This meeting is already added to your Calender.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        dialogs.show();
    }

}