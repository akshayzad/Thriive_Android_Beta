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
    private RequestedAdapter requestedAdapter;
    private ScheduledAdapter scheduledAdapter;

    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList = new ArrayList<>();


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

    APIInterface apiInterface;
    private LoginPOJO loginPOJO;
    private SharedData sharedData;

    public static String TAG = MeetingsFragment.class.getName();
    private  String startTime, endTime, meetingCode, selectedDate;

    private ScrollingPagerIndicator recyclerIndicator, recyclerIndicator1;
    private  KProgressHUD progressHUD;
    private CommonStartMeetingPOJO.MeetingDataPOJO meetingDataPOJO;

    private String schedule_date = "";
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

    }


    public static Fragment newInstance() {
        Fragment fragment = new MeetingsFragment();
        return fragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_meeetings, container, false);
        unbinder = ButterKnife.bind(this, view);

        loginPOJO = Utility.getLoginData(getContext());
        apiInterface = APIClient.getApiInterface();

        sharedData = new SharedData(getContext());
        requesterPOJOArrayList.add(new CommonRequesterPOJO());
        requesterPOJOArrayList.add(new CommonRequesterPOJO());
        requesterPOJOArrayList.add(new CommonRequesterPOJO());

        recyclerIndicator = view.findViewById(R.id.indicator_requster);
        recyclerIndicator1 = view.findViewById(R.id.indicator_schedule);
     //   getMeetingRequest();
       // getPendingRequest();
        refreshView.setOnRefreshListener(this);
        refreshView.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        // cAdapter.removeAll();
        refreshView.setRefreshing(false);

        Log.d(TAG, loginPOJO.getReturnEntity().getActiveToken()   + "  " + loginPOJO.getReturnEntity().getRowcode());



//        scheduledAdapter = new ScheduledAdapter(getActivity(),MeetingsFragment.this,arrayList);
//        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),
//                LinearLayoutManager.HORIZONTAL, false);
//        recycler_scheduled.setLayoutManager(layoutManager1);
//        recycler_scheduled.setAdapter(scheduledAdapter);
//        recyclerIndicator1.attachToRecyclerView(recycler_scheduled);

        recyclerIndicator.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (schedule_date.equals(scheduledAdapter.getDate(i))){

                } else {

                    Toast.makeText(getContext(), ""+scheduledAdapter.getDate(i), Toast.LENGTH_SHORT).show();
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


    private void getScheduledMeeting() {
        Call<CommonMeetingListPOJO> call = apiInterface.getScheduledMeeting(loginPOJO.getReturnEntity().getActiveToken(),
                loginPOJO.getReturnEntity().getRowcode());
        call.enqueue(new Callback<CommonMeetingListPOJO>() {
            @Override
            public void onResponse(Call<CommonMeetingListPOJO> call, Response<CommonMeetingListPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                  //  progressHUD.dismiss();
                    CommonMeetingListPOJO pojo = response.body();
                    Log.d(TAG,""+pojo.getMessage());
                    if (pojo.getOK()) {
                        if (pojo.getMeetingList() != null){
                            scheduledAdapter = new ScheduledAdapter(getActivity(),MeetingsFragment.this,pojo.getMeetingList());
                            RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false);
                            recycler_scheduled.setLayoutManager(layoutManager1);
                            recycler_scheduled.setAdapter(scheduledAdapter);
                            recyclerIndicator1.attachToRecyclerView(recycler_scheduled);
                            if (pojo.getMeetingList().size() == 0){
                                txt_noSchedule.setVisibility(View.VISIBLE);
                            }else {
                                txt_noSchedule.setVisibility(View.GONE);
                                schedule_date = scheduledAdapter.getDate(0);
                            }

                        }

                        // recycler_requested.setAdapter(requestedAdapter);
                        Toast.makeText(getContext(), "Success "+pojo.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Failure "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void startMeeting(int meeting_id) {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonStartMeetingPOJO> call = apiInterface.getMeetingStart(loginPOJO.getReturnEntity().getActiveToken(),
                meeting_id, true, loginPOJO.getReturnEntity().getRowcode());
        call.enqueue(new Callback<CommonStartMeetingPOJO>() {
            @Override
            public void onResponse(Call<CommonStartMeetingPOJO> call, Response<CommonStartMeetingPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d("TAG", response.toString());
                    CommonStartMeetingPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                     Log.d(TAG,""+reasonPOJO.getMessage());
                    if (reasonPOJO.getOK()) {
                        meetingDataPOJO = reasonPOJO.getMeetingData();
                        sharedData.addStringData(SharedData.MEETING_TOKEN, meetingDataPOJO.getMeetingToken());
                        callMeeting();
                        Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        //   Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
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

        Log.d(TAG,  meetingDataPOJO.getPlanStartTime() + " "+  meetingDataPOJO.getPlanEndTime());
        Log.d(TAG,  meetingDataPOJO.getActualStartTime() + " "+  meetingDataPOJO.getAcutalEndTime());
        startActivityForResult(intent,123);
    }


    private void getMeetingRequest() {
//        progressHUD = KProgressHUD.create(getContext())
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setCancellable(false)
//                .show();
        Call<PendingMeetingRequestPOJO> call = apiInterface.getPendingMeeting(loginPOJO.getReturnEntity().getActiveToken(),
                loginPOJO.getReturnEntity().getRowcode());
        call.enqueue(new Callback<PendingMeetingRequestPOJO>() {
            @Override
            public void onResponse(Call<PendingMeetingRequestPOJO> call, Response<PendingMeetingRequestPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                   // progressHUD.dismiss();
                    PendingMeetingRequestPOJO pojo = response.body();
                    Log.d(TAG,""+pojo.getMessage());
                    if (pojo.getOK()) {
                        requestedAdapter = new RequestedAdapter(getActivity(), MeetingsFragment.this,
                                (ArrayList<PendingMeetingRequestPOJO.MeetingRequestList>) pojo.getMeetingRequestList());
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                                LinearLayoutManager.HORIZONTAL, false);
                        recycler_requested.setLayoutManager(layoutManager);
                        recycler_requested.setAdapter(requestedAdapter);

                        recyclerIndicator.attachToRecyclerView(recycler_requested);
                       // recycler_requested.setAdapter(requestedAdapter);
//                        Toast.makeText(getContext(), "Success "+pojo.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Failure "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    if (pojo.getMeetingRequestList().size() == 0)
                    {
                        txt_noRequest.setVisibility(View.VISIBLE);
                    } else {
                        txt_noRequest.setVisibility(View.GONE);
                    }
                    if (refreshView != null)
                    {
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

    public void getMeetingSlot(String m) {
        meetingCode = ""+m;
        progressHUD = KProgressHUD.create(getActivity())
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
                        Toast.makeText(getContext(), "Failure "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
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
//                startTime = slotList.getPlanStartTime();
//                endTime = slotList.getPlanEndTime();
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
//                    startTime = slotList.getPlanStartTime();
//                    endTime = slotList.getPlanEndTime();
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

        layout1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                startTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(0).getPlanStartTime());
                endTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(0).getPlanEndTime());
//                startTime = entitySlotList.get(0).getPlanStartTime();
//                endTime = entitySlotList.get(0).getPlanEndTime();
                layout1.setBackground(getActivity().getDrawable(R.drawable.rectangle_tarccoto_outline));
                layout2.setBackground(getActivity().getDrawable(R.drawable.rectangle_grey_half_outline));
                layout3.setBackground(getActivity().getDrawable(R.drawable.reactangle_grey_outline));

                img_date1.setImageDrawable(getActivity().getDrawable(R.drawable.ic_calender_t));
                img_date2.setImageDrawable(getActivity().getDrawable(R.drawable.ic_calender));
                img_date3.setImageDrawable(getActivity().getDrawable(R.drawable.ic_calender));

                img_time1.setImageDrawable(getActivity().getDrawable(R.drawable.ic_time_t));
                img_time2.setImageDrawable(getActivity().getDrawable(R.drawable.ic_time));
                img_time3.setImageDrawable(getActivity().getDrawable(R.drawable.ic_time));

                txt_date1.setTextColor(getActivity().getColor(R.color.terracota));
                txt_date2.setTextColor(getActivity().getColor(R.color.darkGreyBlue));
                txt_date3.setTextColor(getActivity().getColor(R.color.darkGreyBlue));


                txt_time1.setTextColor(getActivity().getColor(R.color.terracota));
                txt_time2.setTextColor(getActivity().getColor(R.color.darkGreyBlue));
                txt_time3.setTextColor(getActivity().getColor(R.color.darkGreyBlue));

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
            //    startTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(1).getPlanStartTime());
             //   endTime = Utility.ConvertUTCToUserTimezone(entitySlotList.get(1).getPlanEndTime());
                layout1.setBackground(getActivity().getDrawable(R.drawable.reactangle_grey_outline));
                layout2.setBackground(getActivity().getDrawable(R.drawable.half_outline_tarracco));
                layout3.setBackground(getActivity().getDrawable(R.drawable.reactangle_grey_outline));

                img_date1.setImageDrawable(getActivity().getDrawable(R.drawable.ic_calender));
                img_date2.setImageDrawable(getActivity().getDrawable(R.drawable.ic_calender_t));
                img_date3.setImageDrawable(getActivity().getDrawable(R.drawable.ic_calender));

                img_time1.setImageDrawable(getActivity().getDrawable(R.drawable.ic_time));
                img_time2.setImageDrawable(getActivity().getDrawable(R.drawable.ic_time_t));
                img_time3.setImageDrawable(getActivity().getDrawable(R.drawable.ic_time));

                txt_date1.setTextColor(getActivity().getColor(R.color.darkGreyBlue));
                txt_date2.setTextColor(getActivity().getColor(R.color.terracota));
                txt_date3.setTextColor(getActivity().getColor(R.color.darkGreyBlue));


                txt_time1.setTextColor(getActivity().getColor(R.color.darkGreyBlue));
                txt_time2.setTextColor(getActivity().getColor(R.color.terracota));
                txt_time3.setTextColor(getActivity().getColor(R.color.darkGreyBlue));

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
                layout1.setBackground(getActivity().getDrawable(R.drawable.reactangle_grey_outline));
                layout2.setBackground(getActivity().getDrawable(R.drawable.rectangle_grey_half_outline));
                layout3.setBackground(getActivity().getDrawable(R.drawable.rectangle_tarccoto_outline));

                img_date1.setImageDrawable(getActivity().getDrawable(R.drawable.ic_calender));
                img_date2.setImageDrawable(getActivity().getDrawable(R.drawable.ic_calender));
                img_date3.setImageDrawable(getActivity().getDrawable(R.drawable.ic_calender_t));

                img_time1.setImageDrawable(getActivity().getDrawable(R.drawable.ic_time));
                img_time2.setImageDrawable(getActivity().getDrawable(R.drawable.ic_time));
                img_time3.setImageDrawable(getActivity().getDrawable(R.drawable.ic_time_t));

                txt_date1.setTextColor(getActivity().getColor(R.color.darkGreyBlue));
                txt_date2.setTextColor(getActivity().getColor(R.color.darkGreyBlue));
                txt_date3.setTextColor(getActivity().getColor(R.color.terracota));


                txt_time1.setTextColor(getActivity().getColor(R.color.darkGreyBlue));
                txt_time2.setTextColor(getActivity().getColor(R.color.darkGreyBlue));
                txt_time3.setTextColor(getActivity().getColor(R.color.terracota));

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
        Call<CommonPOJO> call = apiInterface.getRescheduleMeeting(loginPOJO.getReturnEntity().getActiveToken(),
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


    @Override
    public void onRefresh() {
        refreshView.setRefreshing(true);
        onResume();

    }
}