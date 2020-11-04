package com.thriive.app;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.RequesterListAdapter;
import com.thriive.app.adapters.ScheduleListAdapter;
import com.thriive.app.adapters.ViewPagerAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.fragments.HomeFragment;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.fragments.MeetingsFragment;

import com.thriive.app.fragments.ProfileFragment;
import com.thriive.app.models.CommonHomePOJO;
import com.thriive.app.models.CommonMeetingCountPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
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
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
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

    private ViewPagerAdapter viewPagerAdapter;
    private SharedData sharedData;

    private   String meetingId = "", UUID = "", time_stamp = "", reason = "";;
    private APIInterface apiInterface;
    private KProgressHUD progressHUD;
    private LoginPOJO.ReturnEntity loginPOJO;
    private static final String TAG = HomeActivity.class.getName();

    private int rating_int = 0, isRelevantMatchSelect;
    boolean isDidntMeet = false, isRelevantMatch = false  , isDidntMeetSelect ;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        apiInterface = APIClient.getApiInterface();

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
        spaceNavigationView1.addSpaceItem(new SpaceItem("My Meetings", R.drawable.ic_group));
        spaceNavigationView1.setCentreButtonIconColorFilterEnabled(false);


        spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("My Meetings", R.drawable.ic_group));
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_medium);
        spaceNavigationView.setFont(typeface);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
       // spaceNavigationView.showIconOnly();
        setupViewPager(viewPager);

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                getMeetingCount();

                // Toast.makeText(HomeActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                viewPager.setCurrentItem(itemIndex);
                //   Toast.makeText(HomeActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                //   Toast.makeText(HomeActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });

        if (getIntent().getStringExtra("intent_type").equals("NOTI")){
            getMeetingById();

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
                                    Log.d(TAG, " "+ e.getMessage());
                                }

                            }
                        } catch (Exception e){
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


    public void setNoti(int noti) {
        if (noti != 0){
            txt_notification.setText(""+noti);
            txt_notification.setVisibility(View.VISIBLE);
        } else {
            txt_notification.setVisibility(View.GONE);
        }

    }



    public void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new HomeFragment());
        viewPagerAdapter.addFragment(new MeetingsFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void setMeetingFragment() {
        viewPager.setCurrentItem(1);
        spaceNavigationView.changeCurrentItem(1);
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
            setMeetingFragment();
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
        //getMeetingHome();
    }


    private void getMeetingHome() {
        TimeZone timeZone = TimeZone.getDefault();
        Log.d(TAG, "time zone "+ timeZone.getID());
        UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
        if (UUID  == null) {
            UUID = "";
        }
        Log.d(TAG, " token "+ sharedData.getStringData(SharedData.PUSH_TOKEN));
        Call<CommonHomePOJO> call = apiInterface.getMeetingHome(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/get-meetings-home", loginPOJO.getActiveToken(),
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
                                // recycler_requested.setAdapter(requestedAdapter);
                                // Toast.makeText(getContext(), "Success "+pojo.getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                //Toast.makeText(getContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
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
                    getSaveMeetingReview("",  isRelevantMatchSelect,  flag_no_show,
                            (int) rating_app.getRating(),  (int) rating_meeting.getRating());
                } else {
                    if (rating_app.getRating() != 0.0 && rating_meeting.getRating() != 0.0 && isRelevantMatch) {
                        dialogs.dismiss();
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
    }
}