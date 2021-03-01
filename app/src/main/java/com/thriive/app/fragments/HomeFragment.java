package com.thriive.app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.onesignal.OneSignal;
import com.thriive.app.HomeActivity;
import com.thriive.app.R;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.MeetingNewTimeAdapterForActivity;
import com.thriive.app.adapters.MeetingNewTimeAdapterForHomeFragment;
import com.thriive.app.adapters.RequesterListAdapter;
import com.thriive.app.adapters.ScheduleListAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonHomePOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonRequestTimeSlots;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.MeetingDetailPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thriive.app.utilities.Utility.checkInternet;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RequesterListAdapter requesterListAdapter;
    private ScheduleListAdapter scheduleListAdapter;

    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList = new ArrayList<>();


    @BindView(R.id.recycler_requester)
    RecyclerView recyclerRequester;
    @BindView(R.id.recycler_schedule)
    RecyclerView recyclerSchedule;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.refresh_view)
    SwipeRefreshLayout refreshView;
    @BindView(R.id.layout_noMeeting)
    RelativeLayout layout_noMeeting;
    @BindView(R.id.layout_data)
    LinearLayout layout_data;
    @BindView(R.id.txt_Nname)
    TextView txt_Nname;
    @BindView(R.id.txt_noRequest)
    TextView txt_noRequest;
    @BindView(R.id.txt_noSchedule)
    TextView txt_noSchedule;
    private LoginPOJO.ReturnEntity loginPOJO;
    Unbinder unbinder;
    private APIInterface apiInterface;
    private static String TAG = HomeFragment.class.getName();
    private KProgressHUD progressHUD;

    private SharedData sharedData;
    private String UUID, time_stamp = "";
    private CleverTapAPI cleverTap;
    private AlertDialog dialogDetails;
    LinearLayout layout_accept_enable;
    private String meeting_slot_id="",startTime, endTime;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance() {
        Fragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        requesterPOJOArrayList.add(new CommonRequesterPOJO());
        requesterPOJOArrayList.add(new CommonRequesterPOJO());

        apiInterface = APIClient.getApiInterface();
        cleverTap = CleverTapAPI.getDefaultInstance(getActivity());
        loginPOJO  = Utility.getLoginData(getContext());

        sharedData = new SharedData(getActivity());

       // getMeetingRequest();

      //  getPendingRequest();

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



        UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();


        ArrayList arrayList  = new ArrayList();
        arrayList.add(new CommonMeetingListPOJO().getMeetingList());
        arrayList.add(new CommonMeetingListPOJO().getMeetingList());

       // scheduleListAdapter = new ScheduleListAdapter(getActivity(),arrayList);
        //recyclerSchedule.setAdapter(scheduleListAdapter);

        loginPOJO = Utility.getLoginData(getContext());
        txt_name.setText(""+loginPOJO.getFirstName());
        txt_Nname.setText(""+loginPOJO.getFirstName());
        refreshView.setOnRefreshListener(this);
        refreshView.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
       // cAdapter.removeAll();
        refreshView.setRefreshing(false);

        return  view;
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

    @SuppressLint("NewApi")
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
        if (event.getEvent() == Utility.END_CALL_DIALOG){
            Toast.makeText(getContext(), "ended", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "event  " +  event.getMeeting_id());
          //  ((HomeActivity)getActivity()).showMeetingDialog(event.getMeeting_id());
            //showMeetingDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshView != null){
            refreshView.setRefreshing(true);
        }
        NetworkInfo networkInfo = checkInternet(getActivity());
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

            getMeetingHome();
        } else {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMeetingHome() {
        TimeZone timeZone = TimeZone.getDefault();
        Log.d(TAG, "time zone "+ timeZone.getID());
        UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
        if (UUID  == null) {
            UUID = "";
        }
        Log.d(TAG, " token "+ sharedData.getStringData(SharedData.PUSH_TOKEN));
        Call<CommonHomePOJO> call = apiInterface.getMeetingHome(sharedData.getStringData(SharedData.API_URL) + "api/AppHome/get-meetings-home", loginPOJO.getActiveToken(),
                loginPOJO.getRowcode(),  UUID, ""+timeZone.getID(), time_stamp);
        call.enqueue(new Callback<CommonHomePOJO>() {
            @Override
            public void onResponse(Call<CommonHomePOJO> call, Response<CommonHomePOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, " "+ response.toString());
             //       progressHUD.dismiss();
                    CommonHomePOJO pojo = response.body();
                    try {
                        Log.d(TAG,""+pojo.getMessage());
                        if (pojo != null){
                            if (pojo.getOK()) {
                                if (pojo.getMeetingScheduledList() != null){
                                    scheduleListAdapter = new ScheduleListAdapter(getActivity(), (ArrayList<CommonMeetingListPOJO.MeetingListPOJO>) pojo.getMeetingScheduledList());
                                    recyclerSchedule.setAdapter(scheduleListAdapter);
                                }

                                if (pojo.getMeetingRequestList() != null) {
                                    requesterListAdapter = new RequesterListAdapter(getActivity(),HomeFragment.this, (ArrayList<PendingMeetingRequestPOJO.MeetingRequestList>) pojo.getMeetingRequestList());
                                    recyclerRequester.setAdapter(requesterListAdapter);
                                }

                                if (pojo.getMeetingRequestList().size() == 0 && pojo.getMeetingScheduledList().size() == 0){
                                    layout_data.setVisibility(View.VISIBLE);
                                    layout_noMeeting.setVisibility(View.GONE);
                                } else {
                                    layout_data.setVisibility(View.VISIBLE);
                                    layout_noMeeting.setVisibility(View.GONE);
                                }

                                if (pojo.getMeetingScheduledList().size() == 0){
                                    txt_noSchedule.setVisibility(View.VISIBLE);
                                } else {
                                    txt_noSchedule.setVisibility(View.GONE);
                                }

                                if (pojo.getMeetingRequestList().size() == 0){
                                    txt_noRequest.setVisibility(View.VISIBLE);
                                } else {
                                    txt_noRequest.setVisibility(View.GONE);
                                }
                                ((HomeActivity)getActivity()).setNoti(pojo.getPendingRequestCount());
                                // recycler_requested.setAdapter(requestedAdapter);
                               // Toast.makeText(getContext(), "Success "+pojo.getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }


                        if(refreshView != null) {
                            refreshView.setRefreshing(false);
                        }
                    } catch (Exception e){
                        e.getMessage();
                    }

                }
            }
            @Override
            public void onFailure(Call<CommonHomePOJO> call, Throwable t) {
             //   progressHUD.dismiss();
               // Toast.makeText(getContext(), ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        onResume();
    }

    public void setMeetingNewTime(PendingMeetingRequestPOJO.MeetingRequestList item) {
        if (item != null){
            try {
                if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))){
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

                    txt_persona.setText("with "+item.getSel_meeting().getRequestor_name());
                    txt_reason.setText("Meeting for "+item.getReasonName());
                    //txt_tags.setText(""+item.getMeetingLabel());
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.addAll(item.getDomainTags());
                    arrayList.addAll(item.getSubDomainTags());
                    arrayList.addAll(item.getExpertiseTags());
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
                        rv_slots.setAdapter(new MeetingNewTimeAdapterForHomeFragment(getActivity(),HomeFragment.this,
                                (ArrayList<MeetingDetailPOJO>) reverse__time));
                    }

                    //txt_details.setText(""+item.getSel_meeting().getReq_text());
                    label_date.setText("Request sent "+Utility.ConvertUTCToUserTimezoneForSlot(item.getSel_meeting().getRequest_date()));
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
                            dialogThriiveSupport(item,item.getSel_meeting().getMeeting_code(),false);
                        }
                    });

                    dialogDetails.show();
                }else {
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


                    txt_persona.setText("with "+item.getSel_meeting().getGiver_name());
                    txt_reason.setText("Meeting for "+item.getSel_meeting().getReason_name());
                    //txt_tags.setText(""+item.getMeetingLabel());
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.addAll(item.getDomainTags());
                    arrayList.addAll(item.getSubDomainTags());
                    arrayList.addAll(item.getExpertiseTags());
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
                        rv_slots.setAdapter(new MeetingNewTimeAdapterForHomeFragment(getActivity(),HomeFragment.this,
                                (ArrayList<MeetingDetailPOJO>) reverse__time));
                    }

                    //txt_details.setText(""+item.getSel_meeting().getReq_text());
                    label_date.setText("Request sent "+Utility.ConvertUTCToUserTimezoneForSlot(item.getSel_meeting().getRequest_date()));
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
                            dialogThriiveSupport(item,item.getSel_meeting().getMeeting_code(),false);
                        }
                    });

                    dialogDetails.show();
                }


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

    private void getAcceptMeeting(PendingMeetingRequestPOJO.MeetingRequestList item, String meeting_code, boolean flag) {
        try {
            startTime = Utility.ConvertUserTimezoneToUTC(startTime);
            endTime  = Utility.ConvertUserTimezoneToUTC(endTime);

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
                                //   Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
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
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ item.getGiver_email_id()});
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
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ item.getRequestor_email_id()});
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
                getMeetingHome();
            }
        });
        dialogs.show();
    }

    public void dialogThriiveSupport(PendingMeetingRequestPOJO.MeetingRequestList item,String meeting_code, boolean flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_thriive_support, null);
        TextView label_close = view1.findViewById(R.id.label_close);
        ImageView img_02 = view1.findViewById(R.id.img_02);

        img_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAcceptMeeting(item,meeting_code,false);
            }
        });

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