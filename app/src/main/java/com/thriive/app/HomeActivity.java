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
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.ViewPagerAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.fragments.HomeFragment;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.fragments.MeetingsFragment;

import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.showcaseviewlib.TutoShowcase;
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
    private ViewPagerAdapter viewPagerAdapter;
    private SharedData sharedData;

    private   String meetingId = "";
    private APIInterface apiInterface;
    private KProgressHUD progressHUD;
    private LoginPOJO loginPOJO;
    private static String TAG = HomeActivity.class.getName();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        apiInterface = APIClient.getApiInterface();

        sharedData = new SharedData(getApplicationContext());
        loginPOJO  = Utility.getLoginData(getApplicationContext());
        sharedData.addIntData(SharedData.USER_ID, loginPOJO.getReturnEntity().getEntityId());

       // popupMeetingRequest(pojo.getMeetingObject());

        spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("My Meetings", R.drawable.ic_group));
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_medium);
        spaceNavigationView.setFont(typeface);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        setupViewPager(viewPager);

        if (!sharedData.getBooleanData(SharedData.isFirstVisit)) {
            TutoShowcase tutoShowcase = new TutoShowcase(this);
            tutoShowcase
                    .setContentView(R.layout.showcase_homepage)
                    .onClickContentView(R.id.txt_close, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tutoShowcase.dismiss();
                            sharedData.addBooleanData(SharedData.isFirstVisit, true);
                            //   Toast.makeText(HomeActivity.this, "skip", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .on(R.id.uu)
                    .addCircle()
                    .onClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tutoShowcase.dismiss();
                            //    Toast.makeText(HomeActivity.this, "ce nter ,sdlf ", Toast.LENGTH_SHORT).show();
                            callFragment1();
                        }
                    })
                    //.displaySwipableRight()
                    .show();

        }

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                callFragment();
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

    private void getPendingMeeting() {
        Call<CommonMeetingPOJO> call = apiInterface.getMeetingById(loginPOJO.getReturnEntity().getActiveToken(), meetingId);
        call.enqueue(new Callback<CommonMeetingPOJO>() {
            @Override
            public void onResponse(Call<CommonMeetingPOJO> call, Response<CommonMeetingPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonMeetingPOJO pojo = response.body();
                    Log.d(TAG,""+pojo.getMessage());
                    if (pojo.getOK()) {

                        popupMeetingRequest(pojo.getMeetingObject());

                    } else {
                        Toast.makeText(getApplicationContext(), "Failure "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onFailure(Call<CommonMeetingPOJO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Getting Error", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void getMeetingById() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonMeetingPOJO> call = apiInterface.getMeetingById(loginPOJO.getReturnEntity().getActiveToken(),
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

    private void showMeeting() {
        setMeetingFragment();
        //sharedData.addBooleanData(SharedData.isFirstVisit, true);
        TutoShowcase tutoShowcase = new TutoShowcase(this);
        tutoShowcase
                .setContentView(R.layout.showcase_meeting)
                .onClickContentView(R.id.btn_skip, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tutoShowcase.dismiss();
//                        Toast.makeText(HomeActivity.this, "skip", Toast.LENGTH_SHORT).show();
                    }
                })
                .onClickContentView(R.id.btn_done, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tutoShowcase.dismiss();
//                        Toast.makeText(HomeActivity.this, "next", Toast.LENGTH_SHORT).show();
                    }
                })
                .on(R.id.meeting) //a view in actionbar
                .addRoundRect()
                .onClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tutoShowcase.dismiss();

                    }
                })
                //.displaySwipableRight()
                .show();
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

    public void meetingEditDate() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_date, null);
        BottomSheetDialog dialog = new BottomSheetDialog(HomeActivity.this, R.style.SheetDialog);

        Button btn_next = dialogView.findViewById(R.id.btn_next);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
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
        BottomSheetDialog dialog = new BottomSheetDialog(HomeActivity.this, R.style.SheetDialog);

        Button btn_confirm = dialogView.findViewById(R.id.btn_confirm);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
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
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();


    }

    public void callFragment1() {

        Intent intent = new Intent(getApplicationContext(), QuickGuideActivity.class);
        //startActivity(intent);
        startActivityForResult(intent, 123);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 123) {
            showMeeting();
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
            getPendingMeeting();
           // popupMeetingRequest(pojo.getMeetingObject());
            //  ((MeetingsFragment()).onResume();
        }

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

    @OnClick({R.id.img_profile, R.id.img_notification})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_profile:
                //();
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.img_notification:
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