package com.thriive.app.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.onesignal.OneSignal;
import com.thriive.app.MeetingJoinActivity;
import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.ExperienceListAdapter;
import com.thriive.app.adapters.MeetingDetailSlotAdapter;
import com.thriive.app.adapters.MeetingNewTimeAdapter;
import com.thriive.app.adapters.MeetingSelectTagAdapter;
import com.thriive.app.adapters.ProposeNewTimeSlotAdapter;
import com.thriive.app.adapters.RequestPagerAdapter;
import com.thriive.app.adapters.RescheduleTimeSlotAdapter;
import com.thriive.app.adapters.SchedulePagerAdapter;
import com.thriive.app.adapters.SelectedProposeSlotAdapter;
import com.thriive.app.adapters.SelectedRescheduleTimeSlot;
import com.thriive.app.adapters.SelectedSlotsAdapter;
import com.thriive.app.adapters.SlotListFragmentAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonEntitySlotsPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonRequestTimeSlots;
import com.thriive.app.models.CommonScheduleMeetingPOJO;
import com.thriive.app.models.CommonStartMeetingPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.MeetingDetailPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.SwipeController;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
    private AlertDialog dialogDetails ;
    LinearLayout layout_accept_enable;
    private BottomSheetDialog dialogEditSlot;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_CALENDAR
    };

    private APIInterface apiInterface;
    private LoginPOJO.ReturnEntity loginPOJO;
    private SharedData sharedData;

    public static final String TAG = MeetingsFragment.class.getName();
    private  String startTime ="", endTime="", meetingCode, selectedDate,currentDate, personaName, region_name, user_region = "",meeting_slot_id="",txtYear="",txtMonth="",txtDay;

    private  String  meetingReason, title, usertype;
    private long lnsTime, lneTime;
    private KProgressHUD progressHUD;
    private CommonStartMeetingPOJO.MeetingDataPOJO meetingDataPOJO;

    private ArrayList<CommonMeetingListPOJO.MeetingListPOJO> meetingListSchedule;
    private ArrayList<PendingMeetingRequestPOJO.MeetingRequestList> meetingListRequest;

    private String schedule_date = "", request_date = "", time_stamp = "";
    Calendar calendar = Calendar.getInstance();
    Date date1;
    String year1, month1, day, newDate;
    private SchedulePagerAdapter schedulePagerAdapter;
    private RequestPagerAdapter requestPagerAdapter;
    private SwipeController swipeController = null;
    private CleverTapAPI cleverTap;

    TextView txt_year;
    TextView txt_month;
    TextView txt_day;
    TextView txt_toaster;
    RecyclerView rv_select_time_slots;
    RecyclerView rv_time_slots;
    LinearLayout layout_empty_view;
    LinearLayout layout_confirm;
    LinearLayout layout_confirm_disabled;
    int giverID = 0;
    int requestorId = 0;

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
        cleverTap = CleverTapAPI.getDefaultInstance(getActivity().getApplicationContext());

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
                            + "api/AppHome/get-scheduled-meetings", loginPOJO.getActiveToken(),
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
            Call<PendingMeetingRequestPOJO> call = apiInterface.getPendingMeeting(sharedData.getStringData(SharedData.API_URL) + "api/AppHome/get-pending-meetings", loginPOJO.getActiveToken(),
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

    public void startMeeting(int meeting_id, String user_type) {
        HashMap<String, Object> meeting_join = new HashMap<String, Object>();
        meeting_join.put("meeting_request_id", meeting_join);
        meeting_join.put("usertype", user_type);
        cleverTap.pushEvent(Utility.User_Joins_Meeting,meeting_join);
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

    public void getMeetingSlot(CommonMeetingListPOJO.MeetingListPOJO item,String meetingCode, String s, String m, String r_name, String user_country, String userType) {
        this.meetingCode = ""+meetingCode;
        meetingReason = m;
        personaName = s;
        region_name = r_name;
        user_region = user_country;
        usertype = userType;
        giverID = item.getSel_meeting().getGiver_id();
        requestorId = item.getRequestorId();
        alertDialogForRechedule(item);
    }

    public void alertDialogForRechedule(CommonMeetingListPOJO.MeetingListPOJO item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_reschedule_confirm, null);
        TextView label_close = view1.findViewById(R.id.label_close);
        Button btn_reschedule = view1.findViewById(R.id.btn_reschedule);

        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        btn_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                rescheduleMeeting(item);
            }
        });
        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        dialogs.show();
    }

    public void rescheduleMeeting(CommonMeetingListPOJO.MeetingListPOJO item) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            final View view1 = layoutInflater.inflate(R.layout.dialog_rescedule_layout, null);
            ImageView img_close = view1.findViewById(R.id.img_close);
            layout_empty_view = view1.findViewById(R.id.layout_empty_view);
            layout_empty_view.setVisibility(View.VISIBLE);
            rv_time_slots = view1.findViewById(R.id.rv_time_slots);
            rv_time_slots.setVisibility(View.GONE);
            ImageView img_backward = view1.findViewById(R.id.img_backward);
            ImageView img_forward = view1.findViewById(R.id.img_forward);
            ImageView img_info = view1.findViewById(R.id.img_info);
            LinearLayout layout_circle = view1.findViewById(R.id.layout_circle);
            layout_confirm = view1.findViewById(R.id.layout_confirm);
            layout_confirm_disabled = view1.findViewById(R.id.layout_confirm_disabled);

            layout_confirm.setVisibility(View.GONE);
            layout_confirm_disabled.setVisibility(View.VISIBLE);

            txt_toaster = view1.findViewById(R.id.txt_toaster);

            img_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCustomToast(true);
                }
            });

            txt_year = view1.findViewById(R.id.txt_year);
            txt_month = view1.findViewById(R.id.txt_month);
            txt_day = view1.findViewById(R.id.txt_day);
            rv_select_time_slots = view1.findViewById(R.id.rv_select_time_slots);

            setDefaultDate();

            builder.setView(view1);
            dialogDetails = builder.create();
            dialogDetails.setCancelable(false);


            layout_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, Object> meeting_reschedule = new HashMap<String, Object>();
                    meeting_reschedule.put("meeting_request_id", meetingCode);
                    meeting_reschedule.put("meeting_start_datetime", startTime);
                    meeting_reschedule.put("usertype", usertype);
                    cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Reschedule, meeting_reschedule);
                    getResheduledMeeting();

                }
            });

            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDetails.dismiss();
                }
            });

            img_backward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    previousDate();
                }
            });

            img_forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextDate();
                }
            });

            layout_circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    meetingEditDate();
                }
            });

            dialogDetails.show();
        } catch(Exception e){
            e.getMessage();
        }
    }

    public void showCustomToast(boolean flag) {
        try {
            if (flag){
                txt_toaster.setVisibility(View.VISIBLE);
                TimeZone timeZone = TimeZone.getDefault();
                Log.d(TAG, "time zone "+ timeZone.getID());
                String  time_stamp = "Limit of 1 time per day. Times are based on the region you’ve selected. Time is displayed in "+timeZone.getDisplayName();
                txt_toaster.setText(time_stamp);
            }else{
                txt_toaster.setVisibility(View.VISIBLE);
                String  time_stamp = "This time is unavailable because it has already been allocated for a meeting.";
                txt_toaster.setText(time_stamp);
            }


        } catch(Exception e){
            e.getMessage();
        }

        txt_toaster.postDelayed(new Runnable() {
            public void run() {
                txt_toaster.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void setDefaultDate(){
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = df.format(calendar.getTime());
        currentDate = df.format(calendar.getTime());
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy", Locale.getDefault());
        txtYear = dfYear.format(calendar.getTime());

        SimpleDateFormat dfMM = new SimpleDateFormat("dd MMM", Locale.getDefault());
        txtMonth = dfMM.format(calendar.getTime());

        SimpleDateFormat dfDay = new SimpleDateFormat("EEE", Locale.getDefault());
        txtDay = dfDay.format(calendar.getTime());

        txt_year.setText(txtYear);
        txt_month.setText(txtMonth);
        txt_day.setText(txtDay);

        String utc_date = Utility.convertLocaleToUtc(selectedDate);
        if (utc_date != null && utc_date.length() > 0){
            get_meeting_time_slots(utc_date);
        }
    }

    public void meetingEditDate()  {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_select_slot_date, null);

        ImageView img_close = view1.findViewById(R.id.img_close);
        Button btn_done = view1.findViewById(R.id.btn_done);
        CalendarView calender_view = view1.findViewById(R.id.calender);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,+1);

        calender_view.setMinDate(calendar.getTime().getTime());
       // calender_view.setMinDate(new Date().getTime());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(selectedDate);
            calender_view.setDate(date.getTime(),true,true);


            try {
                SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = formatt.parse(selectedDate);
                calendar.setTime(date1);
                SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
                year1 = dfYear.format(date1);

                SimpleDateFormat dfMonth = new SimpleDateFormat("dd MMM");
                month1 = dfMonth.format(date1.getTime());

                SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
                day = dfDay.format(date1.getTime());

                txt_year.setText(""+year1);
                txt_month.setText(month1);
                txt_day.setText(day);

                newDate = selectedDate;

            } catch (ParseException e) {
                e.printStackTrace();
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("NEW_DATE", selectedDate);

        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);

        calender_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                month = month + 1;
                // output to log cat **not sure how to format year to two places here**
                newDate = year+"-"+month+"-"+date;
                Log.d("NEW_DATE", newDate);
//                selectedDate = newDate;
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    date1 = format.parse(newDate);
//                    calendar.setTime(date1);
                    SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
                    txtYear = dfYear.format(date1);

                    SimpleDateFormat dfMonth = new SimpleDateFormat("dd MMM");
                    txtMonth = dfMonth.format(date1.getTime());

                    SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
                    txtDay = dfDay.format(date1.getTime());

                    /*txt_year.setText(""+txtYear);
                    txt_month.setText(txtMonth);
                    txt_day.setText(txtDay);*/

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                if (year1 != null && month1 != null && day != null && newDate!= null && date1!= null) {
                    selectedDate = newDate;
                    calendar.setTime(date1);
                    txt_year.setText("" + txtYear);
                    txt_month.setText(txtMonth);
                    txt_day.setText(txtDay);
                }
                String utc_date = Utility.convertLocaleToUtc(selectedDate);
                if (utc_date != null && utc_date.length() > 0){
                    get_meeting_time_slots(utc_date);
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
            }
        });
        dialogs.show();

    }

    private void nextDate(){

        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = df.format(calendar.getTime());
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = format.parse(selectedDate);

            SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
            txtYear = dfYear.format(date1);

            SimpleDateFormat dfMonth = new SimpleDateFormat("dd MMM");
            txtMonth = dfMonth.format(date1.getTime());

            SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
            txtDay = dfDay.format(date1.getTime());

            txt_year.setText(""+txtYear);
            txt_month.setText(txtMonth);
            txt_day.setText(txtDay);

            String utc_date = Utility.convertLocaleToUtc(selectedDate);
            if (utc_date != null && utc_date.length() > 0){
                get_meeting_time_slots(utc_date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void previousDate(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date current_date = format.parse(currentDate);
            Date selected_date = format.parse(selectedDate);
            if (selected_date.before(current_date) || selected_date.equals(current_date)){
                Toast.makeText(getActivity(), "You can't select past date.", Toast.LENGTH_SHORT).show();
            }else {
                calendar.add(Calendar.DATE, -1);
                //calendar.se.setMinDate(System.currentTimeMillis() - 1000);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate = df.format(calendar.getTime());

                SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
                String year = dfYear.format(calendar.getTime());

                SimpleDateFormat dfMM = new SimpleDateFormat("dd MMM");
                String month = dfMM.format(calendar.getTime());

                SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
                String day = dfDay.format(calendar.getTime());

                txt_year.setText(year);
                txt_month.setText(month);
                txt_day.setText(day);

                String utc_date = Utility.convertLocaleToUtc(selectedDate);
                if (utc_date != null && utc_date.length() > 0){

                    get_meeting_time_slots(utc_date);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void get_meeting_time_slots(String utc_date) {
        try {
            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonRequestTimeSlots> call = apiInterface.getTimeSlotsForGiver(sharedData.getStringData(SharedData.API_URL) +
                    "api/MRCalls/get-time-slots-for-reschedule", loginPOJO.getActiveToken(), requestorId, giverID, utc_date);
            call.enqueue(new Callback<CommonRequestTimeSlots>() {
                @Override
                public void onResponse(Call<CommonRequestTimeSlots> call, Response<CommonRequestTimeSlots> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonRequestTimeSlots pojo = response.body();
                        progressHUD.dismiss();
                        if (pojo != null){
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo.getOK()) {
                                if (pojo.getSlotsListPOJOS().size() > 0 ){
                                    rv_select_time_slots.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                                    RescheduleTimeSlotAdapter meetingRequestTimeSlotAdapter = new RescheduleTimeSlotAdapter(getActivity(),MeetingsFragment.this,
                                            (ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>) pojo.getSlotsListPOJOS(),selectedDate);
                                    rv_select_time_slots.setAdapter(meetingRequestTimeSlotAdapter);
                                    meetingRequestTimeSlotAdapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(getActivity(), ""+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        progressHUD.dismiss();
                        Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<CommonRequestTimeSlots> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }

    public void setSelectedTimeSlot(CommonRequestTimeSlots.EntitySlotsListPOJO slot,String time){
        if (slot != null){
            layout_empty_view.setVisibility(View.GONE);
            rv_time_slots.setVisibility(View.VISIBLE);
            layout_confirm.setVisibility(View.VISIBLE);
            layout_confirm_disabled.setVisibility(View.GONE);
            startTime = slot.getSlot_from_date();
            ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> arrayList = new ArrayList<>();
            arrayList.add(slot);
//            rv_time_slots.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            rv_time_slots.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            SelectedRescheduleTimeSlot meetingRequestTimeSlotAdapter = new SelectedRescheduleTimeSlot(getActivity(),
                    (ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>) arrayList);
            rv_time_slots.setAdapter(meetingRequestTimeSlotAdapter);
            meetingRequestTimeSlotAdapter.notifyDataSetChanged();
        }
    }

//    public void meetingEditSlot(List<CommonEntitySlotsPOJO.EntitySlotList> entitySlotList) {
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_availability, null);
//        dialogEditSlot = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
//        Button btn_confirm = dialogView.findViewById(R.id.btn_newSlot);
//        ImageView img_close = dialogView.findViewById(R.id.img_close);
//        RecyclerView rv_slots = dialogView.findViewById(R.id.rv_slots);
//        ArrayList<CommonEntitySlotsPOJO.EntitySlotList> arrayList = new ArrayList<>();
//
//        if (entitySlotList.size() > 3){
//            for (int i = 0; i <= 2; i++) {
//                arrayList.add(entitySlotList.get(i));
//            }
//        } else {
//            arrayList.addAll(entitySlotList);
//        }
//
//        SlotListFragmentAdapter adapter  = new SlotListFragmentAdapter(getActivity(), MeetingsFragment.this,
//                (ArrayList<CommonEntitySlotsPOJO.EntitySlotList>) entitySlotList, "MEETING");
//        rv_slots.setLayoutManager(new LinearLayoutManager(getActivity(),
//                LinearLayoutManager.VERTICAL, false));
//        rv_slots.setAdapter(adapter);
//
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogEditSlot.dismiss();
//            }
//        });
//        btn_confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // dialogEditSlot.dismiss();
//                //  getMeetingSlote();
//                meetingEditDate();
//            }
//        });
//        dialogEditSlot.setContentView(dialogView);
//        dialogEditSlot.show();
//    }

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
                HashMap<String, Object> meeting_reschedule = new HashMap<String, Object>();
                meeting_reschedule.put("meeting_request_id", meetingCode);
                meeting_reschedule.put("meeting_start_datetime", startTime);
                meeting_reschedule.put("usertype", usertype);
                cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Reschedule, meeting_reschedule);
                getResheduledMeeting();
            }
        });
        dialogs.show();
    }

//    public void meetingEditDate() {
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_date, null);
//        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
//        Button btn_next = dialogView.findViewById(R.id.btn_next);
//        ImageView img_close = dialogView.findViewById(R.id.img_close);
//        CalendarView calender_view = dialogView.findViewById(R.id.calender);
//        calender_view.setMinDate(new Date().getTime());
//
//        selectedDate = DateFormat.format("yyyy-MM-dd", calender_view.getDate()).toString();
//        Log.d("NEW_DATE", selectedDate);
//        calender_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView arg0, int year, int month,
//                                            int date) {
//                month = month + 1;
//                // output to log cat **not sure how to format year to two places here**
//                String newDate = year+"-"+month+"-"+date;
//                Log.d("NEW_DATE", newDate);
//               // Toast.makeText(getContext(),date+ "/"+month+"/"+year + "  "+arg0.getDate(),Toast.LENGTH_LONG).show();
//                selectedDate = newDate;
//            }
//        });
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        btn_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                meetingEditTime();
//            }
//        });
//        dialog.setContentView(dialogView);
//        dialog.show();
//
//
//    }

//    public void meetingEditTime() {
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_time, null);
//        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
//        Button btn_confirm = dialogView.findViewById(R.id.btn_confirm);
//        ImageView img_close = dialogView.findViewById(R.id.img_close);
//        TimePicker time_picker = dialogView.findViewById(R.id.time_picker);
//        int hour = time_picker.getCurrentHour();
//        int min = time_picker.getCurrentMinute();
//        startTime = Utility.convertDate(selectedDate + " " + new StringBuilder().append(hour).append(":").append(min)
//                .append(":").append("00"));
//        Log.d(TAG, startTime);
//        endTime = Utility.getOneHour(startTime) ;
//        Log.d(TAG, endTime);
//
//        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
//                int hour = time_picker.getCurrentHour();
//                int min = time_picker.getCurrentMinute();
//                startTime = Utility.convertDate(selectedDate + " "+ new StringBuilder().append(hour).append(":").append(min)
//                        .append(":").append("00"));
//                Log.d(TAG, startTime);
//                endTime = Utility.getOneHour(startTime) ;
//                Log.d(TAG, endTime);
//            }
//        });
//
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        btn_confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(Utility.getCheckSlotTime(startTime)){
//                    Toast.makeText(getContext(), "Please choose current or future time.", Toast.LENGTH_LONG).show();
//                } else {
//                    //getResheduledMeeting();
//                    dialog.dismiss();
//
//                    meetingConfirmation(startTime, endTime);
//
//                }
//               // dialog.dismiss();
//               // getResheduledMeeting();
//            }
//        });
//        dialog.setContentView(dialogView);
//        dialog.show();
//
//
//    }

    private void getResheduledMeeting() {
        //startTime = Utility.ConvertUserTimezoneToUTC(startTime);
        //endTime = Utility.ConvertUserTimezoneToUTC(endTime);
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
                        if (dialogDetails != null){
                            dialogDetails.dismiss();
                        }
                        EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_CANCEL));
                        //Toast.makeText(getContext(), "" + reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                        successDialogForReshedule();
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

    public void getAddCalenderEvent(String meeting_code, String e_title, String e_meetingReason, long lns_Time, long lne_Time) {
        HashMap<String, Object> visitEvent = new HashMap<String, Object>();
        visitEvent.put("meeting_request_id", meeting_code);
        cleverTap.pushEvent(Utility.Clicked_Add_to_Calendar,visitEvent);

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

    public void setMeetingDetailFragment(PendingMeetingRequestPOJO.MeetingRequestList item) {
      if (item != null){
          MeetingDetailFragment.item = item;
          MeetingDetailFragment addPhotoBottomDialogFragment =
                  (MeetingDetailFragment) MeetingDetailFragment.newInstance();
          addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                  "MeetingDetailFragment");
      }
    }

    public void setMeetingNewTime(PendingMeetingRequestPOJO.MeetingRequestList item) {
        if (item != null){
            try {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
                LayoutInflater layoutInflater = this.getLayoutInflater();
                final View view1 = layoutInflater.inflate(R.layout.dialog_new_time, null);

                ImageView img_close = view1.findViewById(R.id.img_close);
                TextView label_date = view1.findViewById(R.id.label_date);
                TextView txt_persona = view1.findViewById(R.id.txt_persona);
                RecyclerView rv_tags = view1.findViewById(R.id.rv_tags);
                TextView txt_reason = view1.findViewById(R.id.txt_reason);
                TextView txt_tags = view1.findViewById(R.id.txt_tags);
                TextView txt_support = view1.findViewById(R.id.txt_support);
                ImageButton img_accept_step2_enable = view1.findViewById(R.id.img_accept_step2_enable);
                RecyclerView rv_slots = view1.findViewById(R.id.rv_slots);
                layout_accept_enable = view1.findViewById(R.id.layout_accept_enable);
                layout_accept_enable.setVisibility(View.GONE);

                    txt_persona.setText("with "+item.getGiverPersonaName());
                    txt_reason.setText("For "+item.getReasonName());
                    //txt_tags.setText(""+item.getMeetingLabel());
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.addAll(item.getDomainTags());
                    arrayList.addAll(item.getSubDomainTags());
                    arrayList.addAll(item.getExpertiseTags());
                    arrayList.addAll(item.getCountryTags());
                    ArrayList<String> finalArrayList = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (i <= 3){
                            finalArrayList.add(arrayList.get(i));
                        }
                    }
                    FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getActivity());
                    rv_tags.setLayoutManager(gridLayout );
                    if (finalArrayList.size() > 0){
                        txt_tags.setVisibility(View.VISIBLE);
                    } else {
                        txt_tags.setVisibility(View.GONE);
                    }
                    rv_tags.setAdapter(new ExperienceAdapter(getActivity(), finalArrayList));

                    rv_slots.setLayoutManager(new GridLayoutManager(getActivity(), 3));

                    ArrayList<MeetingDetailPOJO> reverse_new_time = reverseArrayList((ArrayList<MeetingDetailPOJO>) item.getSlot_list());

                      ArrayList<MeetingDetailPOJO> dialog_new_time = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            MeetingDetailPOJO data = reverse_new_time.get(i);
                            dialog_new_time.add(data);
                        }

                        ArrayList<MeetingDetailPOJO> reverse__time = reverseArrayList((ArrayList<MeetingDetailPOJO>) dialog_new_time);

                    if (reverse__time.size() > 0){
                        rv_slots.setAdapter(new MeetingNewTimeAdapter(getActivity(), MeetingsFragment.this,
                                (ArrayList<MeetingDetailPOJO>) reverse__time));
                    }

                    //txt_details.setText(""+item.getSel_meeting().getReq_text());
                label_date.setText("Request sent on "+Utility.ConvertUTCToUserTimezoneForSlot(item.getRequestDate()));
                builder.setView(view1);
                dialogDetails = builder.create();
                dialogDetails.setCancelable(false);

                img_accept_step2_enable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getAcceptMeeting(item,item.getSel_meeting().getMeeting_code(),true);
                    }
                });
                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDetails.dismiss();
                    }
                });

                txt_support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        dialogDetails.dismiss();
                        dialogThriiveSupport(item,false);
                    }
                });

                dialogDetails.show();
            } catch(Exception e){
                e.getMessage();
            }
        }
    }


    public void acceptMeetingNewTime(MeetingDetailPOJO item){
        ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> selectItem = new ArrayList<>();
        if (item != null){
            layout_accept_enable.setVisibility(View.VISIBLE);
             meeting_slot_id =  ""+item.getMeeting_slot_id();
             startTime =  item.getSlot_from_date();
             endTime = item.getSlot_to_date();
        }
    }

    private void getAcceptMeeting(PendingMeetingRequestPOJO.MeetingRequestList item,String meeting_code, boolean flag) {
        try {
            if (flag){
                startTime = Utility.ConvertUserTimezoneToUTC(startTime);
                endTime  = Utility.ConvertUserTimezoneToUTC(endTime);
            }

            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.getRequestorAction(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/requestor-action", loginPOJO.getActiveToken(),
                    meeting_code, loginPOJO.getRowcode(),flag,meeting_slot_id ,startTime, endTime);
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
                                if (dialogDetails != null){
                                    dialogDetails.dismiss();
                                }
                                if (flag){
                                    successDialogAccept();
                                }else {
                                    sentEmailToThriiveSupport(item);
                                }

                            } else {
                                Toast.makeText(getActivity(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.getMessage();
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(getActivity(), " " +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }

    public void dialogThriiveSupport(PendingMeetingRequestPOJO.MeetingRequestList item, boolean flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_thriive_support, null);
        TextView label_close = view1.findViewById(R.id.label_close);
        ImageView img_02 = view1.findViewById(R.id.img_02);



        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        img_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                getAcceptMeeting(item,item.getSel_meeting().getMeeting_code(),false);
            }
        });
        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        dialogs.show();
    }

    public void sentEmailToThriiveSupport(PendingMeetingRequestPOJO.MeetingRequestList item){
        if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
            if (item.getGiver_email_id().equals("")){
                Toast.makeText(getActivity(), "Sorry email not found", Toast.LENGTH_SHORT).show();
            } else {
                HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                visitEvent.put("meeting_request_id", item.getSel_meeting().getMeeting_code());
                cleverTap.pushEvent(Utility.Clicked_Matched_Users_Email,visitEvent);
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ item.getGiver_email_id()});
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ "admin@thriive.app"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                    final PackageManager pm = getActivity().getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                    ResolveInfo best = null;
                    for(final ResolveInfo info : matches)
                        if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                            best = info;
                    if (best != null)
                        emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                    startActivity(emailIntent);
                } catch (Exception e){
                    e.getMessage();
                }

            }

        } else {
            if (item.getRequestor_email_id().equals("")){
                Toast.makeText(getActivity(), "Sorry email not found", Toast.LENGTH_SHORT).show();

            } else {
                HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                visitEvent.put("meeting_request_id", item.getSel_meeting().getMeeting_code());
                cleverTap.pushEvent(Utility.Clicked_Matched_Users_Email,visitEvent);
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ item.getRequestor_email_id()});
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ "admin@thriive.app"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                    final PackageManager pm = getActivity().getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                    ResolveInfo best = null;
                    for(final ResolveInfo info : matches)
                        if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                            best = info;
                    if (best != null)
                        emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                    startActivity(emailIntent);
                } catch (Exception e){
                    e.getMessage();
                }

            }
        }
    }

    public void successDialogAccept() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
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

    public void successDialogForReshedule() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_meeting_reschedule_confirmed, null);
        TextView label_close = view1.findViewById(R.id.label_close);
        TextView label_title = view1.findViewById(R.id.label_title);

        label_title.setText(getActivity().getResources().getText(R.string.msg_reshedule));
        //    tv_msg.setText("Session Added Successfully.");
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


    public ArrayList<MeetingDetailPOJO> reverseArrayList(ArrayList<MeetingDetailPOJO> alist) {
        ArrayList<MeetingDetailPOJO> revArrayList = new ArrayList<MeetingDetailPOJO>();
        for (int i = alist.size() - 1; i >= 0; i--) {
            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }
        return revArrayList;
    }
}