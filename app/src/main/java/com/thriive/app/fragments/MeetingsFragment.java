package com.thriive.app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.thriive.app.MeetingJoinActivity;
import com.thriive.app.R;
import com.thriive.app.adapters.RequestedAdapter;
import com.thriive.app.adapters.ScheduledAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonEntitySlotsPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.CommonStartMeetingPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.scrollingpagerindicator.ScrollingPagerIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.txt_noRequest)
    TextView txt_noRequest;
    @BindView(R.id.recycler_requested)
    RecyclerView recycler_requested;
    @BindView(R.id.recycler_scheduled)
    RecyclerView recycler_scheduled;
    @BindView(R.id.txt_noSchedule)
    TextView txt_noSchedule;
    @BindView(R.id.refresh_view)
    SwipeRefreshLayout refreshView;
    Unbinder unbinder;
    @BindView(R.id.txt_schedule)
    TextView txt_schedule;
    @BindView(R.id.txt_request)
    TextView txt_request;

    private LinearLayoutManager layoutManagerSchedule;
    private LinearLayoutManager layoutManagerRequested;
    private ScrollingPagerIndicator recyclerIndicator, recyclerIndicator1;
//    @BindView(R.id.txt_name)
//    TextView txt_name;


    private RequestedAdapter requestedAdapter;
    private ScheduledAdapter scheduledAdapter;

    private APIInterface apiInterface;
    private LoginPOJO.ReturnEntity loginPOJO;
    private SharedData sharedData;

    public static String TAG = MeetingsFragment.class.getName();
    private  String startTime, endTime, meetingCode, selectedDate;

    private KProgressHUD progressHUD;
    private CommonStartMeetingPOJO.MeetingDataPOJO meetingDataPOJO;

    private String schedule_date = "", request_date = "";
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

        //txt_name.setText("Welcome, " + loginPOJO.getFirstName());
        recyclerIndicator = view.findViewById(R.id.indicator_requster);
        recyclerIndicator1 = view.findViewById(R.id.indicator_schedule);

        refreshView.setOnRefreshListener(this);
        refreshView.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        refreshView.setRefreshing(false);


        layoutManagerSchedule = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        layoutManagerRequested = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);

        recycler_scheduled.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE || newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    int position = layoutManagerSchedule.findFirstVisibleItemPosition();
                    schedule_date = scheduledAdapter.getDate(position);
                    txt_schedule.setText(Utility.getScheduleMeetingDate(Utility.ConvertUTCToUserTimezone(schedule_date)));
                  // return;
                }
              //  Toast.makeText(getContext(), ""+layoutManagerSchedule.getPosition(), Toast.LENGTH_SHORT).show();

            }
        });



        recycler_requested.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE || newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    int position = layoutManagerRequested.findFirstVisibleItemPosition();
                    request_date = requestedAdapter.getDate(position);
                    txt_request.setText(Utility.getScheduleMeetingDate(Utility.ConvertUTCToUserTimezone(request_date)));
                    return;
                }
                if (recycler_requested == null) {
                    return;
                }
            }
            });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getScheduledMeeting();
        getMeetingRequest();
    }

    @Override
    public void onRefresh() {
        if (refreshView != null){
            refreshView.setRefreshing(true);
        }

        onResume();
    }

    private void getScheduledMeeting() {
        Call<CommonMeetingListPOJO> call = apiInterface.getScheduledMeeting(loginPOJO.getActiveToken(),
                loginPOJO.getRowcode());
        call.enqueue(new Callback<CommonMeetingListPOJO>() {
            @Override
            public void onResponse(Call<CommonMeetingListPOJO> call, Response<CommonMeetingListPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                  //  progressHUD.dismiss();
                    CommonMeetingListPOJO pojo = response.body();
                    Log.d(TAG,""+pojo.getMessage());
                    if (pojo != null){
                        if (pojo.getOK()) {
                            if (pojo.getMeetingList() != null){
                                scheduledAdapter = new ScheduledAdapter(getActivity(),MeetingsFragment.this,pojo.getMeetingList());
                                recycler_scheduled.setLayoutManager(layoutManagerSchedule);
                                recycler_scheduled.setAdapter(scheduledAdapter);
                                recyclerIndicator1.attachToRecyclerView(recycler_scheduled);
                                if (pojo.getMeetingList().size() == 0){
                                    txt_noSchedule.setVisibility(View.VISIBLE);
                                    txt_schedule.setText("");
                                }else {
                                    txt_noSchedule.setVisibility(View.GONE);
                                    schedule_date = scheduledAdapter.getDate(0);
                                    txt_schedule.setText(Utility.getScheduleMeetingDate(Utility.ConvertUTCToUserTimezone(schedule_date)));

                                }

                            }
                            Toast.makeText(getContext(), "Success "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failure "+pojo.getMessage(), Toast.LENGTH_SHORT).show();

                        }  // recycler_requested.setAdapter(requestedAdapter);

                    }
                }
            }
            @Override
            public void onFailure(Call<CommonMeetingListPOJO> call, Throwable t) {
              //  progressHUD.dismiss();
                Toast.makeText(getContext(), "Getting Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getMeetingRequest() {
        Call<PendingMeetingRequestPOJO> call = apiInterface.getPendingMeeting(loginPOJO.getActiveToken(),
                loginPOJO.getRowcode());
        call.enqueue(new Callback<PendingMeetingRequestPOJO>() {
            @Override
            public void onResponse(Call<PendingMeetingRequestPOJO> call, Response<PendingMeetingRequestPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    // progressHUD.dismiss();
                    PendingMeetingRequestPOJO pojo = response.body();
                    Log.d(TAG,""+pojo.getMessage());
                    if (pojo != null){
                        if (pojo.getOK()) {
                            if (pojo.getMeetingRequestList() != null){
                                requestedAdapter = new RequestedAdapter(getActivity(), MeetingsFragment.this,
                                        (ArrayList<PendingMeetingRequestPOJO.MeetingRequestList>) pojo.getMeetingRequestList());
                                recycler_requested.setLayoutManager(layoutManagerRequested);
                                recycler_requested.setAdapter(requestedAdapter);
                                recyclerIndicator.attachToRecyclerView(recycler_requested);
                            }
                        } else {
                            Toast.makeText(getContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        if (pojo.getMeetingRequestList() == null){
                            txt_noRequest.setVisibility(View.VISIBLE);
                            txt_request.setText("");
                        } else {
                            if (pojo.getMeetingRequestList().size() == 0)
                            {
                                txt_noRequest.setVisibility(View.VISIBLE);
                                txt_request.setText("");
                            } else {
                                txt_noRequest.setVisibility(View.GONE);
                                request_date = requestedAdapter.getDate(0);
                                txt_request.setText(Utility.getScheduleMeetingDate(Utility.ConvertUTCToUserTimezone(request_date)));

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
                Toast.makeText(getContext(), "Getting Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void startMeeting(int meeting_id) {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonStartMeetingPOJO> call = apiInterface.getMeetingStart(loginPOJO.getActiveToken(),
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
                             callMeeting();
                             Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
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



    public void getMeetingSlot(String m) {
        meetingCode = ""+m;
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonEntitySlotsPOJO> call = apiInterface.getEntitySlots(""+loginPOJO.getActiveToken(), ""+loginPOJO.getRowcode());
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
                            meetingAvailability(pojo.getEntitySlotList());

                        } else {
                            Toast.makeText(getContext(), "Failure "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }


                }
            }
            @Override
            public void onFailure(Call<CommonEntitySlotsPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(getContext(), "Getting Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void meetingAvailability(List<CommonEntitySlotsPOJO.EntitySlotList> entitySlotList) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_availability, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);

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

        if (entitySlotList != null){
            if (entitySlotList.size() == 0) {
                edit.setVisibility(View.GONE);
            } else if (entitySlotList.size() == 1){
                edit.setVisibility(View.VISIBLE);
                for (int i = 0; i < entitySlotList.size(); i++) {
                    CommonEntitySlotsPOJO.EntitySlotList slotList = entitySlotList.get(i);
                    layout2.setVisibility(View.VISIBLE);
                    txt_date2.setText(Utility.getSlotDate(Utility.ConvertUTCToUserTimezone(slotList.getSlotDate())));
                    txt_time2.setText(Utility.getSlotTime(Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime()),
                            Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime())));
                    startTime = Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime());
                    endTime = Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime());
//                startTime = slotList.getPlanStartTime();
//                endTime = slotList.getPlanEndTime();
                }
            } else {
                edit.setVisibility(View.VISIBLE);
                for (int i = 0; i < entitySlotList.size(); i++) {
                    CommonEntitySlotsPOJO.EntitySlotList slotList = entitySlotList.get(i);
                    if(i == 0){
                        layout1.setVisibility(View.VISIBLE);
                        txt_date1.setText(Utility.getSlotDate(Utility.ConvertUTCToUserTimezone(slotList.getSlotDate())));
                        txt_time1.setText(Utility.getSlotTime(Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime()),
                                Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime())));
                    } else if(i == 1){
                        layout2.setVisibility(View.VISIBLE);
                        txt_date2.setText(Utility.getSlotDate(Utility.ConvertUTCToUserTimezone(slotList.getSlotDate())));
                        txt_time2.setText(Utility.getSlotTime(Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime()),
                                Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime())));
                        startTime = Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime());
                        endTime = Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime());
//                    startTime = slotList.getPlanStartTime();
//                    endTime = slotList.getPlanEndTime();
                        //txt_time2.setText(slotList.getFromHour() + ":" + slotList.getFromMin() +"-" +slotList.getToHour() + ":" + slotList.getToMin());

                    } else if(i == 2){
                        layout3.setVisibility(View.VISIBLE);
                        txt_date3.setText(Utility.getSlotDate(Utility.ConvertUTCToUserTimezone(slotList.getSlotDate())));
                        txt_time3.setText(Utility.getSlotTime(Utility.ConvertUTCToUserTimezone(slotList.getPlanStartTime()),
                                Utility.ConvertUTCToUserTimezone(slotList.getPlanEndTime())));

                        // txt_time3.setText(slotList.getFromHour() + ":" + slotList.getFromMin() +"-" +slotList.getToHour() + ":" + slotList.getToMin());

                    }
                }
            }
        }


        layout1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                startTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(0).getPlanStartTime());
                endTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(0).getPlanEndTime());
//                startTime = entitySlotList.get(0).getPlanStartTime();
//                endTime = entitySlotList.get(0).getPlanEndTime();
                setUnSelectedDate(txt_date3,txt_time3, img_date3, img_time3, layout3);
                selectDate(txt_date1,txt_time1, img_date1, img_time1, layout1);
                setUnSelectedDate(txt_date2,txt_time2, img_date2, img_time2, layout2);

            }
        });


        layout2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
//                startTime = entitySlotList.get(1).getPlanStartTime();
//                endTime = entitySlotList.get(1).getPlanEndTime();
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
//                startTime = entitySlotList.get(2).getPlanStartTime();
//                endTime = entitySlotList.get(2).getPlanEndTime();
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

                    getResheduledMeeting();
                }
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();


    }

    private void selectDate(TextView textDate,  TextView textTime, ImageView imageDate, ImageView imageTime, LinearLayout linearLayout){
        textDate.setTextColor(getActivity().getResources().getColor(R.color.terracota));
        textTime.setTextColor(getActivity().getResources().getColor(R.color.terracota));
        imageDate.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_calender_t));
        imageTime.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_time_t));
        linearLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rectangle_tarccoto_outline));

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUnSelectedDate(TextView textDate,  TextView textTime, ImageView imageDate, ImageView imageTime, LinearLayout linearLayout){
        textDate.setTextColor(getActivity().getResources().getColor(R.color.darkGreyBlue));
        textTime.setTextColor(getActivity().getResources().getColor(R.color.darkGreyBlue));
        imageDate.setImageDrawable(getActivity().getDrawable(R.drawable.ic_calender));
        imageTime.setImageDrawable(getActivity().getDrawable(R.drawable.ic_time));
        linearLayout.setBackground(getActivity().getDrawable(R.drawable.reactangle_grey_outline));

    }

    public void meetingEditDate() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_date, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
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
                Toast.makeText(getContext(),date+ "/"+month+"/"+year + "  "+arg0.getDate(),Toast.LENGTH_LONG).show();
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
                dialog.dismiss();
                getResheduledMeeting();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();


    }

    private void getResheduledMeeting() {
        startTime = Utility.ConvertUserTimezoneToUTC(startTime);
        endTime  = Utility.ConvertUserTimezoneToUTC(endTime);
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getRescheduleMeeting(loginPOJO.getActiveToken(),
                meetingCode, loginPOJO.getRowcode(), startTime,endTime);
        call.enqueue(new Callback<CommonPOJO>() {
            @Override
            public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMessage());
                    if (reasonPOJO.getOK()) {
                        EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_CANCEL));
                        Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                       // successDialog();
                    } else {
                        Toast.makeText(getContext(), "Failure "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
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



}