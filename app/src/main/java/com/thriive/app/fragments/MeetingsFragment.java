package com.thriive.app.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thriive.app.R;
import com.thriive.app.adapters.RequestedAdapter;
import com.thriive.app.adapters.ScheduledAdapter;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.utilities.scrollingpagerindicator.ScrollingPagerIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MeetingsFragment extends Fragment {
    private RequestedAdapter requestedAdapter;
    private ScheduledAdapter scheduledAdapter;

    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList = new ArrayList<>();


    @BindView(R.id.recycler_requested)
    RecyclerView recycler_requested;
    @BindView(R.id.recycler_scheduled)
    RecyclerView recycler_scheduled;
    Unbinder unbinder;
    public MeetingsFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        Fragment fragment = new MeetingsFragment();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_meeetings, container, false);
        unbinder = ButterKnife.bind(this, view);
        requesterPOJOArrayList.add(new CommonRequesterPOJO());
        requesterPOJOArrayList.add(new CommonRequesterPOJO());
        requesterPOJOArrayList.add(new CommonRequesterPOJO());

        requestedAdapter = new RequestedAdapter(getActivity(),requesterPOJOArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recycler_requested.setLayoutManager(layoutManager);
        recycler_requested.setAdapter(requestedAdapter);
        ScrollingPagerIndicator recyclerIndicator = view.findViewById(R.id.indicator_requster);
        recyclerIndicator.setSelectedDotColor(getContext().getColor(R.color.colorAccent));

        recyclerIndicator.attachToRecyclerView(recycler_requested);

        scheduledAdapter = new ScheduledAdapter(getActivity(), MeetingsFragment.this, requesterPOJOArrayList);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recycler_scheduled.setLayoutManager(layoutManager1);
        recycler_scheduled.setAdapter(scheduledAdapter);

        ScrollingPagerIndicator recyclerIndicator1 = view.findViewById(R.id.indicator_schedule);
        recyclerIndicator1.setSelectedDotColor(getContext().getColor(R.color.colorAccent));
        recyclerIndicator1.attachToRecyclerView(recycler_scheduled);

        return view;
    }

    public void callFragment(){
        MeetingDetailsFragment addPhotoBottomDialogFragment =
                (MeetingDetailsFragment) MeetingDetailsFragment.newInstance();
        addPhotoBottomDialogFragment.show(getFragmentManager(),
                "add_photo_dialog_fragment");
    }

}