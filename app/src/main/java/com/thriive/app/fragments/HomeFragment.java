package com.thriive.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thriive.app.R;
import com.thriive.app.adapters.RequesterListAdapter;
import com.thriive.app.adapters.ScheduleListAdapter;
import com.thriive.app.models.CommonRequesterPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

    private RequesterListAdapter requesterListAdapter;
    private ScheduleListAdapter scheduleListAdapter;

    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList = new ArrayList<>();


    @BindView(R.id.recycler_requester)
    RecyclerView recyclerRequester;
    @BindView(R.id.recycler_schedule)
    RecyclerView recyclerSchedule;

    Unbinder unbinder;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance() {
        Fragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        requesterPOJOArrayList.add(new CommonRequesterPOJO());
        requesterPOJOArrayList.add(new CommonRequesterPOJO());

        requesterListAdapter = new RequesterListAdapter(getActivity(),requesterPOJOArrayList);
        recyclerRequester.setAdapter(requesterListAdapter);

        scheduleListAdapter = new ScheduleListAdapter(getActivity(), requesterPOJOArrayList);
        recyclerSchedule.setAdapter(scheduleListAdapter);

        return  view;
    }


}