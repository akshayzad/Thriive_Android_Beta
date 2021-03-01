package com.thriive.app.fragments;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.adapters.ExperienceListAdapter;
import com.thriive.app.adapters.MeetingSelectTagAdapter;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MeetingRequestAcceptFragment extends BottomSheetDialogFragment {

    Unbinder unbinder;
    @BindView(R.id.rv_experience)
    RecyclerView rv_experience;
    @BindView(R.id.rv_expertise)
    RecyclerView rv_expertise;
    @BindView(R.id.img_close)
    ImageView img_close;
    @BindView(R.id.img_accept)
    ImageButton img_accept;
    @BindView(R.id.img_decline)
    ImageButton img_decline;
    @BindView(R.id.txt_reason)
    TextView txt_reason;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_experience)
    TextView txt_experience;
    @BindView(R.id.txt_profession)
    TextView txt_profession;

    @BindView(R.id.txt_details)
    TextView txt_details;

    @BindView(R.id.txt_country)
    TextView txt_country;

    @BindView(R.id.img_user)
    CircleImageView img_user;
    public static CommonMeetingListPOJO.MeetingListPOJO meetingListPOJO;
    String region_name = "", user_region = "";
    String meetingCode ="", startTime ="", endTime ="", cancelReason = "", selectedDate = "", personaName, meetingReason;


    public MeetingRequestAcceptFragment() {
        // Required empty public constructor
    }

    public static MeetingRequestAcceptFragment newInstance() {
        return new MeetingRequestAcceptFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.SheetDialog);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_meeting_request_accept, container, false);
        unbinder = ButterKnife.bind(this, view);
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
                //      behavior.setPeekHeight(0); // Remove this line to hide a dark background if you manually hide the dialog.
            }
        });

        init();
        return  view;
    }

    public void init(){
        if (meetingListPOJO != null){

            try {
                meetingCode = meetingListPOJO.getMeetingCode();
                startTime = meetingListPOJO.getPlanStartTime();
                endTime = meetingListPOJO.getPlanEndTime();
                meetingReason = meetingListPOJO.getMeetingReason();
                personaName = meetingListPOJO.getRequestorPersonaTags().get(0);
                region_name = meetingListPOJO.getRequestorCountryName();
                user_region = meetingListPOJO.getGiverCountryName();
            } catch (Exception e ){
                e.getMessage();
            }
            txt_details.setText(meetingListPOJO.getMeeting_req_text());
            //makeTextViewResizable(txt_details, 1, "See More", true);

            txt_profession.setText(""+meetingListPOJO.getRequestorSubTitle());

            if (!meetingListPOJO.getMeeting_l1_attrib_name().equals("")){
                txt_reason.setText("Meeting for "+meetingListPOJO.getMeetingReason() + " - " + meetingListPOJO.getMeeting_l1_attrib_name());
            }else {
                txt_reason.setText("Meeting for "+meetingListPOJO.getMeetingReason());
            }

            txt_country.setText(""+meetingListPOJO.getRequestorCountryName());
            try {
                txt_name.setText(Utility.getEncodedName(meetingListPOJO.getRequestorName()));
            } catch (Exception e){
                txt_name.setText(""+meetingListPOJO.getRequestorName());
            }
            if (meetingListPOJO.getRequestorExperienceTags() != null){
                txt_experience.setText("Total " +meetingListPOJO.getRequestorExperienceTags().get(0));
            }
            if (meetingListPOJO.getRequestorPicUrl().equals("")){
                Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.roboto_medium);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(getResources().getColor(R.color.darkGreyBlue))
                        .useFont(typeface)
                        .fontSize(60) /* size in px */
                        .bold()
                        .toUpperCase()
                        .width(140)  // width in px
                        .height(140) // height in px
                        .endConfig()
                        .buildRect(Utility.getInitialsName(meetingListPOJO.getRequestorName()) ,getResources().getColor(R.color.whiteTwo));

//                TextDrawable drawable = TextDrawable.builder().width(60)  // width in px
//                        .height(60)
//                        .buildRound(""+meetingListPOJO.getGiverName().charAt(0), R.color.darkGreyBlue);
                img_user.setImageDrawable(drawable);
            } else {
                img_user.setMinimumWidth(120);
                img_user.setMaxHeight(120);
                img_user.setMinimumHeight(120);
                img_user.setMaxWidth(120);
                Glide.with(this)
                        .load(meetingListPOJO.getRequestorPicUrl())
                        .into(img_user);
            }

            FlexboxLayoutManager manager = new FlexboxLayoutManager(getActivity());
//            manager.setFlexWrap(FlexWrap.WRAP);
//            manager.setJustifyContent(JustifyContent.CENTER);
            rv_experience.setLayoutManager(manager );
            ArrayList<String> array = new ArrayList<>();
            //array.addAll(meetingListPOJO.getRequestorExperienceTags());
            for (int i =0; i< meetingListPOJO.getRequestorDesignationTags().size(); i++) {
                if (i <= 1){
                    array.add(meetingListPOJO.getRequestorDesignationTags().get(i));
                }
            }
            // array.addAll(meetingListPOJO.getRequestorDesignationTags());
            rv_experience.setAdapter(new ExperienceListAdapter(getActivity(),array));

            FlexboxLayoutManager manager1 = new FlexboxLayoutManager(getActivity());
//            manager1.setFlexWrap(FlexWrap.WRAP);
//            manager1.setJustifyContent(JustifyContent.CENTER);
            rv_expertise.setLayoutManager(manager1 );
            ArrayList<String> array1 = new ArrayList<>();
            // array1.addAll(meetingListPOJO.getMeetingTag());
            array1.addAll(meetingListPOJO.getRequestorDomainTags());
            array1.addAll(meetingListPOJO.getRequestorExpertiseTags());

//            for (int i = 0; i< array1.size(); i++){
//                for (int j = 0; j < meetingListPOJO.getMeetingTag().size(); j++){
//                    if (array1.get(i).equals(meetingListPOJO.getMeetingTag().get(j))){
//                        array1.remove(i);
//                    }
//                }
//            }
//
//            ArrayList<String> combine_array = new ArrayList<>();
//            combine_array.addAll(meetingListPOJO.getMeetingTag());
//            combine_array.addAll(array1);
//
            ArrayList<String> combine_array = new ArrayList<>();
            for (int i = 0; i < array1.size(); i++){
                if (!array1.get(i).equals("")){
                    combine_array.add(array1.get(i));
                }
            }
            HashSet hs = new HashSet();
            hs.addAll(combine_array); // demoArrayList= name of arrayList from which u want to remove duplicates
            combine_array.clear();
            combine_array.addAll(hs);
            ArrayList<String> final_array = new ArrayList<>();
            for (int i =0; i< combine_array.size(); i++)
            {
                if (i <= 3){
                    final_array.add(combine_array.get(i));
                }

            }

            // rv_expertise.setLayoutManager(new FlexboxLayoutManager(NotificationListActivity.this) );
            rv_expertise.setAdapter(new MeetingSelectTagAdapter(getActivity(), final_array,
                    (ArrayList<String>) meetingListPOJO.getMeetingTag()));
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //   ratingDialog();
                }
            });
//            decline.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialogMeetingDetails.dismiss();
//                    getDeclineMeeting(meetingCode);
//                }
//            });
//            accept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //dialogs.dismiss();
//                    getMeetingSlote();
//                    //s successDialog();
//                }
//            });
        }
    }
}
