package com.thriive.app.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.R;

/**

 */
public class MeetingRequestAcceptFragment extends BottomSheetDialogFragment {


    public MeetingRequestAcceptFragment() {
        // Required empty public constructor
    }
    public static MeetingRequestFragment MeetingRequestAcceptFragment() {
        return new MeetingRequestFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting_request_accept, container, false);
    }
}