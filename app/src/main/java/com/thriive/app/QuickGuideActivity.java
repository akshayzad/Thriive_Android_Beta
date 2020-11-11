package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import java.util.Timer;

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


    @BindView(R.id.txt_prev)
    TextView txt_prev;

    @BindView(R.id.txt_next)
    TextView txt_next;

    @BindView(R.id.view_close)
    RelativeLayout view_close;
    @BindView(R.id.view_skip)
    RelativeLayout view_skip;
    @BindView(R.id.txt_skip)
    TextView txt_skip;


    private TextView[] dots;

    private int currentPage = 0;
    private MyViewPagerAdapter myViewPagerAdapter;
    private Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000;


    private int[] scree_list;
    private SharedData sharedData;

    private LoginPOJO.ReturnEntity loginPOJO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_guide);
        ButterKnife.bind(this);

        loginPOJO = Utility.getLoginData(getApplicationContext());

        sharedData = new SharedData(getApplicationContext());
        sharedData.addBooleanData(SharedData.isFirstVisit, true);

        scree_list = new int[]{
                R.drawable.app_wt1,
                R.drawable.app_wt2,
                R.drawable.app_wt3,
                R.drawable.app_wt4,
                R.drawable.app_wt5,
                R.drawable.app_wt6};

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        txt_name.setText(loginPOJO.getFirstName());

        addDots(0);

//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == 0){
//                    layout_wh1.setVisibility(View.GONE);
//                    txt_close.setVisibility(View.GONE);
//                } else {
//                    layout_wh1.setVisibility(View.GONE);
//                }
//                if (currentPage == scree_list.length - 0)
//                {
//
//
//                  //  txt_close.setVisibility(View.VISIBLE);
//                   // timer.cancel();
////                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
////                    intent.putExtra("intent_type", "FLOW");
////                    startActivity(intent);
////                    finishAffinity();
//
//                } else {
//                    viewPager.setCurrentItem(currentPage++, true);
//                }
//
//            }
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer.schedule(new TimerTask() { // task to be scheduled
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);

    }

    private void addDots(int currentPage) {
        dots = new TextView[scree_list.length];

        int[] colorsActive = getResources().getIntArray(R.array.view_dot_inactive);
        int[] colorsInactive = getResources().getIntArray(R.array.view_active);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(32);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dots[i].setPadding(3, 0, 3, 0);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            currentPage = position;
            addDots(currentPage);

            if (position == scree_list.length - 1) {
                txt_next.setText(getResources().getString(R.string.close));
                txt_close.setVisibility(View.VISIBLE);
                view_close.setVisibility(View.VISIBLE);
                view_skip.setVisibility(View.GONE);
                txt_next.setVisibility(View.VISIBLE);

            } else {
                txt_next.setText(getResources().getString(R.string.next));
                txt_close.setVisibility(View.GONE);
                view_close.setVisibility(View.GONE);
                view_skip.setVisibility(View.VISIBLE);
                txt_next.setVisibility(View.VISIBLE);
            }
            if (position == 0){
                layout_wh1.setVisibility(View.GONE);
                txt_prev.setVisibility(View.INVISIBLE);
                txt_next.setVisibility(View.VISIBLE);
                //txt_close.setVisibility(View.GONE);
            } else {
                txt_prev.setVisibility(View.VISIBLE);
                layout_wh1.setVisibility(View.GONE);
            }

        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

    };

    @OnClick({R.id.txt_close, R.id.txt_prev, R.id.txt_next, R.id.view_close, R.id.view_skip})
    public void onViewClicked(View view){
        switch (view.getId()) {

            case R.id.txt_close:
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("intent_type", "FLOW");
                startActivity(intent);
                finishAffinity();

                break;

            case  R.id.txt_prev:
                if (currentPage > 0) {
                    currentPage --;
                    viewPager.setCurrentItem(currentPage);
                }
                break;

            case R.id.txt_next:
                if (currentPage < scree_list.length - 1) {
                    currentPage ++;
                    viewPager.setCurrentItem(currentPage);
                } else {
                    onBackPressed();
                }
                break;
            case R.id.view_close:
                onBackPressed();
                break;

            case R.id.view_skip:

                onBackPressed();
                break;
        }
    }
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_app_walkthrough, container, false);
            container.addView(view);

            ImageView imageView = view.findViewById(R.id.image);

            Glide.with(view.getContext())
                    .load(scree_list[position])
                    .into(imageView);


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