package com.thriive.app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.HomeActivity;
import com.thriive.app.MeetingJoinActivity;
import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.adapters.BusinessProfessionAdapter;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.ExpertiseAdapter;
import com.thriive.app.models.CommonRequesterPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 */
public class MeetingDetailsFragment extends BottomSheetDialogFragment {
    @BindView(R.id.rv_expertise)
    RecyclerView rv_expertise;
    Unbinder unbinder;
    @BindView(R.id.rv_experience)
    RecyclerView rv_experience;
    @BindView(R.id.img_bg)
    ImageView img_bg;
    @BindView(R.id.layout_data)
    LinearLayout layout_data;
    private BusinessProfessionAdapter businessProfessionAdapter;
    private ExpertiseAdapter expertiseAdapter;
    private ArrayList<CommonRequesterPOJO> arrayList   = new ArrayList<>();

    public MeetingDetailsFragment() {
        // Required empty public constructor
    }

    public static MeetingDetailsFragment newInstance() {
        return new MeetingDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.SheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_details, container,
                false);
        unbinder = ButterKnife.bind(this, view);
        arrayList.add(new CommonRequesterPOJO("AI"));
        arrayList.add(new CommonRequesterPOJO("Expert"));
        arrayList.add(new CommonRequesterPOJO("Data Analyst"));
        arrayList.add(new CommonRequesterPOJO("Marketing"));
        arrayList.add(new CommonRequesterPOJO("Business"));
        arrayList.add(new CommonRequesterPOJO("Data Science"));

       // arrayList.add(new CommonReOJO());

        businessProfessionAdapter = new BusinessProfessionAdapter(getContext(), arrayList);
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getContext());
        rv_expertise.setLayoutManager(gridLayout );
        rv_expertise.setAdapter(new ExpertiseAdapter(getContext(), arrayList));

        rv_experience.setLayoutManager(new FlexboxLayoutManager(getContext()) );
        rv_experience.setAdapter(new ExperienceAdapter(getContext(), arrayList));
        // get the views and attach the listener


        img_bg.setMaxHeight(layout_data.getHeight());

        return view;

    }

    @OnClick({R.id.txt_cancel, R.id.join_meeting, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_cancel:
                meetingCancel();
                break;

            case R.id.join_meeting:
                dismiss();
                joinMeeting();
                break;

            case R.id.img_close:
                dismiss();
                break;
        }
    }

    private void joinMeeting() {
        Intent intent = new Intent(getContext(), MeetingJoinActivity.class);
        startActivity(intent);
    }


     public void meetingCancel(){
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
         LayoutInflater layoutInflater = this.getLayoutInflater();
         final View view1 = layoutInflater.inflate(R.layout.dialog_cancel_meeting, null);

         Button accept = view1.findViewById(R.id.txt_10);
         Button decline = view1.findViewById(R.id.txt_11);
         ImageView img_close = view1.findViewById(R.id.img_close);


         //    tv_msg.setText("Session Added Successfully.");
         builder.setView(view1);
         final AlertDialog dialogs = builder.create();
         dialogs.setCancelable(true);

         // rv_expertise.setLayoutManager(new FlexboxLayoutManager(NotificationListActivity.this) );
         img_close.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 dialogs.dismiss();
                 //   ratingDialog();
             }
         });
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
                 meetingCancelConfirmation();
             }
         });
         dialogs.show();
     }

    private void meetingCancelConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_meeting_cancel_alert, null);

        Button btn_TimeMatch = view1.findViewById(R.id.btn_TimeMatch);
        Button btm_noTime = view1.findViewById(R.id.btm_noTime);
        ImageView img_close = view1.findViewById(R.id.img_close);


        //    tv_msg.setText("Session Added Successfully.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(true);

        // rv_expertise.setLayoutManager(new FlexboxLayoutManager(NotificationListActivity.this) );
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                //   ratingDialog();
            }
        });
        btn_TimeMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        btn_TimeMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
              //  meetingCancelConfirmation();
            }
        });
        dialogs.show();

    }

}