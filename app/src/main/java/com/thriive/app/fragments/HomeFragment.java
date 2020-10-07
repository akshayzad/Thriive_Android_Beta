package com.thriive.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;
import com.thriive.app.HomeActivity;
import com.thriive.app.R;
import com.thriive.app.adapters.RequestedAdapter;
import com.thriive.app.adapters.RequesterListAdapter;
import com.thriive.app.adapters.ScheduleListAdapter;
import com.thriive.app.adapters.ScheduledAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonHomePOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RequesterListAdapter requesterListAdapter;
    private ScheduleListAdapter scheduleListAdapter;

    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList = new ArrayList<>();


    @BindView(R.id.recycler_requester)
    RecyclerView recyclerRequester;
    @BindView(R.id.recycler_schedule)
    RecyclerView recyclerSchedule;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.refresh_view)
    SwipeRefreshLayout refreshView;

    private LoginPOJO loginPOJO;

    Unbinder unbinder;
    private APIInterface apiInterface;
    private static String TAG = HomeFragment.class.getName();
    private KProgressHUD progressHUD;

    private SharedData sharedData;
    private String UUID;
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

        apiInterface = APIClient.getApiInterface();

        loginPOJO  = Utility.getLoginData(getContext());

        sharedData = new SharedData(getActivity());

       // getMeetingRequest();

      //  getPendingRequest();

        UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();


        ArrayList arrayList  = new ArrayList();
        arrayList.add(new CommonMeetingListPOJO().getMeetingList());
        arrayList.add(new CommonMeetingListPOJO().getMeetingList());

       // scheduleListAdapter = new ScheduleListAdapter(getActivity(),arrayList);
        //recyclerSchedule.setAdapter(scheduleListAdapter);

        loginPOJO = Utility.getLoginData(getContext());
        txt_name.setText(""+loginPOJO.getReturnEntity().getFirstName());

        refreshView.setOnRefreshListener(this);
        refreshView.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
       // cAdapter.removeAll();
        refreshView.setRefreshing(false);

        return  view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe()
    public void onMessageEvent(EventBusPOJO event) {
        if (event.getEvent() == Utility.MEETING_CANCEL){
            onResume();
            //  ((MeetingsFragment()).onResume();
        }

        if (event.getEvent() == Utility.END_CALL_DIALOG){
            Toast.makeText(getContext(), "ended", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "event  " +  event.getMeeting_id());
          //  ((HomeActivity)getActivity()).showMeetingDialog(event.getMeeting_id());
            //showMeetingDialog();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView.setRefreshing(true);
        getMeetingHome();
    }

    private void getMeetingHome() {
        UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
        if (UUID  == null)
        {
            UUID = "";
        }
        Log.d(TAG, " token "+ sharedData.getStringData(SharedData.PUSH_TOKEN));
        Call<CommonHomePOJO> call = apiInterface.getMeetingHome(loginPOJO.getReturnEntity().getActiveToken(),
                loginPOJO.getReturnEntity().getRowcode(),  UUID);
        call.enqueue(new Callback<CommonHomePOJO>() {
            @Override
            public void onResponse(Call<CommonHomePOJO> call, Response<CommonHomePOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
             //       progressHUD.dismiss();
                    CommonHomePOJO pojo = response.body();
                    Log.d(TAG,""+pojo.getMessage());
                    if (pojo.getOK()) {
                        if (pojo.getMeetingScheduledList() != null){
                            scheduleListAdapter = new ScheduleListAdapter(getActivity(), (ArrayList<CommonMeetingListPOJO.MeetingListPOJO>) pojo.getMeetingScheduledList());
                            recyclerSchedule.setAdapter(scheduleListAdapter);
                        }

                        if (pojo.getMeetingRequestList() != null)
                        {
                            requesterListAdapter = new RequesterListAdapter(getActivity(), (ArrayList<PendingMeetingRequestPOJO.MeetingRequestList>) pojo.getMeetingRequestList());
                            recyclerRequester.setAdapter(requesterListAdapter);
                        }

                        ((HomeActivity)getActivity()).setNoti(pojo.getPendingRequestCount());
                        // recycler_requested.setAdapter(requestedAdapter);
                        Toast.makeText(getContext(), "Success "+pojo.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Failure "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    if(refreshView != null) {
                        refreshView.setRefreshing(false);
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonHomePOJO> call, Throwable t) {
             //   progressHUD.dismiss();
                Toast.makeText(getContext(), "Getting Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getMeetingRequest() {
        progressHUD = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<PendingMeetingRequestPOJO> call = apiInterface.getPendingMeeting(loginPOJO.getReturnEntity().getActiveToken(),
                loginPOJO.getReturnEntity().getRowcode());
        call.enqueue(new Callback<PendingMeetingRequestPOJO>() {
            @Override
            public void onResponse(Call<PendingMeetingRequestPOJO> call, Response<PendingMeetingRequestPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    PendingMeetingRequestPOJO pojo = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+pojo.getMessage());
                    if (pojo.getOK()) {
                        requesterListAdapter = new RequesterListAdapter(getActivity(), (ArrayList<PendingMeetingRequestPOJO.MeetingRequestList>) pojo.getMeetingRequestList());
                        recyclerRequester.setAdapter(requesterListAdapter);
                        // recycler_requested.setAdapter(requestedAdapter);
                        Toast.makeText(getContext(), "Success "+pojo.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Failure "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }
            @Override
            public void onFailure(Call<PendingMeetingRequestPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(getContext(), "Getting Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRefresh() {
        onResume();
    }
}