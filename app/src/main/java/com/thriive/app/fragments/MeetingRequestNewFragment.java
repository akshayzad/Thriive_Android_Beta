package com.thriive.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.R;

import static androidx.fragment.app.DialogFragment.STYLE_NORMAL;

public class MeetingRequestNewFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    

    public MeetingRequestNewFragment() {
        // Required empty public constructor
    }

    public static MeetingRequestNewFragment newInstance() {
        MeetingRequestNewFragment fragment = new MeetingRequestNewFragment();

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
        return inflater.inflate(R.layout.fragment_meeting_request_new,
                container, false);
    }
}