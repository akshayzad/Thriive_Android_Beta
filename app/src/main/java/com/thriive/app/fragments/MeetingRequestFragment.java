package com.thriive.app.fragments;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.adapters.MeetingRequestAdapter;
import com.thriive.app.adapters.MeetingRequestSelectionAdapter;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.SelectBusinessPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MeetingRequestFragment extends BottomSheetDialogFragment {

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

    @BindView(R.id.layout_meeting_request)
    LinearLayout layout_meeting_request;
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

    private ArrayList<CommonRequesterPOJO> arrayList = new ArrayList<>();
    Unbinder unbinder;

    public MeetingRequestFragment() {
        // Required empty public constructor
    }

    public static MeetingRequestFragment newInstance() {
        return new MeetingRequestFragment();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_request, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        ArrayList<SelectBusinessPOJO> arrayList1 = new ArrayList<>();
        arrayList1.add(new SelectBusinessPOJO(getContext().getDrawable(R.drawable.ic_mr1)));
        arrayList1.add(new SelectBusinessPOJO(getContext().getDrawable(R.drawable.ic_mr2)));

        arrayList1.add(new SelectBusinessPOJO(getContext().getDrawable(R.drawable.ic_mr3)));

        arrayList1.add(new SelectBusinessPOJO(getContext().getDrawable(R.drawable.ic_mr4)));

        rv_profession.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_profession.setAdapter(new MeetingRequestAdapter(getActivity(), MeetingRequestFragment.this,
                arrayList1));

        arrayList.add(new CommonRequesterPOJO("Data Analyst"));
        arrayList.add(new CommonRequesterPOJO("Marketing"));
        arrayList.add(new CommonRequesterPOJO("Business"));
        arrayList.add(new CommonRequesterPOJO("Data Science"));
        arrayList.add(new CommonRequesterPOJO("Expert"));
        arrayList.add(new CommonRequesterPOJO("Data Analyst"));
        arrayList.add(new CommonRequesterPOJO("Marketing"));
        arrayList.add(new CommonRequesterPOJO("Data Science"));

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2,
                LinearLayoutManager.HORIZONTAL,false);
        rv_expertise.setLayoutManager(mLayoutManager);

        rv_expertise.setAdapter(new MeetingRequestSelectionAdapter(getContext(), MeetingRequestFragment.this, arrayList));

        rv_domain.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false));
        rv_domain.setAdapter(new MeetingRequestSelectionAdapter(getActivity(), MeetingRequestFragment.this, arrayList));

        rv_subdomain.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false));
        rv_subdomain.setAdapter(new MeetingRequestSelectionAdapter(getActivity(), MeetingRequestFragment.this, arrayList));

        rv_region.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        rv_region.setAdapter(new MeetingRequestSelectionAdapter(getActivity(), MeetingRequestFragment.this, arrayList));

        return  view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style. AppBottomSheetDialogTheme);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @OnClick({R.id.btn_request_meeting, R.id.img_close, R.id.card_mr1, R.id.card_mr2, R.id.card_mr3, R.id.card_mr4, R.id.card_mr5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_request_meeting:

                successDialog();
                break;

            case R.id.img_close:
                dismiss();
                //onCancel();
                break;

            case R.id.card_mr1:
                meetingRequest();
                break;

            case R.id.card_mr2:
                meetingRequest();
                break;

            case R.id.card_mr3:
                meetingRequest();
                break;

            case  R.id.card_mr4:
                meetingRequest();
                break;

            case  R.id.card_mr5:
                meetingRequest();
                break;

        }
    }

    public void init(){
        layout_meeting_request.setVisibility(View.VISIBLE);
        layout_meeting.setVisibility(View.GONE);
        layout_domain.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        layout_subdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);
    }
    public void meetingRequest(){
        layout_meeting_request.setVisibility(View.GONE);
        layout_meeting.setVisibility(View.VISIBLE);
        layout_domain.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        layout_subdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);

        layout_meeting_preference.setVisibility(View.GONE);
    }
    public void meetingRequestResult(){
        layout_meeting_request.setVisibility(View.GONE);
        layout_meeting.setVisibility(View.VISIBLE);
        layout_domain.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.VISIBLE);
        layout_subdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);

    }

    public void meetingRequestExpertise(){
        layout_meeting_request.setVisibility(View.GONE);
        layout_meeting.setVisibility(View.VISIBLE);
        layout_domain.setVisibility(View.VISIBLE);
        layout_expertise.setVisibility(View.VISIBLE);
        layout_subdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.VISIBLE);
        layout_meeting_preference.setVisibility(View.VISIBLE);
    }
    public void successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_request_meeting_success, null);
        TextView label_close = view1.findViewById(R.id.label_close);

    //    tv_msg.setText("Session Added Successfully.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogs.dismiss();
                ratingDialog();
            }
        });
        dialogs.show();
    }

    public void ratingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_rate_app, null);
        ImageView img_close = view1.findViewById(R.id.img_close);

        //    tv_msg.setText("Session Added Successfully.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
       // dialogs.setCancelable(false);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogs.dismiss();
            }
        });
        dialogs.show();
    }

}