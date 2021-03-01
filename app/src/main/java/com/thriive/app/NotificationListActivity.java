package com.thriive.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.ExperienceListAdapter;
import com.thriive.app.adapters.ExpertiseAdapter;
import com.thriive.app.adapters.MeetingAcceptSlotAdapter;
import com.thriive.app.adapters.MeetingDetailSlotAdapter;
import com.thriive.app.adapters.MeetingNewTimeAdapter;
import com.thriive.app.adapters.MeetingRequestTimeSlotAdapter;
import com.thriive.app.adapters.MeetingSelectTagAdapter;
import com.thriive.app.adapters.MeetingTagAdapter;
import com.thriive.app.adapters.NotificationDetailTagAdapter;
import com.thriive.app.adapters.PendingNotificationAdapter;
import com.thriive.app.adapters.ProposeNewTimeSlotAdapter;
import com.thriive.app.adapters.SelectedProposeSlotAdapter;
import com.thriive.app.adapters.SelectedSlotsAdapter;
import com.thriive.app.adapters.SlotListAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.fragments.MeetingDetailFragment;
import com.thriive.app.fragments.MeetingRequestAcceptFragment;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.fragments.MeetingsFragment;
import com.thriive.app.models.CommonEntitySlotsPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonRequestTimeSlots;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.MeetingDetailPOJO;
import com.thriive.app.models.MeetingSlotBodyPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.models.SendProposeTimeBody;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.MySpannable;
import com.thriive.app.utilities.PreciseCountdown;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

public class NotificationListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.rv_notification)
    RecyclerView rv_notification;
    @BindView(R.id.txt_noData)
    TextView txt_noData;
    @BindView(R.id.refresh_view)
    SwipeRefreshLayout refreshView;
    private ArrayList<PendingMeetingRequestPOJO>arrayList = new ArrayList<>();

    RecyclerView rv_select_time_slots;
    RecyclerView rv_time_slots;
    LinearLayout layout_accept_step2;
    LinearLayout layout_accept_disable;
    LinearLayout layout_accept_enable;
    Button slot_btn_next;

    TextView txt_year;
    TextView txt_month;
    TextView txt_day;
    TextView text_please_select_3_slots;
    TextView txtTimer;
    TextView txt_toaster;

    private APIInterface apiInterface;
    private SharedData sharedData;
    private LoginPOJO.ReturnEntity loginPOJO;
    private KProgressHUD progressHUD;
    String region_name = "", user_region = "";
    String meetingCode ="", startTime ="", endTime ="", meeting_slot_id ="", cancelReason = "",
            selectedDate = "", personaName, meetingReason,txtYear="",txtMonth="",txtDay="",selected_layout="",currentDate="";
    Calendar calendar = Calendar.getInstance();
    ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> selectItem;
    ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> slotsListPOJOS;
    List<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList;
    public static final String TAG = NotificationListActivity.class.getName();
    public static String start_time  , end_time;
    private BottomSheetDialog dialogEditSlot;
    private BottomSheetDialog dialogMeetingDetails;
    private AlertDialog dialogDetails ;
    private CleverTapAPI cleverTap;
    int giverID = 0;
    int requestorId = 0;
    private long seconds = 0, minutes = 0, hours = 0, call_time;
    private PreciseCountdown preciseCountdown, endCountDown;
    PendingNotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        ButterKnife.bind(this);

        apiInterface = APIClient.getApiInterface();
        sharedData = new SharedData(getApplicationContext());
        loginPOJO  = Utility.getLoginData(getApplicationContext());

        cleverTap = CleverTapAPI.getDefaultInstance(getApplicationContext());
        slotsListPOJOS = new ArrayList<>();
        slotsListPOJOS.clear();
        requesterPOJOArrayList = new ArrayList<>();
        requesterPOJOArrayList.clear();
        refreshView.setOnRefreshListener(this);
        refreshView.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        refreshView.setRefreshing(false);

        if (getIntent().getStringExtra("intent_type").equals("NOTI")){
//            HashMap<String, Object> visitEvent = new HashMap<String, Object>();
//            visitEvent.put("meeting_request_id", getIntent().getStringExtra("meeting_id"));
//            cleverTap.pushEvent(Utility.Meeting_Request_Viewed,visitEvent);

            cleverTap.pushEvent(Utility.Viewed_Alerts);

            getMeetingRequestById();

        }
        //getMeetingRequest();
    }

    private void getMeetingRequest() {
        try {
            txt_noData.setVisibility(View.GONE);
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonMeetingListPOJO> call = apiInterface.getPendingRequest(sharedData.getStringData(SharedData.API_URL) + "api/AppHome/get-pending-requests", loginPOJO.getActiveToken(),
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
                            if (dialogMeetingDetails != null){
                                dialogMeetingDetails.dismiss();
                            }
                            if (reasonPOJO.getOK()) {
                                if (reasonPOJO.getMeetingList() != null){
                                    if (reasonPOJO.getMeetingList().size() > 0){
                                        requesterPOJOArrayList.addAll(reasonPOJO.getMeetingList());
                                    }
                                    //rv_notification.setAdapter(new PendingNotificationAdapter(NotificationListActivity.this,requesterPOJOArrayList ));
                                }
                                get_awaited_meetings_for_giver();
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

    private void get_awaited_meetings_for_giver() {
        try {
            txt_noData.setVisibility(View.GONE);
//            progressHUD = KProgressHUD.create(this)
//                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                    .setLabel("Please wait")
//                    .setCancellable(false)
//                    .show();
            Call<CommonMeetingListPOJO> call = apiInterface.getPendingRequest(sharedData.getStringData(SharedData.API_URL) + "api/AppHome/get-awaited-meetings-for-giver", loginPOJO.getActiveToken(),
                    loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonMeetingListPOJO>() {
                @Override
                public void onResponse(Call<CommonMeetingListPOJO> call, Response<CommonMeetingListPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonMeetingListPOJO reasonPOJO = response.body();
                        //progressHUD.dismiss();
                        try {
                            Log.d(TAG,""+reasonPOJO.getMessage());
                            if (dialogMeetingDetails != null){
                                dialogMeetingDetails.dismiss();
                            }
                            if (reasonPOJO.getOK()) {
                                if (requesterPOJOArrayList != null){
                                    requesterPOJOArrayList.addAll(reasonPOJO.getMeetingList());
                                 if (requesterPOJOArrayList.size() > 0){
                                     txt_noData.setVisibility(View.GONE);
                                     rv_notification.setVisibility(View.VISIBLE);
//                                     rv_notification.setAdapter(new PendingNotificationAdapter(NotificationListActivity.this,requesterPOJOArrayList));
                                  }else {
                                     rv_notification.setVisibility(View.GONE);
                                 }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            if (requesterPOJOArrayList == null){
                                txt_noData.setVisibility(View.VISIBLE);
                            }else if (requesterPOJOArrayList.size() == 0){
                                txt_noData.setVisibility(View.VISIBLE);
                            } else {
                                txt_noData.setVisibility(View.GONE);
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
                    //progressHUD.dismiss();
                    Toast.makeText(NotificationListActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }

    private void getMeetingRequestById() {
        try {
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

    public void acceptMeetingSlot(MeetingDetailPOJO meetingDetailPOJO) {
        if (meetingDetailPOJO != null){
            try {
                meeting_slot_id = String.valueOf(meetingDetailPOJO.getMeeting_slot_id());
                layout_accept_enable.setVisibility(View.VISIBLE);
                layout_accept_disable.setVisibility(View.GONE);
        }catch(Exception e){e.printStackTrace();}

        }
    }

    public void detailsMeeting(CommonMeetingListPOJO.MeetingListPOJO meetingListPOJO) {
        HashMap<String, Object> visitEvent = new HashMap<String, Object>();
        visitEvent.put("meeting_request_id", meetingListPOJO.getMeetingCode());
        cleverTap.pushEvent(Utility.Meeting_Request_Viewed,visitEvent);
        try {
            meetingCode = meetingListPOJO.getMeetingCode();
            startTime = meetingListPOJO.getPlanStartTime();
            endTime = meetingListPOJO.getPlanEndTime();
            meetingReason = meetingListPOJO.getMeetingReason();
            personaName = meetingListPOJO.getRequestorPersonaTags().get(0);
            region_name = meetingListPOJO.getRequestorCountryName();
            user_region = meetingListPOJO.getGiverCountryName();
            giverID = meetingListPOJO.getSel_meeting().getGiver_id();
            requestorId = meetingListPOJO.getSel_meeting().getRequestor_id();
        } catch (Exception e ){
            e.getMessage();
        }

        try {

            dialogMeetingDetails = new BottomSheetDialog(NotificationListActivity.this);
            dialogMeetingDetails.setContentView(R.layout.dialog_meeting_request_accept);

            FrameLayout bottomSheet =  dialogMeetingDetails.findViewById(R.id.design_bottom_sheet);
            bottomSheet.setBackground(NotificationListActivity.this.getResources().getDrawable(R.drawable.rounded_white));
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            RecyclerView rv_experience = dialogMeetingDetails.findViewById(R.id.rv_experience);
            RecyclerView rv_expertise = dialogMeetingDetails.findViewById(R.id.rv_expertise);
            RecyclerView rv_meetin_tag = dialogMeetingDetails.findViewById(R.id.rv_meetin_tag);
            RecyclerView rv_slots = dialogMeetingDetails.findViewById(R.id.rv_slots);
            ImageView img_close = dialogMeetingDetails.findViewById(R.id.img_close);
            ImageButton accept = dialogMeetingDetails.findViewById(R.id.img_accept);
            ImageButton decline = dialogMeetingDetails.findViewById(R.id.img_decline);
            TextView txt_reason  = dialogMeetingDetails.findViewById(R.id.txt_reason);
            txtTimer  = dialogMeetingDetails.findViewById(R.id.label_timer);
            TextView txt_name = dialogMeetingDetails.findViewById(R.id.txt_name);
            TextView txt_experience = dialogMeetingDetails.findViewById(R.id.txt_experience);
            TextView txt_profession = dialogMeetingDetails.findViewById(R.id.txt_profession);
            TextView txt_details = dialogMeetingDetails.findViewById(R.id.txt_details);
            CircleImageView img_user = dialogMeetingDetails.findViewById(R.id.img_user);
            TextView txt_country = dialogMeetingDetails.findViewById(R.id.txt_country);

            LinearLayout layout_step1 = dialogMeetingDetails.findViewById(R.id.layout_step1);
            LinearLayout layout_step2 = dialogMeetingDetails.findViewById(R.id.layout_step2);
            LinearLayout layout_step3 = dialogMeetingDetails.findViewById(R.id.layout_step3);

            selected_layout = "step_1";
            layout_step1.setVisibility(View.VISIBLE);
            layout_step2.setVisibility(View.GONE);
            layout_step3.setVisibility(View.GONE);

            //step 2 layout
            LinearLayout layout_decline_step2 = dialogMeetingDetails.findViewById(R.id.layout_decline_step2);
            LinearLayout layout_propose_step2 = dialogMeetingDetails.findViewById(R.id.layout_propose_step2);
            layout_accept_step2 = dialogMeetingDetails.findViewById(R.id.layout_accept_step2);

            layout_accept_disable = dialogMeetingDetails.findViewById(R.id.layout_accept_disable);
            layout_accept_enable = dialogMeetingDetails.findViewById(R.id.layout_accept_enable);

            ImageButton img_accept_step2 = dialogMeetingDetails.findViewById(R.id.img_accept_step2_enable);
            ImageButton img_propose_step = dialogMeetingDetails.findViewById(R.id.img_propose_step);
            ImageButton img_decline_step2 = dialogMeetingDetails.findViewById(R.id.img_decline_step2);
            text_please_select_3_slots = dialogMeetingDetails.findViewById(R.id.text_please_select_3_slots);


            //step 3 layout
            rv_time_slots = dialogMeetingDetails.findViewById(R.id.rv_time_slots);
            ImageView img_backward = dialogMeetingDetails.findViewById(R.id.img_backward);
            ImageView img_forward = dialogMeetingDetails.findViewById(R.id.img_forward);
            LinearLayout layout_circle = dialogMeetingDetails.findViewById(R.id.layout_circle);
            txt_year = dialogMeetingDetails.findViewById(R.id.txt_year);
            txt_month = dialogMeetingDetails.findViewById(R.id.txt_month);
            txt_day = dialogMeetingDetails.findViewById(R.id.txt_day);
            txt_toaster = dialogMeetingDetails.findViewById(R.id.txt_toaster);
            rv_select_time_slots = dialogMeetingDetails.findViewById(R.id.rv_select_time_slots);
            slot_btn_next = dialogMeetingDetails.findViewById(R.id.slot_btn_next);
            ImageView img_back = dialogMeetingDetails.findViewById(R.id.img_back);
            ImageView img_info = dialogMeetingDetails.findViewById(R.id.img_info);
            img_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCustomToast();
                }
            });
            setDefaultDate();
            getStartTimer(meetingListPOJO);

            if (slotsListPOJOS.size() == 0){
                slot_btn_next.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
            }else {
                slot_btn_next.setBackground(getResources().getDrawable(R.drawable.filled_circle_terracota));
            }

            txt_details.setText(meetingListPOJO.getMeeting_req_text());
            //makeTextViewResizable(txt_details, 1, "See More", true);

            txt_profession.setText(""+meetingListPOJO.getRequestorSubTitle());

            if (!meetingListPOJO.getMeeting_l1_attrib_name().equals("")){
                txt_reason.setText("Meeting for "+meetingListPOJO.getMeetingReason() + " - " + meetingListPOJO.getMeeting_l1_attrib_name());
            }else {
                txt_reason.setText("Meeting for "+meetingListPOJO.getMeetingReason());
            }

            txt_country.setText(""+meetingListPOJO.getRequestorCountryName());
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
            rv_experience.setLayoutManager(manager );
            ArrayList<String> array = new ArrayList<>();
            //array.addAll(meetingListPOJO.getRequestorExperienceTags());
            for (int i =0; i< meetingListPOJO.getRequestorDesignationTags().size(); i++) {
                if (i <= 1){
                    array.add(meetingListPOJO.getRequestorDesignationTags().get(i));
                }
            }

            rv_experience.setAdapter(new ExperienceListAdapter(NotificationListActivity.this,array));
            FlexboxLayoutManager manager1 = new FlexboxLayoutManager(NotificationListActivity.this);
            rv_expertise.setLayoutManager(manager1 );
            ArrayList<String> array1 = new ArrayList<>();
           // array1.addAll(meetingListPOJO.getMeetingTag());
            array1.addAll(meetingListPOJO.getRequestorDomainTags());
            array1.addAll(meetingListPOJO.getRequestorExpertiseTags());

            ArrayList<String> combine_array = new ArrayList<>();
            for (int i = 0; i < array1.size(); i++){
                if (!array1.get(i).equals("")){
                    combine_array.add(array1.get(i));
                }
            }
            HashSet hs = new HashSet();
            hs.addAll(combine_array); // demoArrayList= name of arrayList from which u want to remove duplicates
            combine_array.clear();
            combine_array.addAll(hs);
            ArrayList<String> final_array = new ArrayList<>();
            for (int i =0; i< combine_array.size(); i++)
            {
                if (i <= 3){
                    final_array.add(combine_array.get(i));
                }

            }
            rv_expertise.setAdapter(new MeetingSelectTagAdapter(NotificationListActivity.this, final_array,
                    (ArrayList<String>) meetingListPOJO.getMeetingTag()));

            if (meetingListPOJO.getMeetingTag().size() > 0){
                List<String> requesterPOJOArrayList = new ArrayList<>();
                for (int i = 0; i < meetingListPOJO.getMeetingTag().size() ; i++) {
                     if (!meetingListPOJO.getMeetingTag().get(i).equals("")){
                         requesterPOJOArrayList.add(meetingListPOJO.getMeetingTag().get(i));
                     }
                }
                if (requesterPOJOArrayList.size() > 0){
                    FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(NotificationListActivity.this);
                    rv_meetin_tag.setLayoutManager(gridLayout);
                    rv_meetin_tag.setAdapter(new NotificationDetailTagAdapter(NotificationListActivity.this, requesterPOJOArrayList));
                }

            }

            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected_layout.equals("step_1")){
                        layout_step1.setVisibility(View.VISIBLE);
                        layout_step2.setVisibility(View.GONE);
                        layout_step3.setVisibility(View.GONE);
                        img_back.setVisibility(View.GONE);
                    }else if (selected_layout.equals("step_2")){
                       layout_step1.setVisibility(View.VISIBLE);
                       layout_step2.setVisibility(View.GONE);
                       layout_step3.setVisibility(View.GONE);
                       img_back.setVisibility(View.GONE);
                   }else if (selected_layout.equals("step_3")){
                        layout_step1.setVisibility(View.GONE);
                        layout_step2.setVisibility(View.VISIBLE);
                        layout_step3.setVisibility(View.GONE);
                        img_back.setVisibility(View.VISIBLE);
                        selected_layout = "step_1";
                   }
                }
            });

            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getIntent().getStringExtra("intent_type").equals("NOTI")){
                        if (preciseCountdown != null){
                            preciseCountdown.cancel();
                        }
                        dialogMeetingDetails.dismiss();
                        requesterPOJOArrayList = new ArrayList<>();
                        requesterPOJOArrayList.clear();
                        getMeetingRequest();

                    } else {
                        if (preciseCountdown != null){
                            preciseCountdown.cancel();
                        }
                        dialogMeetingDetails.dismiss();
                        requesterPOJOArrayList = new ArrayList<>();
                        requesterPOJOArrayList.clear();
                        getMeetingRequest();

                    }

                }
            });

            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preciseCountdown != null){
                        preciseCountdown.cancel();
                    }
                    dialogMeetingDetails.dismiss();
                    getDeclineMeeting(meetingCode);
                }
            });

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //getMeetingSlote();
                    layout_step1.setVisibility(View.GONE);
                    layout_step2.setVisibility(View.VISIBLE);
                    img_back.setVisibility(View.VISIBLE);
                    selected_layout = "step_2";
                }
            });

            //Layout Step 2
            if (meetingListPOJO.getSlot_list().size() > 0){
                rv_slots.setLayoutManager(new GridLayoutManager(NotificationListActivity.this, 3));
                /*rv_slots.setAdapter(new MeetingAcceptSlotAdapter(this,
                        (ArrayList<MeetingDetailPOJO>) meetingListPOJO.getSlot_list()));*/
            }

            img_decline_step2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preciseCountdown != null){
                        preciseCountdown.cancel();
                    }
                    dialogMeetingDetails.dismiss();
                    getDeclineMeeting(meetingCode);
                }
            });
            img_propose_step.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layout_step1.setVisibility(View.GONE);
                    layout_step2.setVisibility(View.GONE);
                    layout_step3.setVisibility(View.VISIBLE);
                    img_back.setVisibility(View.VISIBLE);
                    selected_layout = "step_3";
                }
            });

            img_accept_step2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogMeetingDetails.dismiss();
                    if (preciseCountdown != null){
                        preciseCountdown.cancel();
                    }

                    HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                    visitEvent.put("meeting_request_id", meetingCode);
                    visitEvent.put("availability_type ", "custom");
                    visitEvent.put("meeting_start_datetime", startTime);
                    cleverTap.pushEvent(Utility.Meeting_Request_Accepted,visitEvent);

                    getAcceptMeeting();
                }
            });

            //Layout Step 3
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

            slot_btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (slotsListPOJOS != null){
                        if (slotsListPOJOS.size() > 3){
                            Toast.makeText(NotificationListActivity.this, "Please select only 3 slots", Toast.LENGTH_SHORT).show();
                        }else {
                            if (slotsListPOJOS.size() == 3){
                                sendProposeNewTime();
                            }else {
                                Toast.makeText(NotificationListActivity.this, "Please select 3 slots", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
            dialogMeetingDetails.setCancelable(false);
            dialogMeetingDetails.show();
        } catch(Exception e){
            e.getMessage();
        }
    }

    private void showCustomToast() {
        try {
            txt_toaster.setVisibility(View.VISIBLE);
            TimeZone timeZone = TimeZone.getDefault();
            Log.d(TAG, "time zone "+ timeZone.getID());
            String  time_stamp = "Limit of 1 time per day. Times are based on the region you’ve selected. Time is displayed in "+timeZone.getDisplayName();

            txt_toaster.setText(time_stamp);

        } catch(Exception e){
            e.getMessage();
        }

        txt_toaster.postDelayed(new Runnable() {
            public void run() {
                txt_toaster.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void getStartTimer(CommonMeetingListPOJO.MeetingListPOJO meetingListPOJO){
       try {
           String match_date = meetingListPOJO.getSel_meeting().getDate_matched();
           final long millisToAdd = 43_200_000; //12 hours => 43,200,000

           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
           Date d = format.parse(match_date);
           d.setTime(d.getTime() + millisToAdd);
           Date abc =  d;

           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
           String dateTime = dateFormat.format(abc);

           call_time = Utility.getTimeDifferenceWithCurrentTime(Utility.ConvertUTCToUserTimezone(dateTime));
           startTimer();
           preciseCountdown.start();
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    private void startTimer() {
        // TimeUnit.MINUTES.toMillis(30)
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                preciseCountdown = new PreciseCountdown(call_time, 1000, 0) {
                    @Override
                    public void onTick(long timeLeft) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                seconds ++;
                                if(seconds == 60) {
                                    seconds = 0;
                                    minutes ++;
                                }
                                if(minutes == 60) {
                                    seconds = 0;
                                    minutes = 0;
                                    hours ++;
                                }

                                NumberFormat f = new DecimalFormat("00");

                                  long hour1 = (timeLeft / 3600000) % 24;
                                   long min1 = (timeLeft / 60000) % 60;
                                   long sec1 = (timeLeft / 1000) % 60;

                                  if (timeLeft >= 0){
                                      txtTimer.setText(f.format(hour1)
                                              + ":"  + f.format(min1)
                                              + ":" + f.format(sec1));
                                  }else {
                                      txtTimer.setText("00"
                                              + ":"  + "00"
                                              + ":" +  "00");
                                  }
                            }
                        });
                    }
                    @Override
                    public void onFinished() {
                        Log.d(TAG, "RESTART");
                    }
                };
            }
        });

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

        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationListActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_select_slot_date, null);

        ImageView img_close = view1.findViewById(R.id.img_close);
        Button btn_done = view1.findViewById(R.id.btn_done);
        CalendarView calender_view = view1.findViewById(R.id.calender);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,+1);
        //calender_view.setMinDate(new Date().getTime());
        calender_view.setMinDate(calendar.getTime().getTime());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(selectedDate);
            calender_view.setDate(date.getTime(),true,true);

//            Date maxDate = format.parse("2021-12-15");
//            calender_view.setMaxDate(maxDate.getTime());


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
                String newDate = year+"-"+month+"-"+date;
                Log.d("NEW_DATE", newDate);
                selectedDate = newDate;
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = format.parse(selectedDate);
                    calendar.setTime(date1);
                    SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
                    txtYear = dfYear.format(date1);

                    SimpleDateFormat dfMonth = new SimpleDateFormat("dd MMM");
                    txtMonth = dfMonth.format(date1.getTime());

                    SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
                    txtDay = dfDay.format(date1.getTime());

                    txt_year.setText(""+txtYear);
                    txt_month.setText(txtMonth);
                    txt_day.setText(txtDay);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
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

//    private void previousDate(){
//        calendar.add(Calendar.DATE, -1);
//        //calendar.se.setMinDate(System.currentTimeMillis() - 1000);
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        selectedDate = df.format(calendar.getTime());
//
//        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
//         txtYear = dfYear.format(calendar.getTime());
//
//        SimpleDateFormat dfMM = new SimpleDateFormat("dd MMM");
//        txtMonth = dfMM.format(calendar.getTime());
//
//        SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
//        txtDay = dfDay.format(calendar.getTime());
//
//        txt_year.setText(txtYear);
//        txt_month.setText(txtMonth);
//        txt_day.setText(txtDay);
//
//        String utc_date = Utility.convertLocaleToUtc(selectedDate);
//        if (utc_date != null && utc_date.length() > 0){
//            get_meeting_time_slots(utc_date);
//        }
//
//    }

    private void previousDate(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date current_date = format.parse(currentDate);
            Date selected_date = format.parse(selectedDate);
            if (selected_date.before(current_date) || selected_date.equals(current_date)){
                Toast.makeText(NotificationListActivity.this, "You can't select past date.", Toast.LENGTH_SHORT).show();
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
            progressHUD = KProgressHUD.create(NotificationListActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonRequestTimeSlots> call = apiInterface.getTimeSlotsForGiver(sharedData.getStringData(SharedData.API_URL) +
                    "api/MRCalls/get-time-slots-for-giver", loginPOJO.getActiveToken(), requestorId, giverID, utc_date);
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
                                    rv_select_time_slots.setLayoutManager(new GridLayoutManager(NotificationListActivity.this, 2));
                                    /*ProposeNewTimeSlotAdapter meetingRequestTimeSlotAdapter = new ProposeNewTimeSlotAdapter(NotificationListActivity.this,
                                            (ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>) pojo.getSlotsListPOJOS(),selectedDate);
                                    rv_select_time_slots.setAdapter(meetingRequestTimeSlotAdapter);
                                    meetingRequestTimeSlotAdapter.notifyDataSetChanged();*/
                                }else {
                                    Toast.makeText(NotificationListActivity.this, ""+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(NotificationListActivity.this, " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        progressHUD.dismiss();
                        Toast.makeText(NotificationListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<CommonRequestTimeSlots> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(NotificationListActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }


    public void setSelectedTimeSlot(CommonRequestTimeSlots.EntitySlotsListPOJO slot,String time){
        selectItem = new ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>();
        if (slot != null){
            if (slotsListPOJOS.size() < 3){
                slotsListPOJOS.add(slot);
            }

            if (slotsListPOJOS.size() > 1){
                for(int i =0; i < slotsListPOJOS.size(); i++) {
                    if (slot.getFor_date().equals(slotsListPOJOS.get(i).getFor_date())){
                        slotsListPOJOS.remove(i);
                    }else {
                        if (slotsListPOJOS.size() < 4){
                            slotsListPOJOS.add(slot);
                        }
                    }
                }
            }

            if (slotsListPOJOS.size() > 0){
                if (slotsListPOJOS.size() < 4){

                    if (slotsListPOJOS.size() == 3){
                        slot_btn_next.setBackground(getResources().getDrawable(R.drawable.filled_circle_terracota));
                    }else {
                        slot_btn_next.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                    }
                    text_please_select_3_slots.setVisibility(View.GONE);
                    rv_time_slots.setVisibility(View.VISIBLE);
                    rv_time_slots.setLayoutManager(new GridLayoutManager(NotificationListActivity.this, 3));
                    /*SelectedProposeSlotAdapter meetingRequestTimeSlotAdapter = new SelectedProposeSlotAdapter(NotificationListActivity.this,
                            (ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>) slotsListPOJOS);
                    rv_time_slots.setAdapter(meetingRequestTimeSlotAdapter);
                    meetingRequestTimeSlotAdapter.notifyDataSetChanged();*/
                }
            }else {
                slot_btn_next.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                text_please_select_3_slots.setVisibility(View.VISIBLE);
            }
        }
    }

    public void getSelectedTimeSlot(CommonRequestTimeSlots.EntitySlotsListPOJO slot, int pos){
        if (slot != null){

        }
    }

    public void removeSelectedTimeSlot(int pos){
        if (slotsListPOJOS.size() > 0){
            slotsListPOJOS.remove(pos);
            if (slotsListPOJOS.size() < 3){
                slot_btn_next.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
            }
        }else {
            text_please_select_3_slots.setVisibility(View.VISIBLE);
        }
    }

//    public void meetingEditSlot(List<CommonEntitySlotsPOJO.EntitySlotList> entitySlotList) {
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_availability, null);
//        dialogEditSlot = new BottomSheetDialog(NotificationListActivity.this, R.style.SheetDialog);
//        Button btn_confirm = dialogView.findViewById(R.id.btn_newSlot);
//        ImageView img_close = dialogView.findViewById(R.id.img_close);
//        RecyclerView rv_slots = dialogView.findViewById(R.id.rv_slots);
//
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
//        SlotListAdapter adapter  = new SlotListAdapter(NotificationListActivity.this,
//                (ArrayList<CommonEntitySlotsPOJO.EntitySlotList>) entitySlotList, "ACCEPT");
//        rv_slots.setLayoutManager(new LinearLayoutManager(NotificationListActivity.this,
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
//              //  dialogEditSlot.dismiss();
//                //  getMeetingSlote();
//                //meetingEditDate();
//            }
//        });
//        dialogEditSlot.setContentView(dialogView);
//        dialogEditSlot.show();
//    }



//    public void meetingEditDate() {
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_date, null);
//        BottomSheetDialog dialog = new BottomSheetDialog(NotificationListActivity.this, R.style.SheetDialog);
//
//        Button btn_next = dialogView.findViewById(R.id.btn_next);
//        ImageView img_close = dialogView.findViewById(R.id.img_close);
//        CalendarView calender_view = dialogView.findViewById(R.id.calender);
//        calender_view.setMinDate(new Date().getTime());
//
//        selectedDate = DateFormat.format("yyyy-MM-dd", calender_view.getDate()).toString();
//        Log.d("NEW_DATE", selectedDate);
//        calender_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//
//            @Override
//            public void onSelectedDayChange(CalendarView arg0, int year, int month,
//                                            int date) {
//                month = month + 1;
//                // output to log cat **not sure how to format year to two places here**
//                String newDate = year+"-"+month+"-"+date;
//                Log.d("NEW_DATE", newDate);
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
//    }

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
                if(Utility.getCheckSlotTime(startTime)){
                    Toast.makeText(getApplicationContext(), "Please choose current or future time.", Toast.LENGTH_SHORT).show();
                } else {
                    start_time = startTime;
                    end_time  = endTime;
                    //getAcceptMeeting();
                    dialog.dismiss();

                    meetingConfirmation("custom");
                }
//                dialog.dismiss();
//                getAcceptMeeting();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();


    }

    public void meetingConfirmation(String availability_type){
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationListActivity.this, R.style.SheetDialog);
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

        txt_title.setText(Html.fromHtml("You’re confirming a meeting with"));
        txt_subTitle.setText(Html.fromHtml(personaName + " for " + meetingReason + ""));
        txt_time.setText(Utility.getMeetingTime(start_time, end_time) + " (" + user_region + " time)");
        txt_date.setText(Utility.getMeetingDate(start_time));

        txt_country.setText("from "+ region_name);

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
                startTime = start_time;
                endTime = end_time;

                HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                visitEvent.put("meeting_request_id", meetingCode);
                visitEvent.put("availability_type ", availability_type);
                visitEvent.put("meeting_start_datetime", startTime);
                cleverTap.pushEvent(Utility.Meeting_Request_Accepted,visitEvent);

                getAcceptMeeting();
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

    private void sendProposeNewTime() {
        try {
            SendProposeTimeBody timeBody  = new SendProposeTimeBody();
            timeBody.setMeeting_code(meetingCode);
            timeBody.setRowcode(loginPOJO.getRowcode());
            timeBody.setFlag_giver_prop_time(true);
            timeBody.setSel_meeting_slot_id(0);
            timeBody.setSlotsListPOJOS(slotsListPOJOS);

            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.getSendProposeNewTime(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/accept-meeting",
                    loginPOJO.getActiveToken(), timeBody);
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

                                if (dialogEditSlot != null){
                                    dialogEditSlot.dismiss();
                                }
                                //   Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();

                                successDialogForProposeTome();

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
            Call<CommonPOJO> call = apiInterface.getAcceptMeeting(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/accept-meeting", loginPOJO.getActiveToken(),
                    meetingCode, loginPOJO.getRowcode(),false,meeting_slot_id);
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


                                if (dialogEditSlot != null){
                                    dialogEditSlot.dismiss();
                                }
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
        HashMap<String, Object> visitEvent = new HashMap<String, Object>();
        visitEvent.put("meeting_request_id", meetingCode);
        cleverTap.pushEvent(Utility.Meeting_Request_Declined,visitEvent);
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.getRejectMeeting(sharedData.getStringData(SharedData.API_URL)
                            + "api/Meeting/reject-meeting", loginPOJO.getActiveToken(),
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

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getStringExtra("intent_type").equals("FLOW")){
            if (preciseCountdown != null){
                preciseCountdown.cancel();
            }
            requesterPOJOArrayList = new ArrayList<>();
            requesterPOJOArrayList.clear();
            getMeetingRequest();
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
                requesterPOJOArrayList = new ArrayList<>();
                requesterPOJOArrayList.clear();
                getMeetingRequest();
            }
        });
        dialogs.show();
    }

    public void successDialogForProposeTome() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationListActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_propose_confirmed, null);
        TextView label_close = view1.findViewById(R.id.label_close);

        //    tv_msg.setText("Session Added Successfully.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                requesterPOJOArrayList = new ArrayList<>();
                requesterPOJOArrayList.clear();
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
        requesterPOJOArrayList = new ArrayList<>();
        requesterPOJOArrayList.clear();
        getMeetingRequest();
    }

    private void getMeetingSlote() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonEntitySlotsPOJO> call = apiInterface.getEntitySlots(sharedData.getStringData(SharedData.API_URL) +
                            "api/Entity/get-entity-slots", loginPOJO.getActiveToken(),
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

                                       // meetingEditSlot(reasonPOJO.getEntitySlotList());
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

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {
        try {
            if (tv.getTag() == null) {
                tv.setTag(tv.getText());
            }
            ViewTreeObserver vto = tv.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    String text;
                    int lineEndIndex;
                    ViewTreeObserver obs = tv.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    if (maxLine == 0) {
                        lineEndIndex = tv.getLayout().getLineEnd(0);
                        text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                        lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                        text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    } else {
                        lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                        text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    }
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            });
        }catch (Exception e){e.printStackTrace();}

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, 1, "See More", false);
                    } else {
                        makeTextViewResizable(tv, 3, "See Less", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;
    }

    public void show_giver_detail_pop_up(CommonMeetingListPOJO.MeetingListPOJO item) {
        if (item != null){
            try {

                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationListActivity.this, R.style.SheetDialog);
                LayoutInflater layoutInflater = this.getLayoutInflater();
                final View view1 = layoutInflater.inflate(R.layout.dialog_giver_awaited_meeting, null);

                ImageView img_close = view1.findViewById(R.id.img_close);
                TextView label_date = view1.findViewById(R.id.label_date);
                TextView txt_persona = view1.findViewById(R.id.txt_persona);
                RecyclerView rv_tags = view1.findViewById(R.id.rv_tags);
                TextView txt_reason = view1.findViewById(R.id.txt_reason);
                TextView txt_tags = view1.findViewById(R.id.txt_tags);
                TextView txt_region = view1.findViewById(R.id.txt_region);


                RecyclerView rv_slots = view1.findViewById(R.id.rv_slots);

                txt_persona.setText("with "+item.getSel_meeting().getRequestor_name());
                txt_reason.setText("For "+item.getSel_meeting().getReason_name());
                //txt_tags.setText(""+item.getMeetingLabel());
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(item.getRequestorDomainTags());
                arrayList.addAll(item.getRequestorSubDomainTags());
                arrayList.addAll(item.getRequestorExpertiseTags());
                FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(NotificationListActivity.this);
                rv_tags.setLayoutManager(gridLayout );
                if (arrayList.size() > 0){
                    txt_tags.setVisibility(View.VISIBLE);
                } else {
                    txt_tags.setVisibility(View.GONE);
                }
                rv_tags.setAdapter(new ExperienceAdapter(NotificationListActivity.this, arrayList));

                rv_slots.setLayoutManager(new GridLayoutManager(NotificationListActivity.this, 3));

                ArrayList<MeetingDetailPOJO> dialog_new_time = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    if (item.getSlot_list().size() > 0){
                        MeetingDetailPOJO data = item.getSlot_list().get(i);
                        dialog_new_time.add(data);
                    }
                }

                if (dialog_new_time.size() > 0){
                    rv_slots.setAdapter(new MeetingDetailSlotAdapter( NotificationListActivity.this,
                            (ArrayList<MeetingDetailPOJO>) dialog_new_time));
                }

                //txt_details.setText(""+item.getSel_meeting().getReq_text());
                label_date.setText("Request sent "+Utility.ConvertUTCToUserTimezoneForSlot(item.getSel_meeting().getRequest_date()));
                txt_region.setText(""+item.getRequestorCountryName());
                builder.setView(view1);
                dialogDetails = builder.create();
                dialogDetails.setCancelable(false);


                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDetails.dismiss();
                    }
                });

                dialogDetails.show();
            } catch(Exception e){
                e.getMessage();
            }
        }
    }
}