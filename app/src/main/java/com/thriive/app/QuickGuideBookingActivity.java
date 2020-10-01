package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.adapters.BusinessProfessionAdapter;
import com.thriive.app.adapters.MeetingRequestSelectionAdapter;
import com.thriive.app.adapters.RequestedAdapter;
import com.thriive.app.adapters.ScheduledAdapter;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.utilities.showcaseviewlib.TutoShowcase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuickGuideBookingActivity extends AppCompatActivity {
    @BindView(R.id.rv_expertise)
    RecyclerView rv_expertise;
//    @BindView(R.id.rv_domain)
//    RecyclerView rv_domain;
//    @BindView(R.id.rv_subdomain)
//    RecyclerView rv_subdomain;
    @BindView(R.id.rv_region)
    RecyclerView rv_region;

    private RequestedAdapter requestedAdapter;
    private ScheduledAdapter scheduledAdapter;

    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_guide_booking);
        ButterKnife.bind(this);
        init();


        TutoShowcase tutoShowcase = new TutoShowcase(this);
        tutoShowcase
                .setContentView(R.layout.showcase_book_meeting)
                .onClickContentView(R.id.btn_skip, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tutoShowcase.dismiss();
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
                .on(R.id.btn_request_meeting) //a view in actionbar
                .addRoundRect()
                //.displaySwipableRight()
                .show();
    }

    private void init() {

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


        rv_region.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false));
        rv_region.setAdapter(new MeetingRequestSelectionAdapter(getApplicationContext(), arrayList));




    }
}