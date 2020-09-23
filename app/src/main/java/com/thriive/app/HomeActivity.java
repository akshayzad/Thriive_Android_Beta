package com.thriive.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thriive.app.adapters.ViewPagerAdapter;
import com.thriive.app.fragments.HomeFragment;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.fragments.MeetingsFragment;
import com.thriive.app.utilities.spacenavigation.SpaceItem;
import com.thriive.app.utilities.spacenavigation.SpaceNavigationView;
import com.thriive.app.utilities.spacenavigation.SpaceOnClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.space)
    SpaceNavigationView spaceNavigationView;
    @BindView(R.id.img_logo)
    ImageView img_logo;
    private ViewPagerAdapter viewPagerAdapter;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
       // tab();
       // spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("Meetings", R.drawable.ic_group));
        //spaceNavigationView.showIconOnly();
        spaceNavigationView.setFont(getResources().getFont(R.font.roboto_medium));
        spaceNavigationView.setElevation((float) 11.0);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        setupViewPager(viewPager);

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
    }

    public void setupViewPager(ViewPager viewPager){
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new HomeFragment());
        viewPagerAdapter.addFragment(new MeetingsFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void setMeetingFragment(){
        viewPager.setCurrentItem(1);
        spaceNavigationView.changeCurrentItem(1);
    }

    public void meetingEditDate(){
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_date, null);
        BottomSheetDialog dialog = new BottomSheetDialog(HomeActivity.this,R.style.SheetDialog);

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
    public void meetingEditTime(){
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_time, null);
        BottomSheetDialog dialog = new BottomSheetDialog(HomeActivity.this,R.style.SheetDialog);

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
                startActivity(intent1);
                break;

        }
    }
    public void callFragment(){
        MeetingRequestFragment addPhotoBottomDialogFragment =
                (MeetingRequestFragment) MeetingRequestFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "add_photo_dialog_fragment");
    }
}