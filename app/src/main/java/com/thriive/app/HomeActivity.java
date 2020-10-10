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
import com.thriive.app.adapters.ViewPagerAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.fragments.HomeFragment;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.fragments.MeetingsFragment;

import com.thriive.app.models.CommonMeetingCountPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.spacenavigation.SpaceItem;
import com.thriive.app.utilities.spacenavigation.SpaceNavigationView;
import com.thriive.app.utilities.spacenavigation.SpaceOnClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

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

    private   String meetingId = "";
    private APIInterface apiInterface;
    private KProgressHUD progressHUD;
    private LoginPOJO.ReturnEntity loginPOJO;
    private static String TAG = HomeActivity.class.getName();

    private int rating_int = 0;
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


        String UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();

        Log.d(TAG, " UUID "+ UUID);

        spaceNavigationView1.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        spaceNavigationView1.addSpaceItem(new SpaceItem("My Meetings", R.drawable.ic_group));
        spaceNavigationView1.setCentreButtonIconColorFilterEnabled(false);

        spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("My Meetings", R.drawable.ic_group));
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_medium);
        spaceNavigationView.setFont(typeface);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
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
    }

    private void getMeetingCount() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonMeetingCountPOJO> call = apiInterface.getMeetingCount(loginPOJO.getActiveToken(), loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonMeetingCountPOJO>() {
                @Override
                public void onResponse(Call<CommonMeetingCountPOJO> call, Response<CommonMeetingCountPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        progressHUD.dismiss();
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
                    }
                }
                @Override
                public void onFailure(Call<CommonMeetingCountPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(getApplicationContext(), "Getting Error", Toast.LENGTH_SHORT).show();
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
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        sharedData.addBooleanData(SharedData.isFirstVisit, false);
        sharedData.addBooleanData(SharedData.isLogged, false);
        sharedData.clearPref(getApplicationContext());
        Utility.clearLogin(getApplicationContext());
        Utility.clearMeetingDetails(getApplicationContext());
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressHUD.dismiss();
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
        Call<CommonMeetingPOJO> call = apiInterface.getMeetingById(loginPOJO.getActiveToken(),
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
                        Utility.saveMeetingDetailsData(getApplicationContext(), pojo.getMeetingObject());
                        MeetingDetailsFragment meetingDetailsFragment =
                                (MeetingDetailsFragment) MeetingDetailsFragment.newInstance();
                        meetingDetailsFragment.show(getSupportFragmentManager(), "MeetingDetailsFragment");
                        // recycler_requested.setAdapter(requestedAdapter);
                        Toast.makeText(getApplicationContext(), "Success "+pojo.getMessage(), Toast.LENGTH_SHORT).show();

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
            showMeetingDialog(meetingId);
        }
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

                dialogs.dismiss();
                String reason = txt_didntMeet.getText().toString();
                getSaveMeetingReview(reason, 0);
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
                if (rating.getRating() == 0.0){
                    Toast.makeText(HomeActivity.this, "Please select rating", Toast.LENGTH_SHORT).show();
                } else {
                    dialogs.dismiss();
                    rating_int = (int) rating.getRating();
                    String reason = txt_reason1.getText().toString();
                    getSaveMeetingReview(reason, rating_int);
                }

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
                if (rating.getRating() == 0.0){
                    Toast.makeText(HomeActivity.this, "Please select rating", Toast.LENGTH_SHORT).show();
                } else {
                    dialogs.dismiss();
                    rating_int = (int) rating.getRating();
                    String reason = txt_reason2.getText().toString();
                    getSaveMeetingReview(reason, rating_int);
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

                if (rating.getRating() == 0.0){
                    Toast.makeText(HomeActivity.this, "Please select rating", Toast.LENGTH_SHORT).show();
                } else {
                    dialogs.dismiss();
                    rating_int = (int) rating.getRating();
                    String reason = txt_reason3.getText().toString();
                    getSaveMeetingReview(reason, rating_int);
                }
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

                if (rating.getRating() == 0.0){
                    Toast.makeText(HomeActivity.this, "Please select rating", Toast.LENGTH_SHORT).show();
                } else {
                    dialogs.dismiss();
                    rating_int = (int) rating.getRating();
                    Log.d(TAG,""+ rating_int);
                    String reason = txt_reason4.getText().toString();
                    getSaveMeetingReview(reason, rating_int);
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

    public void getSaveMeetingReview(String review_text, int review_int) {
        Log.d(TAG, "review int " + review_int);
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getSaveMeetingReview(loginPOJO.getActiveToken(),
                meetingId, loginPOJO.getRowcode(),review_text ,review_text, review_int);
        call.enqueue(new Callback<CommonPOJO>() {
            @Override
            public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMessage());
                    if (reasonPOJO != null){
                        if (reasonPOJO.getOK()) {
                            Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failure "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(HomeActivity.this, "Getting Error", Toast.LENGTH_SHORT).show();
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
//
//    @Override
//    public void onBackPressed() {
//      //  super.onBackPressed();
//
//    }
}