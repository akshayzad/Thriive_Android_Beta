package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.adapters.BusinessProfessionAdapter;
import com.thriive.app.adapters.MeetingRequestSelectionAdapter;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.SelectBusinessPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.showcaseviewlib.TutoShowcase;
import com.thriive.app.utilities.spacenavigation.SpaceItem;
import com.thriive.app.utilities.spacenavigation.SpaceNavigationView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuickGuideActivity extends AppCompatActivity {
    @BindView(R.id.rv_profession)
    RecyclerView rv_profession;
    @BindView(R.id.rv_expertise)
    RecyclerView rv_expertise;
    @BindView(R.id.rv_domain)
    RecyclerView rv_domain;
    @BindView(R.id.rv_subdomain)
    RecyclerView rv_subdomain;
    @BindView(R.id.rv_region)
    RecyclerView rv_region;

    @BindView(R.id.layout_meetings)
    LinearLayout layout_meetings;
    @BindView(R.id.layout_meeting_request)
    LinearLayout layout_meeting_request;
    @BindView(R.id.layout_request)
    LinearLayout layout_request;
    @BindView(R.id.layout_meeting)
    LinearLayout layout_meeting;
    @BindView(R.id.layout_meeting_preference)
    LinearLayout layout_meeting_preference;
    @BindView(R.id.layout_expertise)
    LinearLayout layout_expertise;
    @BindView(R.id.layout_domain)
    LinearLayout layout_domain;
    @BindView(R.id.layout_subdomain)
    LinearLayout layout_subdomain;
    @BindView(R.id.layout_region)
    LinearLayout layout_region;
    @BindView(R.id.card_mr1)
    CardView cardView_mr1;

    @BindView(R.id.recycler_profession)
    RecyclerView recycler_profession;
    @BindView(R.id.recycler_schedule)
    RecyclerView recycler_schedule;
    private SharedData sharedData;
    @BindView(R.id.space)
    SpaceNavigationView spaceNavigationView;

    boolean isClose1, isClose2, isClose3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_guide);
        ButterKnife.bind(this);
        sharedData = new SharedData(getApplicationContext());
        init();       // spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("My Meetings", R.drawable.ic_group));
        //spaceNavigationView.showIconOnly();
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_medium);
        spaceNavigationView.setFont(typeface);
//        spaceNavigationView.setElevation((float) 11.0);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);


        TutoShowcase tutoShowcase = new TutoShowcase(this);
        tutoShowcase
                .setContentView(R.layout.showcase_request_meeting)
                .onClickContentView(R.id.txt_close, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isClose1 = false;
                        tutoShowcase.dismiss();
                      //  finish();
                        //Toast.makeText(HomeA, "skip", Toast.LENGTH_SHORT).show();
                    }
                })
                .onClickContentView(R.id.btn_next, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tutoShowcase.dismiss();
                        //    Toast.makeText(HomeActivity.this, "next", Toast.LENGTH_SHORT).show();
                        //   showMeeting();
                    }
                })
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                        if(!isClose1){
                            finish();
                        }
                    }
                })
                .on(R.id.img_01) //a view in actionbar
                .addRoundRect()
                .onClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isClose1 = true;
                        tutoShowcase.dismiss();

                        meetingRequestExpertise();
                    }
                })
                //.displaySwipableRight()
                .show();

    }
    public void init(){
        layout_meetings.setVisibility(View.GONE);
        layout_request.setVisibility(View.VISIBLE);
        layout_meeting_request.setVisibility(View.VISIBLE);
        layout_meeting.setVisibility(View.GONE);
        layout_domain.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        layout_subdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);

        ArrayList<CommonRequesterPOJO> arrayList = new ArrayList<>();


        arrayList.add(new CommonRequesterPOJO("Data Analyst"));
        arrayList.add(new CommonRequesterPOJO("Marketing"));
        arrayList.add(new CommonRequesterPOJO("Business"));
        arrayList.add(new CommonRequesterPOJO("Data Science"));
        arrayList.add(new CommonRequesterPOJO("Expert"));
        arrayList.add(new CommonRequesterPOJO("Data Analyst"));
        arrayList.add(new CommonRequesterPOJO("Marketing"));
        arrayList.add(new CommonRequesterPOJO("Data Science"));

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2,
                LinearLayoutManager.HORIZONTAL,false);
        rv_expertise.setLayoutManager(mLayoutManager);

        rv_expertise.setAdapter(new MeetingRequestSelectionAdapter(getApplicationContext(), arrayList));

        rv_domain.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.HORIZONTAL, false));
        rv_domain.setAdapter(new MeetingRequestSelectionAdapter(getApplicationContext(), arrayList));


        rv_subdomain.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.HORIZONTAL, false));
        rv_subdomain.setAdapter(new MeetingRequestSelectionAdapter(getApplicationContext(),  arrayList));

        rv_region.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false));
        rv_region.setAdapter(new MeetingRequestSelectionAdapter(getApplicationContext(), arrayList));




        ArrayList<CommonRequesterPOJO> arrayList1 = new ArrayList<>();
        arrayList1.add(new CommonRequesterPOJO("Expert"));
        arrayList1.add(new CommonRequesterPOJO("Data Analyst"));
        arrayList1.add(new CommonRequesterPOJO("Marketing"));
        arrayList1.add(new CommonRequesterPOJO("Data Science"));
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getApplicationContext());
        recycler_profession.setLayoutManager(gridLayout );
        recycler_profession.setAdapter(new BusinessProfessionAdapter(QuickGuideActivity.this, arrayList1));
        FlexboxLayoutManager gridLayout1 = new FlexboxLayoutManager(getApplicationContext());
        recycler_schedule.setLayoutManager(gridLayout1);
        recycler_schedule.setAdapter(new BusinessProfessionAdapter(QuickGuideActivity.this, arrayList1));

    }
    public void meetingRequestExpertise(){
        layout_meetings.setVisibility(View.GONE);
        layout_request.setVisibility(View.VISIBLE);
        layout_meeting_request.setVisibility(View.GONE);
        layout_meeting.setVisibility(View.VISIBLE);
        layout_domain.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.VISIBLE);
        layout_subdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.VISIBLE);
        layout_meeting_preference.setVisibility(View.VISIBLE);
        TutoShowcase tutoShowcase = new TutoShowcase(this);
        tutoShowcase
                .setContentView(R.layout.showcase_book_meeting)
                .onClickContentView(R.id.txt_close, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isClose2 = false;
                        tutoShowcase.dismiss();
                      //  finish();
                        //Toast.makeText(HomeA, "skip", Toast.LENGTH_SHORT).show();
                    }
                })
                .onClickContentView(R.id.btn_next, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tutoShowcase.dismiss();
                        //    Toast.makeText(HomeActivity.this, "next", Toast.LENGTH_SHORT).show();
                        //   showMeeting();
                    }
                })
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                        if (!isClose2){
                            finish();
                        }
                    }
                })
                .on(R.id.btn_request_meeting) //a view in actionbar
                .addRoundRect()
                .onClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isClose2 = true;
                        tutoShowcase.dismiss();
                        setMetting();

                    }
                })
                //.displaySwipableRight()
                .show();

    }

    private void setMetting() {
        layout_meetings.setVisibility(View.VISIBLE);
        layout_request.setVisibility(View.GONE);
        spaceNavigationView.changeCurrentItem(1);
        sharedData.addBooleanData(SharedData.isFirstVisit, true);
        TutoShowcase tutoShowcase1 = new TutoShowcase(this);
        tutoShowcase1
                .setContentView(R.layout.showcase_meeting)
                .onClickContentView(R.id.txt_close, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isClose3 = false;
                        tutoShowcase1.dismiss();
                      //  finish();
                    }
                })
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                        if (!isClose3){
                            finish();
                        }
                    }
                })
                .on(R.id.meeting) //a view in actionbar
                .addRoundRect()
                .onClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isClose3 = true;
                        tutoShowcase1.dismiss();
                        //meetingRequestExpertise();
                        finish();

                    }
                })

                //.displaySwipableRight()
                .show();
    }
}