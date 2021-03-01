package com.thriive.app.fragments;

import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.R;
import com.thriive.app.adapters.MatchingListAdapter;
import com.thriive.app.adapters.RequestPagerAdapter;
import com.thriive.app.adapters.RequesterListAdapter;
import com.thriive.app.models.CommonMatchingPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MatchingIntroFragment extends BottomSheetDialogFragment {

    private static CommonMatchingPOJO server_response;
    @BindView(R.id.viewpager_matching)
    ViewPager viewpager_matching;

    @BindView(R.id.layout_dotmatch)
    LinearLayout layout_dotmatch;

    @BindView(R.id.btn_got_it)
    TextView btn_got_it;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_msg)
    TextView txt_msg;
    @BindView(R.id.img_close)
    ImageView img_close;
    Unbinder unbinder;
    TextView[] dotRequest;
    private ArrayList<CommonMatchingPOJO.EntityListPOJO> entityListPOJOS;

    private int currentPage = 0;
    private Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000;

    @BindView(R.id.txt_01)
    TextView txt_01;

    @BindView(R.id.txt_02)
    TextView txt_02;
    private LoginPOJO.ReturnEntity loginPOJO;

    public MatchingIntroFragment() {
        // Required empty public constructor
    }

    public static MatchingIntroFragment newInstance(CommonMatchingPOJO res) {
        MatchingIntroFragment fragment = new MatchingIntroFragment();
        server_response = res;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.SheetDialog);
      
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting_request_new, container, false);
        unbinder = ButterKnife.bind(this, view);
        loginPOJO  = Utility.getLoginData(getContext());
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = (FrameLayout)
                        dialog.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        setUpUI();
        return view;
    }

    private void setUpUI() {
        if (server_response.getOK()){
            if (server_response.getEntityList().size() > 0){

                entityListPOJOS = new ArrayList<>();
                entityListPOJOS.addAll(server_response.getEntityList());

                ArrayList<CommonMatchingPOJO.EntityListPOJO> arrayList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    CommonMatchingPOJO.EntityListPOJO data = server_response.getEntityList().get(i);
                    arrayList.add(data);

                }
                MatchingListAdapter matchingListAdapter = new MatchingListAdapter(getActivity(), MatchingIntroFragment.this, arrayList);
                viewpager_matching.setAdapter(matchingListAdapter);
                viewpager_matching.addOnPageChangeListener(viewPagerPageChangeListener);

                txt_name.setVisibility(View.VISIBLE);
                txt_name.setText("Hey "+ loginPOJO.getFirstName() +",");
                txt_msg.setText("Thanks for your request. Thriive is now searching for your best Match!!");

                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == arrayList.size()) {
                            if (timer != null){
                                timer.cancel();
                            }
                        }
                        viewpager_matching.setCurrentItem(currentPage++, true);

                    }
                };

                timer = new Timer(); // This will create a new Thread
                timer.schedule(new TimerTask() { // task to be scheduled
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, DELAY_MS, PERIOD_MS);
                //setRequestData();
            }
        }else {
            Toast.makeText(getActivity(), server_response.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            currentPage = position;
            //addDotRequest(position);
            if (position == 0){
                txt_01.setBackground(getActivity().getResources().getDrawable(R.drawable.filled_circle_terracota));
                txt_02.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_green_layout));
                txt_name.setVisibility(View.VISIBLE);
                txt_name.setText("Hey "+ loginPOJO.getFirstName() +",");
                txt_msg.setText("Thanks for your request. Thriive is now searching for your best Match!!");
            } else if (position == 1){
                txt_01.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_green_layout));
                txt_02.setBackground(getActivity().getResources().getDrawable(R.drawable.filled_circle_terracota));
                txt_name.setVisibility(View.VISIBLE);
                txt_name.setText("");
                txt_msg.setText("Expect to meet similar profiles for your match");
            }else  if (position == 2){
                txt_name.setVisibility(View.VISIBLE);
                txt_name.setText("");
                txt_msg.setText("You will receive a notification of your match within 72 hrs");
                txt_01.setBackground(getActivity().getResources().getDrawable(R.drawable.filled_circle_terracota));
                txt_02.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_green_layout));
            }
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

    };

//    private void setRequestData() {
//        viewpager_matching.setPadding(0, 0, 30, 0);
//        addDotRequest(0);
//        // whenever the page changes
//        viewpager_matching.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//            }
//            @Override
//            public void onPageSelected(int i) {
//                try {
//                    addDotRequest(i);
//
//
//                } catch (Exception e){
//                    e.getMessage();
//                }
//            }
//            @Override
//            public void onPageScrollStateChanged(int i) {
//            }
//        });
//    }

    private void addDotRequest(int page_position) {
        try {
            dotRequest = new TextView[entityListPOJOS.size()];
            layout_dotmatch.removeAllViews();
            for (int i = 0; i < dotRequest.length; i++) {;
                dotRequest[i] = new TextView(getActivity());
                dotRequest[i].setText(Html.fromHtml("&#9679;"));
                dotRequest[i].setTextSize(20);
                dotRequest[i].setTextColor(getResources().getColor(R.color.darkSeaGreen));
                layout_dotmatch.addView(dotRequest[i]);
            }
            //active dot
            dotRequest[page_position].setTextColor(getResources().getColor(R.color.colorAccent));
        } catch (Exception e){
            e.getMessage();
        }
    }

    @OnClick({R.id.img_close,R.id.btn_got_it})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.img_close:
                    timer.cancel();
                    dismiss();
                    break;
                case R.id.btn_got_it:
                    timer.cancel();
                    dismiss();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            dismiss();
        }

    }
}