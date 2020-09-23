package com.thriive.app.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.HomeActivity;
import com.thriive.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**

 */
public class MeetingAvailabilityFragment extends BottomSheetDialogFragment {

    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    @BindView(R.id.txt_edit)
    TextView txt_edit;

    Unbinder unbinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.SheetDialog);
    }

    public MeetingAvailabilityFragment() {
        // Required empty public constructor
    }

    public static MeetingAvailabilityFragment newInstance() {
        return new MeetingAvailabilityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting_availability, container, false);
        unbinder = ButterKnife.bind(this,view);
        return  view;
    }

    @OnClick({R.id.txt_edit, R.id.btn_confirm, R.id.edit, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_cancel:
                dismiss();
                ((HomeActivity)getActivity()).meetingEditDate();
                break;

            case R.id.edit:
                dismiss();
                ((HomeActivity)getActivity()).meetingEditDate();
             //   meetingEditDate();
                break;

            case R.id.btn_confirm:
                dismiss();
               // ((HomeActivity)getActivity()).onBackPressed();
                break;

            case R.id.img_close:
                dismiss();
                //((HomeActivity)getActivity()).onBackPressed();

                break;
        }
    }





}