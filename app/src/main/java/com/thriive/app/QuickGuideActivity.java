package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.adapters.BusinessProfessionAdapter;
import com.thriive.app.adapters.MeetingRequestSelectionAdapter;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.SelectBusinessPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuickGuideActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.layoutDots)
    LinearLayout dotsLayout;

    @BindView(R.id.txt_close)
    TextView txt_close;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.btn_next)
    ImageView btn_next;
    @BindView(R.id.layout_wh1)
    LinearLayout layout_wh1;

    private int currentPage = 0;
    private MyViewPagerAdapter myViewPagerAdapter;

    private int[] scree_list;
    private SharedData sharedData;

    private LoginPOJO loginPOJO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_guide);
        ButterKnife.bind(this);

        loginPOJO = Utility.getLoginData(getApplicationContext());

        sharedData = new SharedData(getApplicationContext());
        sharedData.addBooleanData(SharedData.isFirstVisit, true);
        scree_list = new int[]{
                R.layout.app_walkthrough1,
                R.layout.app_walkthrough2,
                R.layout.app_walkthrogh3,
                R.layout.app_walkthrough4};

        myViewPagerAdapter = new MyViewPagerAdapter();
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        txt_name.setText(loginPOJO.getReturnEntity().getFirstName());


    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            //if (position =  )


            //addBottomDots(position);
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

    };

    @OnClick({R.id.txt_close, R.id.btn_next, R.id.btn_wt2})
    public void onViewClicked(View view){
        switch (view.getId()) {

            case R.id.btn_wt2:
                if (currentPage == 1){
                    currentPage = 2;
                    txt_close.setVisibility(View.GONE);
                    viewPager.setCurrentItem(2, true);
                }
                break;

            case R.id.txt_close:
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("intent_type", "FLOW");
                startActivity(intent);
                finishAffinity();
                break;

            case R.id.btn_next:
                if (currentPage == 0){
                    txt_close.setVisibility(View.GONE);
                    viewPager.setCurrentItem(1, true);
                    layout_wh1.setVisibility(View.GONE);
                    currentPage = 1;
                } else  if (currentPage == 2){
                //   txt_close.setVisibility(View.GONE);
                    currentPage = 3;
                    viewPager.setCurrentItem(3, true);
                    txt_close.setVisibility(View.VISIBLE);
                    sharedData.addBooleanData(SharedData.isFirstVisit, true);
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.putExtra("intent_type", "FLOW");
                            startActivity(intent);
                            finishAffinity();
                        }
                    }, 2000);
                } else if (currentPage == 3){
                    Intent intent1 = new Intent(getApplicationContext(), HomeActivity.class);
                    intent1.putExtra("intent_type", "FLOW");
                    startActivity(intent1);
                    finishAffinity();
                }

        }
    }
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.view_pager_meeting_request, container, false);
            container.addView(view);

            RelativeLayout vp_layout = view.findViewById(R.id.vp_layout);
            LayoutInflater inf = LayoutInflater.from(getApplicationContext());
            View child;
            child = inf.inflate(scree_list[position], null);
            vp_layout.addView(child);

            return view;
        }

        @Override
        public int getCount() {
            return scree_list.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sharedData.addBooleanData(SharedData.isFirstVisit, true);
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("intent_type", "FLOW");
        startActivity(intent);
        finishAffinity();
    }

}