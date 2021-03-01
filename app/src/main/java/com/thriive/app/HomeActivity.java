package com.thriive.app;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.onesignal.OneSignal;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.MeetingNewTimeAdapter;
import com.thriive.app.adapters.MeetingNewTimeAdapterForActivity;
import com.thriive.app.adapters.RequestPagerAdapter;
import com.thriive.app.adapters.ViewPagerAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.fragments.HomeFragment;
import com.thriive.app.fragments.MeetingDetailFragment;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.fragments.MeetingsFragment;

import com.thriive.app.fragments.NewHomeFragment;
import com.thriive.app.fragments.NewRequstMeetingFragment;
import com.thriive.app.fragments.NotificationFragment;
import com.thriive.app.fragments.ProfileFragment;
import com.thriive.app.models.CommonHomePOJO;
import com.thriive.app.models.CommonMeetingCountPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonRequestTimeSlots;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.MeetingDetailPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.spacenavigation.SpaceItem;
import com.thriive.app.utilities.spacenavigation.SpaceNavigationView;
import com.thriive.app.utilities.spacenavigation.SpaceOnClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.layout_toolbar_blank)
    CardView layout_toolbar_blank;
    @BindView(R.id.layout_toolbar_with_icon)
    LinearLayout layout_toolbar_with_icon;
    @BindView(R.id.rootContainer)
    RelativeLayout rootContainer;
    @BindView(R.id.toolbar_name)
    TextView toolbar_name;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.space)
    SpaceNavigationView spaceNavigationView;
    @BindView(R.id.img_logo)
    ImageView img_logo;
    @BindView(R.id.txt_notification)
    TextView txt_notification;

    @BindView(R.id.space1)
    SpaceNavigationView spaceNavigationView1;

    private NotificationFragment notificationFragment;
    private ViewPagerAdapter viewPagerAdapter;
    private SharedData sharedData;

    private String meetingId = "", UUID = "", time_stamp = "", reason = "",meeting_slot_id="",startTime, endTime;
    private APIInterface apiInterface;
    private KProgressHUD progressHUD;
    private LoginPOJO.ReturnEntity loginPOJO;
    private static final String TAG = HomeActivity.class.getName();
    private CleverTapAPI cleverTap;
    private int rating_int = 0, isRelevantMatchSelect;
    boolean isDidntMeet = false, isRelevantMatch = false  , isDidntMeetSelect ;

    private AlertDialog dialogDetails;
    LinearLayout layout_accept_enable;
    Bundle bundle = null;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        apiInterface = APIClient.getApiInterface();
        notificationFragment = new NotificationFragment();

        sharedData = new SharedData(getApplicationContext());
        loginPOJO  = Utility.getLoginData(getApplicationContext());
        if (loginPOJO != null){
            sharedData.addIntData(SharedData.USER_ID, loginPOJO.getEntityId());
        }

        String mydate = "2020-10-06T16:30:00";
        String utc = "2020-10-06T16:30:00";
        Log.d(TAG,  "ConvertUserTimezoneToUTC " + Utility.ConvertUserTimezoneToUTC(mydate));
        Log.d(TAG,  "ConvertUTCToUserTimezone " + Utility.ConvertUTCToUserTimezone(utc));

        // popupMeetingRequest(pojo.getMeetingObject());

       // showMeetingDialog(meetingId);

        cleverTap = CleverTapAPI.getDefaultInstance(getApplicationContext());
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

        Log.d(TAG, " UUID "+ UUID);

        spaceNavigationView1.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        spaceNavigationView1.addSpaceItem(new SpaceItem("Meetings", R.drawable.ic_my_meetings));
        spaceNavigationView1.addSpaceItem(new SpaceItem("Requests", R.drawable.ic_notifications));
        spaceNavigationView1.addSpaceItem(new SpaceItem("Profile", R.drawable.ic_profile));
        spaceNavigationView1.setCentreButtonIconColorFilterEnabled(false);


        spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("Meetings", R.drawable.ic_my_meetings));
        spaceNavigationView.addSpaceItem(new SpaceItem("Requests", R.drawable.ic_notifications));
        spaceNavigationView.addSpaceItem(new SpaceItem("Profile", R.drawable.ic_profile));

        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_medium);
        spaceNavigationView.setFont(typeface);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
       // spaceNavigationView.showIconOnly();
        setupViewPager(viewPager);

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {

                cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Request_Initiated);
                getMeetingCount();

                // Toast.makeText(HomeActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex == 2){
                    layout_toolbar_blank.setVisibility(View.VISIBLE);
                    layout_toolbar_with_icon.setVisibility(View.GONE);
                    rootContainer.setBackgroundColor(getResources().getColor(R.color.color_requests_background));
                    toolbar_name.setText("Meeting Request Received");
                } else if (itemIndex == 3){
                    layout_toolbar_blank.setVisibility(View.VISIBLE);
                    layout_toolbar_with_icon.setVisibility(View.GONE);
                    rootContainer.setBackgroundColor(getResources().getColor(R.color.color_requests_background));
                    toolbar_name.setText("Account");
                }else {
                    layout_toolbar_blank.setVisibility(View.GONE);
                    layout_toolbar_with_icon.setVisibility(View.VISIBLE);
                    rootContainer.setBackground(getResources().getDrawable(R.drawable.home_background));
                }
                viewPager.setCurrentItem(itemIndex);
                //   Toast.makeText(HomeActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                //   Toast.makeText(HomeActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });

        if (getIntent().getStringExtra("intent_type").equals("NOTI")){
            if (getIntent().getStringExtra("view_type")!= null) {
                if (getIntent().getStringExtra("view_type").equals("NOTI")) {
                    bundle = new Bundle();
                    bundle.putString("intent_type", "NOTI");
                    notificationFragment.setArguments(bundle);
                    setupViewPager(viewPager);
                    viewPager.setCurrentItem(2);
                    spaceNavigationView.changeCurrentItem(2);
                    layout_toolbar_blank.setVisibility(View.VISIBLE);
                    layout_toolbar_with_icon.setVisibility(View.GONE);
                    rootContainer.setBackgroundColor(getResources().getColor(R.color.color_requests_background));
                    toolbar_name.setText("Meeting Request Received");
                }else {
                    cleverTap.pushEvent(Utility.Viewed_Alerts);
                    getMeetingById();
                }
            } else {
                cleverTap.pushEvent(Utility.Viewed_Alerts);
                getMeetingById();
            }

        }

        if (getIntent().getStringExtra("intent_type").equals("MEETING_DETAILS")){
            MeetingDetailsFragment meetingDetailsFragment =
                        (MeetingDetailsFragment) MeetingDetailsFragment.newInstance();
                meetingDetailsFragment.show(getSupportFragmentManager(), "MeetingDetailsFragment");
        }

        if (getIntent().getStringExtra("intent_type").equals("TIME")){
            cleverTap.pushEvent(Utility.Viewed_Alerts);
            getProPoseNewTime();
        }

        //showMeetingRatingDialog(meetingId);
    }

    private void getMeetingCount() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonMeetingCountPOJO> call = apiInterface.getMeetingCount(sharedData.getStringData(SharedData.API_URL)
                    + "api/Entity/get-request-count", loginPOJO.getActiveToken(), loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonMeetingCountPOJO>() {
                @Override
                public void onResponse(Call<CommonMeetingCountPOJO> call, Response<CommonMeetingCountPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        progressHUD.dismiss();
                        try {
                            CommonMeetingCountPOJO pojo = response.body();
                            Log.d(TAG,""+pojo.getDoneCount() + " " + pojo.getTotalCount());
                            sharedData.addIntData(SharedData.MEETING_TOTAL, pojo.getTotalCount());
                            sharedData.addIntData(SharedData.MEETING_DONE, pojo.getDoneCount());
                            if (pojo != null){
                                try {
                                    if (pojo.getOK()){
                                        if (pojo.getDoneCount() >= pojo.getTotalCount()){
                                            getMeetingExsaustedDialog(pojo.getTemplateMessage());
                                        } else {
                                            callFragment();
                                        }
                                    }
                                } catch (Exception e){
                                    progressHUD.dismiss();
                                    Log.d(TAG, " "+ e.getMessage());
                                }

                            }
                        } catch (Exception e){
                            progressHUD.dismiss();
                            e.getMessage();
                        }

                    }
                }
                @Override
                public void onFailure(Call<CommonMeetingCountPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(getApplicationContext(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            progressHUD.dismiss();
            e.getMessage();
        }

    }

    private void getMeetingExsaustedDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_success_alert, null);

        TextView txt_message = view1.findViewById(R.id.txt_message);
        TextView label_close = view1.findViewById(R.id.label_close);
        //    tv_msg.setText("Session Added Successfully.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(true);
        txt_message.setText(""+message);
        // rv_expertise.setLayoutManager(new FlexboxLayoutManager(NotificationListActivity.this) );
        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                //   ratingDialog();
            }
        });
        dialogs.show();
    }

    public void getLogoutApp() {
//        progressHUD = KProgressHUD.create(this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setCancellable(false)
//                .show();
        sharedData.addBooleanData(SharedData.isFirstVisit, false);
        sharedData.addBooleanData(SharedData.isLogged, false);
        sharedData.clearPref(getApplicationContext());
        Utility.clearLogin(getApplicationContext());
        Utility.clearMeetingDetails(getApplicationContext());
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // progressHUD.dismiss();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        }, 2000);

    }

    private void getMeetingById() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonMeetingPOJO> call = apiInterface.getMeetingById(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/get-meeting", loginPOJO.getActiveToken(),
                getIntent().getStringExtra("meeting_id"));
        call.enqueue(new Callback<CommonMeetingPOJO>() {
            @Override
            public void onResponse(Call<CommonMeetingPOJO> call, Response<CommonMeetingPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    progressHUD.dismiss();
                    try {
                        CommonMeetingPOJO pojo = response.body();
                        if (pojo != null){
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo.getOK()) {
                                Utility.saveMeetingDetailsData(getApplicationContext(), pojo.getMeetingObject());
                                MeetingDetailsFragment meetingDetailsFragment =
                                        (MeetingDetailsFragment) MeetingDetailsFragment.newInstance();
                                meetingDetailsFragment.show(getSupportFragmentManager(), "MeetingDetailsFragment");
                                // recycler_requested.setAdapter(requestedAdapter);

                            } else {
                                Toast.makeText(getApplicationContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e){
                        progressHUD.dismiss();
                        e.getMessage();
                    }


                }
            }
            @Override
            public void onFailure(Call<CommonMeetingPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProPoseNewTime() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonMeetingPOJO> call = apiInterface.getMeetingById(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/get-meeting", loginPOJO.getActiveToken(),
                getIntent().getStringExtra("meeting_id"));
        call.enqueue(new Callback<CommonMeetingPOJO>() {
            @Override
            public void onResponse(Call<CommonMeetingPOJO> call, Response<CommonMeetingPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    progressHUD.dismiss();
                    try {
                        CommonMeetingPOJO pojo = response.body();
                        if (pojo != null){
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo.getOK()) {

                                setMeetingNewTime(pojo.getMeetingObject());

                            } else {
                                Toast.makeText(getApplicationContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e){
                        progressHUD.dismiss();
                        e.getMessage();
                    }


                }
            }
            @Override
            public void onFailure(Call<CommonMeetingPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setMeetingNewTime(CommonMeetingListPOJO.MeetingListPOJO item) {
        if (item != null){
            try {
                if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.SheetDialog);
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

                    txt_persona.setText("with "+item.getGiverName());
                    txt_reason.setText("Meeting for "+item.getMeetingReason());
                    //txt_tags.setText(""+item.getMeetingLabel());
                    ArrayList<String> arrayList = new ArrayList<>();
                    /*arrayList.addAll(item.getRequestorDomainTags());
                    arrayList.addAll(item.getRequestorSubDomainTags());
                    arrayList.addAll(item.getRequestorExpertiseTags());*/
                    arrayList.add(item.getSel_meeting().getDomain_name());
                    arrayList.add(item.getSel_meeting().getSub_domain_name());
                    arrayList.add(item.getSel_meeting().getExpertise_name());
                    arrayList.add(item.getSel_meeting().getCountry_name());
                    ArrayList<String> finalArrayList = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (i <= 3 && !arrayList.get(i).equals("")){
                            finalArrayList.add(arrayList.get(i));
                        }
                    }

                    FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(HomeActivity.this);
                    rv_tags.setLayoutManager(gridLayout );
                    if (finalArrayList.size() > 0){
                        txt_tags.setVisibility(View.VISIBLE);
                    } else {
                        txt_tags.setVisibility(View.GONE);
                    }
                    rv_tags.setAdapter(new ExperienceAdapter(HomeActivity.this, finalArrayList));

                    rv_slots.setLayoutManager(new GridLayoutManager(HomeActivity.this, 3));

                    ArrayList<MeetingDetailPOJO> reverse_new_time = reverseArrayList((ArrayList<MeetingDetailPOJO>) item.getSlot_list());

                    ArrayList<MeetingDetailPOJO> dialog_new_time = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        MeetingDetailPOJO data = reverse_new_time.get(i);
                        dialog_new_time.add(data);
                    }

                    ArrayList<MeetingDetailPOJO> reverse__time = reverseArrayList((ArrayList<MeetingDetailPOJO>) dialog_new_time);

                    if (reverse__time.size() > 0){
                        rv_slots.setAdapter(new MeetingNewTimeAdapterForActivity(HomeActivity.this,
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.SheetDialog);
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


                    txt_persona.setText("with "+item.getGiverName());
                    txt_reason.setText("Meeting for "+item.getMeetingReason());
                    //txt_tags.setText(""+item.getMeetingLabel());
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.addAll(item.getGiverDomainTags());
                    arrayList.addAll(item.getGiverSubDomainTags());
                    arrayList.addAll(item.getGiverExpertiseTags());
                    ArrayList<String> finalArrayList = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (i <= 3){
                            finalArrayList.add(arrayList.get(i));
                        }
                    }
                    FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(HomeActivity.this);
                    rv_tags.setLayoutManager(gridLayout );
                    if (finalArrayList.size() > 0){
                        txt_tags.setVisibility(View.VISIBLE);
                    } else {
                        txt_tags.setVisibility(View.GONE);
                    }
                    rv_tags.setAdapter(new ExperienceAdapter(HomeActivity.this, finalArrayList));

                    rv_slots.setLayoutManager(new GridLayoutManager(HomeActivity.this, 3));

                    ArrayList<MeetingDetailPOJO> reverse_new_time = reverseArrayList((ArrayList<MeetingDetailPOJO>) item.getSlot_list());

                    ArrayList<MeetingDetailPOJO> dialog_new_time = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        MeetingDetailPOJO data = reverse_new_time.get(i);
                        dialog_new_time.add(data);
                    }

                    ArrayList<MeetingDetailPOJO> reverse__time = reverseArrayList((ArrayList<MeetingDetailPOJO>) dialog_new_time);

                    if (reverse__time.size() > 0){
                        rv_slots.setAdapter(new MeetingNewTimeAdapterForActivity(HomeActivity.this,
                                (ArrayList<MeetingDetailPOJO>) reverse__time));
                    }

                    //txt_details.setText(""+item.getSel_meeting().getReq_text());
                    label_date.setText("Request sent on "+Utility.ConvertUTCToUserTimezoneForSlot(item.getSel_meeting().getRequest_date()));
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

    private void getAcceptMeeting(CommonMeetingListPOJO.MeetingListPOJO item, String meeting_code, boolean flag) {
        try {
            startTime = Utility.ConvertUserTimezoneToUTC(startTime);
            endTime  = Utility.ConvertUserTimezoneToUTC(endTime);

            progressHUD = KProgressHUD.create(HomeActivity.this)
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
                                Toast.makeText(HomeActivity.this, " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            progressHUD.dismiss();
                            e.getMessage();
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(HomeActivity.this, " " +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            progressHUD.dismiss();
            e.getMessage();
        }
    }

    public void sentEmailToThriiveSupport(CommonMeetingListPOJO.MeetingListPOJO item){
        if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
            if (item.getGiverEmailId().equals("")){
                Toast.makeText(HomeActivity.this, "Sorry email not found", Toast.LENGTH_SHORT).show();
            } else {
                HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                visitEvent.put("meeting_request_id", item.getSel_meeting().getMeeting_code());
                cleverTap.pushEvent(Utility.Clicked_Matched_Users_Email,visitEvent);
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ item.getGiverEmailId()});
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ "admin@thriive.app"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                    final PackageManager pm = getPackageManager();
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
            if (item.getRequestorEmailId().equals("")){
                Toast.makeText(HomeActivity.this, "Sorry email not found", Toast.LENGTH_SHORT).show();

            } else {
                HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                visitEvent.put("meeting_request_id", item.getMeetingCode());
                cleverTap.pushEvent(Utility.Clicked_Matched_Users_Email,visitEvent);
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ item.getRequestorEmailId()});
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ "admin@thriive.app"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                    final PackageManager pm = getPackageManager();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.SheetDialog);
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

            }
        });
        dialogs.show();
    }

    public void dialogThriiveSupport(CommonMeetingListPOJO.MeetingListPOJO item,String meeting_code, boolean flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.SheetDialog);
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

    public void setNoti(int noti) {
        if (noti != 0){
            txt_notification.setText(""+noti);
//            txt_notification.setVisibility(View.VISIBLE);
            spaceNavigationView.showBadgeAtIndex(2, noti, getResources().getColor(R.color.black_01));
            spaceNavigationView.shouldShowFullBadgeText(false);
        } else {
            txt_notification.setVisibility(View.GONE);
            spaceNavigationView.hideBadgeAtIndex(2);
        }
    }

    public void setMeetingBadge(int noti) {
        if (noti != 0){
            spaceNavigationView.showBadgeAtIndex(1, noti, getResources().getColor(R.color.black_01));
        } else {
            spaceNavigationView.hideBadgeAtIndex(1);
            spaceNavigationView.shouldShowFullBadgeText(false);
        }

    }

    public void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter.addFragment(new HomeFragment());
        viewPagerAdapter.addFragment(new NewHomeFragment());
        viewPagerAdapter.addFragment(new MeetingsFragment());
        viewPagerAdapter.addFragment(notificationFragment);
        viewPagerAdapter.addFragment(new ProfileFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void setMeetingFragment(PendingMeetingRequestPOJO.MeetingRequestList item) {
//        viewPager.setCurrentItem(1);
//        spaceNavigationView.changeCurrentItem(1);
        if (item !=null){
            MeetingDetailFragment.item = item;
            MeetingDetailFragment addPhotoBottomDialogFragment =
                    (MeetingDetailFragment) MeetingDetailFragment.newInstance();
            addPhotoBottomDialogFragment.show(HomeActivity.this.getSupportFragmentManager(),
                    "MeetingDetailFragment");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 123) {
         //   showMeetingDialog();
        }
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
        if (event.getEvent() == Utility.MEETING_REQUEST){
            Log.d(TAG, "event  " +  event.getMeeting_id());
            meetingId = event.getMeeting_id();
         //   getPendingMeeting();
            // popupMeetingRequest(pojo.getMeetingObject());

        } else if (event.getEvent() == Utility.MEETING_BOOK){
            //setMeetingFragment();
        } else if (event.getEvent() == Utility.END_CALL_DIALOG){
//            Toast.makeText(this, "ended", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "event  " +  event.getMeeting_id());
//            meetingId = event.getMeeting_id();
//            showMeetingDialog(meetingId);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sharedData.getBooleanData(SharedData.SHOW_DIALOG)){
            meetingId = sharedData.getStringData(SharedData.MEETING_ID);
            showMeetingRatingDialog(meetingId);
        }
//        getMeetingHome();
    }


    public void getMeetingHome() {
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
                    CommonHomePOJO pojo = response.body();
                    try {
                        Log.d(TAG,""+pojo.getMessage());
                        if (pojo != null){
                            if (pojo.getOK()) {
                                setNoti(pojo.getPendingRequestCount());
//                                setMeetingBadge(pojo.getMeetingScheduledList().size());
                                /*int meetingCount = 0;
                                for (int i=0; i<pojo.getMeetingRequestList().size(); i++) {
                                    if (pojo.getMeetingRequestList().get(i).getSel_meeting().isFlag_giver_prop_time()){
                                        meetingCount++;
                                    }
                                }
                                setMeetingBadge(meetingCount);*/
                                // recycler_requested.setAdapter(requestedAdapter);
                                // Toast.makeText(getContext(), "Success "+pojo.getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                //Toast.makeText(getContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        getMeetingRequest();
                    } catch (Exception e){
                        e.getMessage();
                    }

                }
            }
            @Override
            public void onFailure(Call<CommonHomePOJO> call, Throwable t) {
                //   progressHUD.dismiss();
                Toast.makeText(getApplicationContext(), ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
                                    int meetingCount = 0;
                                    for (int i=0; i<pojo.getMeetingRequestList().size(); i++) {
                                        if (pojo.getMeetingRequestList().get(i).getSel_meeting().isFlag_giver_prop_time()){
                                            meetingCount++;
                                        }
                                    }
                                    setMeetingBadge(meetingCount);
                                }
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<PendingMeetingRequestPOJO> call, Throwable t) {
                    //progressHUD.dismiss();
                    Toast.makeText(getApplicationContext(), ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch(Exception e){
            e.getMessage();
        }

    }


    public void showMeetingRatingDialog(String meeting_Id) {
        meetingId = meeting_Id;
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater =  this.getLayoutInflater();
        // final View dialogView = inflater.inflate(R.layout.popup_pending_meeting, null);

        final View view1 = layoutInflater.inflate(R.layout.dialog_meeting_rating, null);
        builder.setView(view1);
        ImageView img_close = view1.findViewById(R.id.img_close);

        TextView txt_didntMeet = view1.findViewById(R.id.txt_didntMeet);
        Button btn_submit = view1.findViewById(R.id.btn_submit);

        RatingBar rating_meeting = view1.findViewById(R.id.rating_meeting);
        RatingBar rating_app = view1.findViewById(R.id.rating_app);

        TextView txt_experience = view1.findViewById(R.id.txt_experience);

        ImageView img_thumbs_up = view1.findViewById(R.id.img_thumbs_up);
        ImageView img_thumbs_down = view1.findViewById(R.id.img_thumbs_down);

        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        sharedData.addBooleanData(SharedData.SHOW_DIALOG, false);
        txt_experience.setText(Html.fromHtml((getResources().getString(R.string.experience))+
                "<br> with <font color='#108568'>" +sharedData.getStringData(SharedData.MEETING_PARSON_NAME)  +"</font>" + "?"));

        builder.setView(view1);
        rating_app.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (isDidntMeetSelect) {
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.circle_terracota));
                } else {
                    if (rating_app.getRating() != 0.0  && rating_meeting.getRating() != 0.0 && isRelevantMatch){
                        btn_submit.setBackground(getResources().getDrawable(R.drawable.circle_terracota));
                    } else {
                        btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                    }
                }
//                if (rating.getRating() != 0.0 && !reason.equals("")){
//                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_darkseacolor));
//                } else {
//                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_transparent_darkseacolor));
//                }
            }
        });

        rating_meeting.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (isDidntMeetSelect) {
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.circle_terracota));
                } else {
                    if (rating_app.getRating() != 0.0  && rating_meeting.getRating() != 0.0 && isRelevantMatch){
                        btn_submit.setBackground(getResources().getDrawable(R.drawable.circle_terracota));
                    } else {
                        btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                    }
                }
                rating_int = (int) ratingBar.getRating();

            }
        });

        img_thumbs_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRelevantMatch = true;
                isRelevantMatchSelect = 1;
                img_thumbs_up.setImageDrawable(getResources().getDrawable(R.drawable.thumbs_up_select));
                img_thumbs_down.setImageDrawable(getResources().getDrawable(R.drawable.thumb_down));
                if (isDidntMeetSelect) {
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.circle_terracota));
                } else {
                    if (rating_app.getRating() != 0.0  && rating_meeting.getRating() != 0.0 && isRelevantMatch){
                        btn_submit.setBackground(getResources().getDrawable(R.drawable.circle_terracota));
                    } else {
                        btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                    }
                }

            }
        });

        img_thumbs_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRelevantMatch = true;
                isRelevantMatchSelect = 0;
                img_thumbs_up.setImageDrawable(getResources().getDrawable(R.drawable.thumbs_up));
                img_thumbs_down.setImageDrawable(getResources().getDrawable(R.drawable.thumbs_down_select));
                if (isDidntMeetSelect) {
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.circle_terracota));
                } else {
                    if (rating_app.getRating() != 0.0  && rating_meeting.getRating() != 0.0 && isRelevantMatch){
                        btn_submit.setBackground(getResources().getDrawable(R.drawable.circle_terracota));
                    } else {
                        btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                    }
                }

            }
        });


        txt_didntMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDidntMeet){
                    isDidntMeet =false;
                } else {
                    isDidntMeet = true;
                }


                if (isDidntMeet) {
                    txt_didntMeet.setTextColor(getResources().getColor(R.color.terracota));
                    txt_didntMeet.setBackground(getResources().getDrawable(R.drawable.outline_circle_tarccota_transparent));
                  //  isDidntMeet = false;
                    isDidntMeetSelect = true;

                } else {
                    txt_didntMeet.setTextColor(getResources().getColor(R.color.darkSeaGreen));
                    txt_didntMeet.setBackground(getResources().getDrawable(R.drawable.outline_circle_darkseagreen));
                  //  isDidntMeet = true;
                    isDidntMeetSelect = false;

                }

                if (isDidntMeetSelect) {
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.circle_terracota));
                } else {
                    if (rating_app.getRating() != 0.0  && rating_meeting.getRating() != 0.0 && isRelevantMatch){
                        btn_submit.setBackground(getResources().getDrawable(R.drawable.circle_terracota));
                    } else {
                        btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                    }
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flag_no_show;
                if (isDidntMeet){
                    dialogs.dismiss();
                    flag_no_show = 1;

                    HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                    visitEvent.put("meeting_request_id", meetingId);
                    visitEvent.put("rating", (int) rating_meeting.getRating());
                    visitEvent.put("review", isRelevantMatchSelect);
                    visitEvent.put("usertype", sharedData.getStringData(SharedData.USER_TYPE));
                    cleverTap.pushEvent(Utility.Meeting_Rated,visitEvent);

                    getSaveMeetingReview("",  isRelevantMatchSelect,  flag_no_show,
                            (int) rating_app.getRating(),  (int) rating_meeting.getRating());
                  //  usertype(requestor/giver)





                } else {
                    if (rating_app.getRating() != 0.0 && rating_meeting.getRating() != 0.0 && isRelevantMatch) {
                        dialogs.dismiss();

                        HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                        visitEvent.put("meeting_request_id", meetingId);
                        visitEvent.put("rating", (int) rating_meeting.getRating());
                        visitEvent.put("review", isRelevantMatchSelect);
                        visitEvent.put("usertype", sharedData.getStringData(SharedData.USER_TYPE));
                        cleverTap.pushEvent(Utility.Meeting_Rated,visitEvent);

                        getSaveMeetingReview("",  isRelevantMatchSelect,  0,
                                (int) rating_app.getRating(), (int) rating_meeting.getRating());

                    } else {
                        //  Toast.makeText(HomeActivity.this, "Select valid details", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        // dialogs.setCancelable(false);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });

        dialogs.show();



    }

    public void showMeetingDialog(String meeting_Id) {
        meetingId = meeting_Id;
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater =  this.getLayoutInflater();
       // final View dialogView = inflater.inflate(R.layout.popup_pending_meeting, null);

        final View view1 = layoutInflater.inflate(R.layout.dialog_rate_meeting, null);
        builder.setView(view1);
        ImageView img_close = view1.findViewById(R.id.img_close);

        TextView txt_name = view1.findViewById(R.id.txt_name);
        TextView txt_reason1 = view1.findViewById(R.id.txt_reason1);
        TextView txt_reason2 = view1.findViewById(R.id.txt_reason2);
        TextView txt_reason3 = view1.findViewById(R.id.txt_reason3);
        TextView txt_reason4 = view1.findViewById(R.id.txt_reason4);
        TextView txt_didntMeet = view1.findViewById(R.id.txt_didntMeet);
        Button btn_submit = view1.findViewById(R.id.btn_submit);
        RatingBar rating = view1.findViewById(R.id.rating);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        sharedData.addBooleanData(SharedData.SHOW_DIALOG, false);
        txt_name.setText(Html.fromHtml((getResources().getString(R.string.rate_meeting))+
                " <font color='#108568'>" + "</font>" + ""));

        builder.setView(view1);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating_int = (int) ratingBar.getRating();
                if (rating.getRating() != 0.0 && !reason.equals("")){
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_darkseacolor));
                } else {
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_transparent_darkseacolor));
                }
            }
        });

        txt_didntMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReason(txt_didntMeet);
                setUnSelectedReason(txt_reason1);
                setUnSelectedReason(txt_reason2);
                setUnSelectedReason(txt_reason3);
                setUnSelectedReason(txt_reason4);
                if (rating.getRating() != 0.0){
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_darkseacolor));
                }
                //dialogs.dismiss();
                reason = txt_didntMeet.getText().toString();
               // getSaveMeetingReview(reason, 0);
            }
        });

        txt_reason1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReason(txt_reason1);
                setUnSelectedReason(txt_reason4);
                setUnSelectedReason(txt_reason2);
                setUnSelectedReason(txt_reason3);
                setUnSelectedReason(txt_didntMeet);
                reason = txt_reason1.getText().toString();
//                if (rating.getRating() == 0.0){
//                    Toast.makeText(HomeActivity.this, "Please select rating", Toast.LENGTH_SHORT).show();
//                } else {
//                    dialogs.dismiss();
//                    rating_int = (int) rating.getRating();
//                    String reason = txt_reason1.getText().toString();
//                    getSaveMeetingReview(reason, rating_int);
//                }

            }
        });

        txt_reason2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReason(txt_reason2);
                setUnSelectedReason(txt_reason1);
                setUnSelectedReason(txt_reason4);
                setUnSelectedReason(txt_reason3);
                setUnSelectedReason(txt_didntMeet);
                reason = txt_reason2.getText().toString();
                if (rating.getRating() != 0.0){
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_darkseacolor));
                }
            }
        });
        txt_reason3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReason(txt_reason3);
                setUnSelectedReason(txt_reason1);
                setUnSelectedReason(txt_reason2);
                setUnSelectedReason(txt_reason4);
                setUnSelectedReason(txt_didntMeet);
                reason = txt_reason3.getText().toString();
                if (rating.getRating() != 0.0){
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_darkseacolor));
                }
//                if (rating.getRating() == 0.0){
//                    Toast.makeText(HomeActivity.this, "Please select rating", Toast.LENGTH_SHORT).show();
//                } else {
//                    dialogs.dismiss();
//                    rating_int = (int) rating.getRating();
//                    String reason = txt_reason3.getText().toString();
//                    getSaveMeetingReview(reason, rating_int);
//                }
            }
        });
        txt_reason4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReason(txt_reason4);
                setUnSelectedReason(txt_reason1);
                setUnSelectedReason(txt_reason2);
                setUnSelectedReason(txt_reason3);
                setUnSelectedReason(txt_didntMeet);
                reason = txt_reason4.getText().toString();
                if (rating.getRating() != 0.0){
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_darkseacolor));
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rating.getRating() == 0.0 && reason.equals("")){
                    Toast.makeText(HomeActivity.this, "Please select rating and experience", Toast.LENGTH_SHORT).show();
                } else if (reason.equals("")) {
                    Toast.makeText(HomeActivity.this, "Please select experience", Toast.LENGTH_SHORT).show();
                } else if (rating.getRating() == 0.0 ){
                    Toast.makeText(HomeActivity.this, "Please select rating", Toast.LENGTH_SHORT).show();
                } else {
                    dialogs.dismiss();
                    rating_int = (int) rating.getRating();
                    Log.d(TAG,""+ rating_int);
                    //String reason = txt_reason4.getText().toString();
                  //  getSaveMeetingReview(reason, rating_int);
                }
            }
        });
        // dialogs.setCancelable(false);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });

        dialogs.show();



    }

    private  void selectedReason(TextView textView){
        textView.setBackground(getResources().getDrawable(R.drawable.bg_dark_rate));
        textView.setTextColor(getResources().getColor(R.color.terracota));
    }

    private void setUnSelectedReason(TextView textView)
    {
        textView.setBackground(getResources().getDrawable(R.drawable.outline_background_rate));
        textView.setTextColor(getResources().getColor(R.color.darkGrey));

    }

    public void getSaveMeetingReview(String review_text,int flag_thumbs, int flag_no_show, int rating_app,  int rating_meeting ) {
        Log.d(TAG, "review int " + rating_meeting + " meetingId " + meetingId);
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getSaveMeetingReview(
                sharedData.getStringData(SharedData.API_URL) +
                "api/meeting/save-meeting-review", loginPOJO.getActiveToken(),
                meetingId, loginPOJO.getRowcode(),review_text ,review_text, rating_meeting, flag_thumbs, flag_no_show, rating_app, rating_meeting);
        call.enqueue(new Callback<CommonPOJO>() {
            @Override
            public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    try {
                        if (reasonPOJO != null){
                            Log.d(TAG,""+reasonPOJO.getMessage());
                            if (reasonPOJO.getOK()) {
                               // Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e){
                        progressHUD.dismiss();
                        e.getMessage();
                    }

                }
            }
            @Override
            public void onFailure(Call<CommonPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void popupMeetingRequest(CommonMeetingListPOJO.MeetingListPOJO meetingObject) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.SheetDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup_pending_meeting, null);
        dialogBuilder.setView(dialogView);
        LinearLayout layout_data = dialogView.findViewById(R.id.layout_data);
        TextView txt_persona = dialogView.findViewById(R.id.txt_persona);
        RecyclerView rv_tags = dialogView.findViewById(R.id.rv_tags);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(meetingObject.getRequestorDomainTags());
        arrayList.addAll(meetingObject.getRequestorSubDomainTags());
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getApplicationContext());
        rv_tags.setLayoutManager(gridLayout);
        rv_tags.setAdapter(new ExperienceAdapter(getApplicationContext(), arrayList));
        txt_persona.setText(meetingObject.getRequestorDesignationTags().get(0));
        final AlertDialog dialog = dialogBuilder.create();
        layout_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), NotificationListActivity.class);
                intent.putExtra("intent_type", "NOTI");
                intent.putExtra("meeting_id", meetingId);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dialog.setCancelable(true);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @OnClick({R.id.img_profile, R.id.layout_notification})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_profile:
                //();
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_notification:
                Intent intent1 = new Intent(getApplicationContext(), NotificationListActivity.class);
                intent1.putExtra("intent_type", "FLOW");
                startActivity(intent1);
                break;

        }
    }

    public void callFragment() {

        MeetingRequestFragment addPhotoBottomDialogFragment =
                (MeetingRequestFragment) MeetingRequestFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "MeetingRequestFragment");

//        NewRequstMeetingFragment addPhotoBottomDialogFragment =
//                (NewRequstMeetingFragment) NewRequstMeetingFragment.newInstance();
//        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
//                "MeetingRequestFragment");
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