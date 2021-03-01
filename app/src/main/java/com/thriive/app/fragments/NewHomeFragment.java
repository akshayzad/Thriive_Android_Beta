package com.thriive.app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.thriive.app.HomeActivity;
import com.thriive.app.R;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.FeaturedObjectivesAdapter;
import com.thriive.app.adapters.FeaturedThriiversAdapter;
import com.thriive.app.adapters.MeetingNewTimeAdapterForHomeFragment;
import com.thriive.app.adapters.RequesterListAdapter;
import com.thriive.app.adapters.ScheduleListAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonAttributeL2POJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonRequestTimeSlots;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.FeaturedObjectivesPOJO;
import com.thriive.app.models.HomeDisplayPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.MeetingDetailPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thriive.app.utilities.Utility.checkInternet;

public class NewHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.layout_start_journey)
    LinearLayout layout_start_journey;
    @BindView(R.id.layout_recommended)
    FrameLayout layout_recommended;
    @BindView(R.id.layout_next_meeting)
    LinearLayout layout_next_meeting;
    @BindView(R.id.next_meeting_card)
    CardView next_meeting_card;
    @BindView(R.id.layout_featured_thriivers)
    LinearLayout layout_featured_thriivers;
    @BindView(R.id.layout_featured_objectives)
    LinearLayout layout_featured_objectives;

    @BindView(R.id.txt_start_journey_header)
    TextView txt_start_journey_header;
    @BindView(R.id.txt_start_journey_desc)
    TextView txt_start_journey_desc;
    @BindView(R.id.txt_journey_header_card)
    TextView txt_journey_header_card;
    @BindView(R.id.txt_journey_desc_card)
    TextView txt_journey_desc_card;
    @BindView(R.id.img_recommended)
    ImageView img_recommended;

    @BindView(R.id.nm_name)
    TextView nm_name;
    @BindView(R.id.nm_reason_name)
    TextView nm_reason_name;
    @BindView(R.id.nm_connection_count)
    TextView nm_connection_count;
    @BindView(R.id.nm_meeting_tag)
    TextView nm_meeting_tag;
    @BindView(R.id.nm_label_date)
    TextView nm_label_date;
    @BindView(R.id.nm_label_time)
    TextView nm_label_time;
    @BindView(R.id.nm_img_user)
    ImageView nm_img_user;

    @BindView(R.id.txt_nName)
    TextView txtName;
    @BindView(R.id.refresh_view)
    SwipeRefreshLayout refreshView;
    @BindView(R.id.rv_featured_obj)
    RecyclerView rv_featured_obj;
    @BindView(R.id.rv_featured_thriivers)
    RecyclerView rv_featured_thriivers;

    @BindView(R.id.tag_intrestedin_domain)
    TextView tag_intrestedin_domain;
    @BindView(R.id.tag_intrestedin_expertise)
    TextView tag_intrestedin_expertise;

    View view;
    private LoginPOJO.ReturnEntity loginPOJO;
    Unbinder unbinder;
    private APIInterface apiInterface;
    private static String TAG = HomeFragment.class.getName();
    private KProgressHUD progressHUD;

    private SharedData sharedData;
    private String UUID, time_stamp = "";
    private CleverTapAPI cleverTap;
    private AlertDialog dialogDetails;
    LinearLayout layout_accept_enable;
    private String meeting_slot_id="",startTime, endTime;

    private String reason_id="", reason_name="", l1_attrib_id="", l1_attrib_name="", l2_attrib_id="", l2_attrib_name="", giver_persona_id="", giver_persona_name="",
            domain_id="", domain_name="", sub_domain_id="", sub_domain_name="", expertise_id="", expertise_name="", rowcode="";

    boolean flag_domain = false, flag_expertise = false, flag_req_text = false,
            flag_l1 = false, flag_l2 = false;

    private RequesterListAdapter requesterListAdapter;
    private ScheduleListAdapter scheduleListAdapter;

    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList = new ArrayList<>();

    public NewHomeFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        Fragment fragment = new NewHomeFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_home, container, false);

        unbinder = ButterKnife.bind(this, view);
        requesterPOJOArrayList.add(new CommonRequesterPOJO());
        requesterPOJOArrayList.add(new CommonRequesterPOJO());

        apiInterface = APIClient.getApiInterface();
        cleverTap = CleverTapAPI.getDefaultInstance(getActivity());
        loginPOJO  = Utility.getLoginData(getContext());

        sharedData = new SharedData(getActivity());



        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                try{
                    time_stamp =""+ Utility.getTimeStamp();
                } catch (Exception e){

                }
            } else {
                TimeZone timeZone = TimeZone.getDefault();
                Log.d(TAG, "time zone "+ timeZone.getID());
                time_stamp = timeZone.getID();
            }
        } catch(Exception e){
            e.getMessage();
        }



        UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();


        ArrayList arrayList  = new ArrayList();
        arrayList.add(new CommonMeetingListPOJO().getMeetingList());
        arrayList.add(new CommonMeetingListPOJO().getMeetingList());

        // scheduleListAdapter = new ScheduleListAdapter(getActivity(),arrayList);
        //recyclerSchedule.setAdapter(scheduleListAdapter);

        loginPOJO = Utility.getLoginData(getContext());
        txtName.setText(loginPOJO.getFirstName()+",");
        refreshView.setOnRefreshListener(this);
        refreshView.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        // cAdapter.removeAll();
        refreshView.setRefreshing(false);

        getNewHome();

        return view;
    }

    public void setFeaturedObjectives(List<HomeDisplayPOJO.FeaturedRequest> objList){
        FeaturedObjectivesAdapter adapter = new FeaturedObjectivesAdapter(getActivity(), objList,NewHomeFragment.this);
        rv_featured_obj.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_featured_obj.setAdapter(adapter);

    }

    public void setFeaturedThriivers(List<HomeDisplayPOJO.EntityList> objList){
        FeaturedThriiversAdapter adapter = new FeaturedThriiversAdapter(getActivity(), objList,NewHomeFragment.this);
        rv_featured_thriivers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_featured_thriivers.setAdapter(adapter);

    }

    public void setOnFeaturedObjectivesClicked(HomeDisplayPOJO.FeaturedRequest item){
        reason_id = ""+item.getReasonId();
        reason_name = ""+item.getReasonName();
        l1_attrib_id = ""+item.getL1AttribId();
        l1_attrib_name = ""+item.getL1AttribName();
        l2_attrib_id = ""+item.getL2AttribId();
        l2_attrib_name = ""+item.getL2AttribName();
        giver_persona_id = ""+item.getGiverPersonaId();
        giver_persona_name = ""+item.getGiverPersonaName();
        domain_id = ""+item.getDomainId();
        domain_name = ""+item.getDomainName();
        sub_domain_id = ""+item.getSubDomainId();
        sub_domain_name = ""+item.getSubDomainName();
        expertise_id = ""+item.getExpertiseId();
        expertise_name = ""+item.getExpertiseName();
        rowcode = ""+item.getReasonRowcode();
        flag_domain = item.getFlagDomain();
        flag_expertise = item.getFlagExpertise();
        flag_l1 = item.getFlagL1();
        flag_l2 = item.getFlagL2();

        callMeetingRequstFragment();
    }


    public void callMeetingRequstFragment(){
        MeetingRequestFragment fragment = MeetingRequestFragment.newInstance();
        Bundle args = new Bundle();
        args.putString("reason_id",""+reason_id);
        args.putString("reason_name",reason_name);
        args.putString("l1_attrib_id",""+l1_attrib_id);
        args.putString("l1_attrib_name",""+l1_attrib_name);
        args.putString("l2_attrib_id",""+l2_attrib_id);
        args.putString("l2_attrib_name",""+l2_attrib_name);
        args.putString("giver_persona_id",""+giver_persona_id);
        args.putString("giver_persona_name",giver_persona_name);
        args.putString("domain_id",""+domain_id);
        args.putString("domain_name",domain_name);
        args.putString("sub_domain_id",""+sub_domain_id);
        args.putString("sub_domain_name",""+sub_domain_name);
        args.putString("expertise_id",""+expertise_id);
        args.putString("expertise_name",""+expertise_name);
        args.putString("expertise_name",""+expertise_name);
        args.putString("rowcode",""+rowcode);
        args.putBoolean("flag_domain",flag_domain);
        args.putBoolean("flag_expertise",flag_expertise);
        args.putBoolean("flag_l1",flag_l1);
        args.putBoolean("flag_l2",flag_l2);
        fragment.setArguments(args);
        callFragment(fragment);
    }

    public void getNewHome(){

        Call<HomeDisplayPOJO> call = apiInterface.getNewHome(sharedData.getStringData(SharedData.API_URL) + "api/AppHome/get-new-home", loginPOJO.getActiveToken(),""+loginPOJO.getRowcode(),
                loginPOJO.getPushToken(), ""+TimeZone.getDefault().getID(),
                time_stamp);
        call.enqueue(new Callback<HomeDisplayPOJO>() {
            @Override
            public void onResponse(Call<HomeDisplayPOJO> call, Response<HomeDisplayPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "homePOJO" + response.toString());
                    Log.d(TAG, "homePOJO" + new Gson().toJson(response.body()));
                    HomeDisplayPOJO homePojo = response.body();

                    if (homePojo.getIsOK()) {

                        if (homePojo.getHomeDisplayType().equals("First_Meeting")) {

                            reason_id = "" + homePojo.getMeetingTemplate().getReasonId();
                            reason_name = "" + homePojo.getMeetingTemplate().getReasonName();
                            l1_attrib_id = "" + homePojo.getMeetingTemplate().getL1AttribId();
                            l1_attrib_name = "" + homePojo.getMeetingTemplate().getL1AttribName();
                            l2_attrib_id = "" + homePojo.getMeetingTemplate().getL2AttribId();
                            l2_attrib_name = "" + homePojo.getMeetingTemplate().getL2AttribName();
                            giver_persona_id = "" + homePojo.getMeetingTemplate().getGiverPersonaId();
                            giver_persona_name = "" + homePojo.getMeetingTemplate().getGiverPersonaName();
                            domain_id = "" + homePojo.getMeetingTemplate().getDomainId();
                            domain_name = "" + homePojo.getMeetingTemplate().getDomainName();
                            sub_domain_id = "" + homePojo.getMeetingTemplate().getSubDomainId();
                            sub_domain_name = "" + homePojo.getMeetingTemplate().getSubDomainName();
                            expertise_id = "" + homePojo.getMeetingTemplate().getExpertiseId();
                            expertise_name = "" + homePojo.getMeetingTemplate().getExpertiseName();
                            rowcode = "" + homePojo.getMeetingTemplate().getReasonRowcode();
                            flag_domain = homePojo.getMeetingTemplate().getFlagDomain();
                            flag_expertise = homePojo.getMeetingTemplate().getFlagExpertise();
                            flag_l1 = homePojo.getMeetingTemplate().getFlagL1();
                            flag_l2 = homePojo.getMeetingTemplate().getFlagL2();

                            txt_start_journey_header.setText(homePojo.getTop_title());
                            txt_start_journey_desc.setText(homePojo.getTop_text());
                            txt_journey_header_card.setText(homePojo.getMeetingTemplate().getCardTitle());
                            txt_journey_desc_card.setText(homePojo.getMeetingTemplate().getCardText());
                            if (getActivity() != null)
                                Glide.with(getActivity())
                                        .load(sharedData.getStringData(SharedData.API_URL) + "reason_button/" + homePojo.getMeetingTemplate().getReasonRowcode() + ".png")
                                        .into(img_recommended);


                            layout_start_journey.setVisibility(View.VISIBLE);
                            layout_recommended.setVisibility(View.VISIBLE);
                            layout_next_meeting.setVisibility(View.GONE);
                            layout_featured_thriivers.setVisibility(View.GONE);

                            layout_recommended.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    callMeetingRequstFragment();
                                }
                            });

                        }
                        if (homePojo.getHomeDisplayType().equals("Featured_Profiles")) {

                            if (homePojo.getEntityList() != null) {
                                setFeaturedThriivers(homePojo.getEntityList());
                                tag_intrestedin_domain.setText(homePojo.getEntityList().get(0).getDomainList().get(0));
                                tag_intrestedin_expertise.setText(homePojo.getEntityList().get(0).getExpertiseList().get(0));
                            }


                            layout_start_journey.setVisibility(View.GONE);
                            layout_recommended.setVisibility(View.GONE);
                            layout_next_meeting.setVisibility(View.GONE);
                            layout_featured_thriivers.setVisibility(View.VISIBLE);
                        }
                        if (homePojo.getHomeDisplayType().equals("Meeting_Display")) {

                            if (homePojo.getMeetingData() != null) {
                                if (homePojo.getMeetingData().getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
                                    nm_name.setText(homePojo.getMeetingData().getGiverName());
                                    Glide.with(getActivity())
                                            .load(homePojo.getMeetingData().getGiverPicUrl())
                                            .into(nm_img_user);
                                } else {
                                    nm_name.setText(homePojo.getMeetingData().getRequestorName());
                                    if (getActivity() != null)
                                        Glide.with(getActivity())
                                                .load(homePojo.getMeetingData().getRequestorPicUrl())
                                                .into(nm_img_user);
                                }

                                nm_reason_name.setText("Meeting For " + homePojo.getMeetingData().getMeetingReason());
                                if (homePojo.getMeetingData().getMeetingTag().get(0) != null) {
                                    if (!homePojo.getMeetingData().getMeetingTag().get(0).equals("")) {
                                        nm_meeting_tag.setVisibility(View.VISIBLE);
                                        nm_meeting_tag.setText(homePojo.getMeetingData().getMeetingTag().get(0));
                                    } else nm_meeting_tag.setVisibility(View.GONE);
                                } else {
                                    nm_meeting_tag.setVisibility(View.GONE);
                                }
                                nm_label_date.setText(Utility.getMeetingDate(Utility.ConvertUTCToUserTimezone(homePojo.getMeetingData().getPlanStartTime())));
                                nm_label_time.setText(Utility.getMeetingTime(Utility.ConvertUTCToUserTimezone(homePojo.getMeetingData().getPlanStartTime()),
                                        Utility.ConvertUTCToUserTimezone(homePojo.getMeetingData().getPlanEndTime())));


                                layout_start_journey.setVisibility(View.GONE);
                                layout_recommended.setVisibility(View.GONE);
                                layout_next_meeting.setVisibility(View.VISIBLE);
                                layout_featured_thriivers.setVisibility(View.GONE);

                                next_meeting_card.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        HomeDisplayPOJO.MeetingData meetingListPOJO = homePojo.getMeetingData();
                                        Utility.saveNextMeetingDetailsData(getActivity(), meetingListPOJO);
                                        NextMeetingDetailsFragment addPhotoBottomDialogFragment =
                                                (NextMeetingDetailsFragment) NextMeetingDetailsFragment.newInstance();
                                        addPhotoBottomDialogFragment.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(),
                                                "add_photo_dialog_fragment");
                                    }
                                });
                            }
                        }
                        if (homePojo.getFeaturedRequests() != null) {
                            layout_featured_objectives.setVisibility(View.VISIBLE);
                            setFeaturedObjectives(homePojo.getFeaturedRequests());
                        } else {
                            layout_featured_objectives.setVisibility(View.GONE);
                        }
                    }
//                    ((HomeActivity)getActivity()).setNoti(pojo.getPendingRequestCount());
                    ((HomeActivity)getActivity()).getMeetingHome();
                    if(refreshView != null) {
                        refreshView.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<HomeDisplayPOJO> call, Throwable t) {
                if(refreshView != null) {
                    refreshView.setRefreshing(false);
                }
            }
        });
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

    @SuppressLint("NewApi")
    @Subscribe()
    public void onMessageEvent(EventBusPOJO event) {
        if (event.getEvent() == Utility.MEETING_CANCEL){
            onResume();
            //  ((MeetingsFragment()).onResume();
        }
        if (event.getEvent() == Utility.MEETING_BOOK){
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
        if (refreshView != null){
            refreshView.setRefreshing(true);
        }
        NetworkInfo networkInfo = checkInternet(getActivity());
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

            getNewHome();
        } else {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        onResume();
    }

    public void setMeetingNewTime(PendingMeetingRequestPOJO.MeetingRequestList item) {
        if (item != null){
            try {
                if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
                    LayoutInflater layoutInflater = this.getLayoutInflater();
                    final View view1 = layoutInflater.inflate(R.layout.dialog_new_time, null);

                    ImageView img_close = view1.findViewById(R.id.img_close);
                    TextView label_date = view1.findViewById(R.id.label_date);
                    TextView txt_persona = view1.findViewById(R.id.txt_persona);
                    RecyclerView rv_tags = view1.findViewById(R.id.rv_tags);
                    TextView txt_reason = view1.findViewById(R.id.txt_reason);
                    TextView txt_tags = view1.findViewById(R.id.txt_tags);
                    TextView txt_support = view1.findViewById(R.id.txt_support);
                    ImageButton img_accept_step2_enable = view1.findViewById(R.id.img_accept_step2_enable);
                    RecyclerView rv_slots = view1.findViewById(R.id.rv_slots);
                    layout_accept_enable = view1.findViewById(R.id.layout_accept_enable);
                    layout_accept_enable.setVisibility(View.GONE);

                    txt_persona.setText("with "+item.getSel_meeting().getRequestor_name());
                    txt_reason.setText("Meeting for "+item.getReasonName());
                    //txt_tags.setText(""+item.getMeetingLabel());
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.addAll(item.getDomainTags());
                    arrayList.addAll(item.getSubDomainTags());
                    arrayList.addAll(item.getExpertiseTags());
                    ArrayList<String> finalArrayList = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (i <= 3){
                            finalArrayList.add(arrayList.get(i));
                        }
                    }
                    FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getActivity());
                    rv_tags.setLayoutManager(gridLayout );
                    if (finalArrayList.size() > 0){
                        txt_tags.setVisibility(View.VISIBLE);
                    } else {
                        txt_tags.setVisibility(View.GONE);
                    }
                    rv_tags.setAdapter(new ExperienceAdapter(getActivity(), finalArrayList));

                    rv_slots.setLayoutManager(new GridLayoutManager(getActivity(), 3));

                    ArrayList<MeetingDetailPOJO> reverse_new_time = reverseArrayList((ArrayList<MeetingDetailPOJO>) item.getSlot_list());

                    ArrayList<MeetingDetailPOJO> dialog_new_time = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        MeetingDetailPOJO data = reverse_new_time.get(i);
                        dialog_new_time.add(data);
                    }

                    ArrayList<MeetingDetailPOJO> reverse__time = reverseArrayList((ArrayList<MeetingDetailPOJO>) dialog_new_time);

                    if (reverse__time.size() > 0){
                        rv_slots.setAdapter(new MeetingNewTimeAdapterForHomeFragment(getActivity(),NewHomeFragment.this,
                                (ArrayList<MeetingDetailPOJO>) reverse__time));
                    }

                    //txt_details.setText(""+item.getSel_meeting().getReq_text());
                    label_date.setText("Request sent on "+Utility.ConvertUTCToUserTimezoneForSlot(item.getSel_meeting().getRequest_date()));
                    builder.setView(view1);
                    dialogDetails = builder.create();
                    dialogDetails.setCancelable(false);

                    img_accept_step2_enable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAcceptMeeting(item,item.getSel_meeting().getMeeting_code(),true);
                        }
                    });
                    img_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogDetails.dismiss();
                        }
                    });

                    txt_support.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogThriiveSupport(item,item.getSel_meeting().getMeeting_code(),false);
                        }
                    });

                    dialogDetails.show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
                    LayoutInflater layoutInflater = this.getLayoutInflater();
                    final View view1 = layoutInflater.inflate(R.layout.dialog_new_time, null);

                    ImageView img_close = view1.findViewById(R.id.img_close);
                    TextView label_date = view1.findViewById(R.id.label_date);
                    TextView txt_persona = view1.findViewById(R.id.txt_persona);
                    RecyclerView rv_tags = view1.findViewById(R.id.rv_tags);
                    TextView txt_reason = view1.findViewById(R.id.txt_reason);
                    TextView txt_tags = view1.findViewById(R.id.txt_tags);
                    TextView txt_support = view1.findViewById(R.id.txt_support);
                    ImageButton img_accept_step2_enable = view1.findViewById(R.id.img_accept_step2_enable);
                    RecyclerView rv_slots = view1.findViewById(R.id.rv_slots);
                    layout_accept_enable = view1.findViewById(R.id.layout_accept_enable);
                    layout_accept_enable.setVisibility(View.GONE);


                    txt_persona.setText("with "+item.getSel_meeting().getGiver_name());
                    txt_reason.setText("Meeting for "+item.getSel_meeting().getReason_name());
                    //txt_tags.setText(""+item.getMeetingLabel());
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.addAll(item.getDomainTags());
                    arrayList.addAll(item.getSubDomainTags());
                    arrayList.addAll(item.getExpertiseTags());
                    ArrayList<String> finalArrayList = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (i <= 3){
                            finalArrayList.add(arrayList.get(i));
                        }
                    }
                    FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getActivity());
                    rv_tags.setLayoutManager(gridLayout );
                    if (finalArrayList.size() > 0){
                        txt_tags.setVisibility(View.VISIBLE);
                    } else {
                        txt_tags.setVisibility(View.GONE);
                    }
                    rv_tags.setAdapter(new ExperienceAdapter(getActivity(), finalArrayList));

                    rv_slots.setLayoutManager(new GridLayoutManager(getActivity(), 3));

                    ArrayList<MeetingDetailPOJO> reverse_new_time = reverseArrayList((ArrayList<MeetingDetailPOJO>) item.getSlot_list());

                    ArrayList<MeetingDetailPOJO> dialog_new_time = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        MeetingDetailPOJO data = reverse_new_time.get(i);
                        dialog_new_time.add(data);
                    }

                    ArrayList<MeetingDetailPOJO> reverse__time = reverseArrayList((ArrayList<MeetingDetailPOJO>) dialog_new_time);

                    if (reverse__time.size() > 0){
                        rv_slots.setAdapter(new MeetingNewTimeAdapterForHomeFragment(getActivity(),NewHomeFragment.this,
                                (ArrayList<MeetingDetailPOJO>) reverse__time));
                    }

                    //txt_details.setText(""+item.getSel_meeting().getReq_text());
                    label_date.setText("Request sent "+Utility.ConvertUTCToUserTimezoneForSlot(item.getSel_meeting().getRequest_date()));
                    builder.setView(view1);
                    dialogDetails = builder.create();
                    dialogDetails.setCancelable(false);

                    img_accept_step2_enable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAcceptMeeting(item,item.getSel_meeting().getMeeting_code(),true);
                        }
                    });

                    img_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogDetails.dismiss();
                        }
                    });

                    txt_support.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogThriiveSupport(item,item.getSel_meeting().getMeeting_code(),false);
                        }
                    });

                    dialogDetails.show();
                }


            } catch(Exception e){
                e.getMessage();
            }
        }
    }

    public void acceptMeetingNewTime(MeetingDetailPOJO item){
        ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> selectItem = new ArrayList<>();
        if (item != null){
            layout_accept_enable.setVisibility(View.VISIBLE);
            meeting_slot_id =  ""+item.getMeeting_slot_id();
            startTime =  item.getSlot_from_date();
            endTime = item.getSlot_to_date();
        }
    }

    private void getAcceptMeeting(PendingMeetingRequestPOJO.MeetingRequestList item, String meeting_code, boolean flag) {
        try {
            startTime = Utility.ConvertUserTimezoneToUTC(startTime);
            endTime  = Utility.ConvertUserTimezoneToUTC(endTime);

            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.getRequestorAction(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/requestor-action", loginPOJO.getActiveToken(),
                    meeting_code, loginPOJO.getRowcode(),flag,meeting_slot_id ,startTime, endTime);
            call.enqueue(new Callback<CommonPOJO>() {
                @Override
                public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonPOJO reasonPOJO = response.body();
                        progressHUD.dismiss();
                        try {
                            Log.d(TAG,""+reasonPOJO.getMessage());
                            if (reasonPOJO.getOK()) {
                                if (dialogDetails != null){
                                    dialogDetails.dismiss();
                                }
                                //   Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                if (flag){
                                    successDialogAccept();
                                }else {
                                    sentEmailToThriiveSupport(item);
                                }

                            } else {
                                Toast.makeText(getActivity(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.getMessage();
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(getActivity(), " " +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }

    public void sentEmailToThriiveSupport(PendingMeetingRequestPOJO.MeetingRequestList item){
        if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
            if (item.getGiver_email_id().equals("")){
                Toast.makeText(getActivity(), "Sorry email not found", Toast.LENGTH_SHORT).show();
            } else {
                HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                visitEvent.put("meeting_request_id", item.getSel_meeting().getMeeting_code());
                cleverTap.pushEvent(Utility.Clicked_Matched_Users_Email,visitEvent);
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ item.getGiver_email_id()});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                    final PackageManager pm = getActivity().getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                    ResolveInfo best = null;
                    for(final ResolveInfo info : matches)
                        if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                            best = info;
                    if (best != null)
                        emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                    startActivity(emailIntent);
                } catch (Exception e){
                    e.getMessage();
                }
            }

        } else {
            if (item.getRequestor_email_id().equals("")){
                Toast.makeText(getActivity(), "Sorry email not found", Toast.LENGTH_SHORT).show();

            } else {
                HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                visitEvent.put("meeting_request_id", item.getSel_meeting().getMeeting_code());
                cleverTap.pushEvent(Utility.Clicked_Matched_Users_Email,visitEvent);
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ item.getRequestor_email_id()});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                    final PackageManager pm = getActivity().getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                    ResolveInfo best = null;
                    for(final ResolveInfo info : matches)
                        if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                            best = info;
                    if (best != null)
                        emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                    startActivity(emailIntent);
                } catch (Exception e){
                    e.getMessage();
                }

            }
        }
    }

    public void successDialogAccept() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_meeting_confirmed, null);
        TextView label_close = view1.findViewById(R.id.label_close);

        //    tv_msg.setText("Session Added Successfully.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                getNewHome();
            }
        });
        dialogs.show();
    }

    public void dialogThriiveSupport(PendingMeetingRequestPOJO.MeetingRequestList item,String meeting_code, boolean flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_thriive_support, null);
        TextView label_close = view1.findViewById(R.id.label_close);
        ImageView img_02 = view1.findViewById(R.id.img_02);

        img_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAcceptMeeting(item,meeting_code,false);
            }
        });

        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        dialogs.show();
    }

    public ArrayList<MeetingDetailPOJO> reverseArrayList(ArrayList<MeetingDetailPOJO> alist) {
        ArrayList<MeetingDetailPOJO> revArrayList = new ArrayList<MeetingDetailPOJO>();
        for (int i = alist.size() - 1; i >= 0; i--) {
            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }
        return revArrayList;
    }

    public void callFragment(BottomSheetDialogFragment fragment) {

        fragment.show(getFragmentManager(),
                "MeetingRequestFragment");
    }
}