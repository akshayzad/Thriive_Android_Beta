package com.thriive.app.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.provider.CalendarContract;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.MeetingJoinActivity;
import com.thriive.app.R;

import com.thriive.app.adapters.DetailRescheduleSlotAdapter;
import com.thriive.app.adapters.ExperienceListAdapter;
import com.thriive.app.adapters.ExpertiseAdapter;
import com.thriive.app.adapters.MeetingSelectTagAdapter;
import com.thriive.app.adapters.MeetingTagAdapter;
import com.thriive.app.adapters.RescheduleTimeSlotAdapter;
import com.thriive.app.adapters.SelectedRescheduleTimeSlot;
import com.thriive.app.adapters.SlotListAdapter;
import com.thriive.app.adapters.SlotListFragmentAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonEntitySlotsPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonRequestTimeSlots;
import com.thriive.app.models.CommonStartMeetingPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.HomeDisplayPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.SwipeController;
import com.thriive.app.utilities.SwipeControllerActions;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thriive.app.utilities.Utility.checkInternet;

/**
 */
public class NextMeetingDetailsFragment extends BottomSheetDialogFragment {
    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_profession)
    TextView txt_profession;
    @BindView(R.id.img_user)
    CircleImageView img_user;
    Unbinder unbinder;
    @BindView(R.id.rv_experience)
    RecyclerView rv_experience;
    @BindView(R.id.img_bg)
    ImageView img_bg;
    @BindView(R.id.layout_data)
    LinearLayout layout_data;
    @BindView(R.id.txt_email)
    TextView txt_email;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_reason)
    TextView txt_reason;
    @BindView(R.id.btn_linkedin)
    Button btn_linkedin;
    @BindView(R.id.btn_email)
    Button btn_email;
    @BindView(R.id.txt_tag)
    TextView txt_tag;
    @BindView(R.id.label_experience)
    TextView label_experience;
    @BindView(R.id.txt_country)
    TextView txt_country;
    @BindView(R.id.txt_details)
    TextView txt_details;


    private CommonStartMeetingPOJO.MeetingDataPOJO meetingDataPOJO;
    private HomeDisplayPOJO.MeetingData meetingListPOJO;
    private SharedData sharedData;
    private KProgressHUD progressHUD;
//    private LoginPOJO.ReturnEntity loginPOJO;
    private LoginPOJO.ReturnEntity loginPOJO;
    private APIInterface apiInterface;
    private String cancelReason = "", usertype = "";

    private static  String TAG = NextMeetingDetailsFragment.class.getName();
    String startTime = "", endTime = "", selectedDate,currentDate;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_CALENDAR
    };
    private ArrayList<CommonEntitySlotsPOJO.EntitySlotList> entitySlotList = new ArrayList<>();
    private CleverTapAPI cleverTap;
    private  BottomSheetDialog dialogEditSlot;
    private AlertDialog dialogDetails ;
    TextView txt_year;
    TextView txt_month;
    TextView txt_day;
    RecyclerView rv_select_time_slots;
    RecyclerView rv_time_slots;
    LinearLayout layout_empty_view;
    TextView txt_toaster;
    LinearLayout layout_confirm;
    LinearLayout layout_confirm_disabled;
    int giverID = 0;
    int requestorId = 0;
    Calendar calendar = Calendar.getInstance();

    String txtYear="",txtMonth="",txtDay="";
    public NextMeetingDetailsFragment() {
        // Required empty public constructor
    }

    public static NextMeetingDetailsFragment newInstance() {
        return new NextMeetingDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.SheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_next_meeting_details, container,
                false);
        unbinder = ButterKnife.bind(this, view);
        sharedData = new SharedData(getActivity());
        apiInterface = APIClient.getApiInterface();
        loginPOJO = Utility.getLoginData(getActivity());
        meetingListPOJO = Utility.getNextMeetingDetailsData(getContext());
        cleverTap = CleverTapAPI.getDefaultInstance(getActivity().getApplicationContext());

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
            }
        });

        setData();

        img_bg.setMaxHeight(layout_data.getHeight());

        return view;

    }

    private void setData() {
        try {
            //  txt_tag.setText(""+meetingListPOJO.getMeetingLabel());
            if (meetingListPOJO.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))){
                usertype = "requestor";
                txt_name.setText(meetingListPOJO.getGiverName());
                if (meetingListPOJO.getGiverPicUrl().equals("")){
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
                            .buildRect(Utility.getInitialsName(meetingListPOJO.getGiverName()) , getResources().getColor(R.color.whiteTwo));
                    img_user.setImageDrawable(drawable);
                } else {
                    img_user.setMinimumWidth(120);
                    img_user.setMaxHeight(120);
                    img_user.setMinimumHeight(120);
                    img_user.setMaxWidth(120);
                    Glide.with(getActivity())
                            .load(meetingListPOJO.getGiverPicUrl())
                            .into(img_user);
                }

                txt_profession.setText(""+meetingListPOJO.getGiverSubTitle());
                txt_country.setText(""+meetingListPOJO.getGiverCountryName());

                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(meetingListPOJO.getGiverExpertiseTags());
                arrayList.addAll(meetingListPOJO.getMeetingTag());
                FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getContext());
                rv_tags.setLayoutManager(gridLayout );

                ArrayList<String> array1 = new ArrayList<>();
                // array1.addAll(meetingListPOJO.getMeetingTag());
                array1.addAll(meetingListPOJO.getGiverDomainTags());
                array1.addAll(meetingListPOJO.getGiverSubDomainTags());
                array1.addAll(meetingListPOJO.getGiverExpertiseTags());

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
                for (int i =0; i< combine_array.size(); i++) {
                    if (i < 4){
                        final_array.add(combine_array.get(i));
                    }
                }

                ArrayList<String> array2 = new ArrayList<>();
                if (meetingListPOJO.getMeetingExpertise()!= null) {
                    array2.add(meetingListPOJO.getMeetingExpertise());
                }
                if (meetingListPOJO.getMeetingDomain()!= null) {
                    array2.add(meetingListPOJO.getMeetingDomain());
                }
                if (meetingListPOJO.getMeetingSubDomain()!= null) {
                    array2.add(meetingListPOJO.getMeetingSubDomain());
                }

                ArrayList<String> meeting_tag_array = new ArrayList<>();
                for (int i = 0; i < array2.size(); i++){
                    if (!array2.get(i).equals("")){
                        meeting_tag_array.add(array2.get(i));
                    }
                }

                rv_tags.setAdapter(new MeetingSelectTagAdapter(getContext(), final_array, (ArrayList<String>) meeting_tag_array));
//                rv_tags.setAdapter(new MeetingSelectTagAdapter(getContext(), final_array, (ArrayList<String>) meetingListPOJO.getMeetingTag()));

                if (meetingListPOJO.getGiverExpertiseTags() != null){
                }

                txt_email.setText(meetingListPOJO.getGiverEmailId());
                ArrayList<String> arrayList1 = new ArrayList<>();

                if (meetingListPOJO.getGiverExperienceTags() != null){
                    label_experience.setText("Total " +meetingListPOJO.getGiverExperienceTags().get(0));
                }

                arrayList1.addAll(meetingListPOJO.getGiverDesignationTags());
                rv_experience.setLayoutManager(new FlexboxLayoutManager(getContext()) );
                rv_experience.setAdapter(new ExperienceListAdapter(getContext(), arrayList1));

            } else {
                usertype = "giver";
                txt_name.setText(meetingListPOJO.getRequestorName());
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

                    img_user.setImageDrawable(drawable);
                } else {
                    img_user.setMinimumWidth(120);
                    img_user.setMaxHeight(120);
                    img_user.setMinimumHeight(120);
                    img_user.setMaxWidth(120);
                    Glide.with(getActivity())
                            .load(meetingListPOJO.getRequestorPicUrl())
                            .into(img_user);
                }

                txt_profession.setText(""+meetingListPOJO.getRequestorSubTitle());
                txt_country.setText(""+meetingListPOJO.getRequestorCountryName());


                ArrayList<String> array1 = new ArrayList<>();
                // array1.addAll(meetingListPOJO.getMeetingTag());
                array1.addAll(meetingListPOJO.getRequestorDomainTags());
                array1.addAll(meetingListPOJO.getRequestorSubDomainTags());
                array1.addAll(meetingListPOJO.getRequestorExpertiseTags());

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
                for (int i =0; i< combine_array.size(); i++) {
                    if (i < 4){
                        final_array.add(combine_array.get(i));
                    }
                }

                ArrayList<String> array2 = new ArrayList<>();
                if (meetingListPOJO.getMeetingExpertise()!= null) {
                    array2.add(meetingListPOJO.getMeetingExpertise());
                }
                if (meetingListPOJO.getMeetingDomain()!= null) {
                    array2.add(meetingListPOJO.getMeetingDomain());
                }
                if (meetingListPOJO.getMeetingSubDomain()!= null) {
                    array2.add(meetingListPOJO.getMeetingSubDomain());
                }

                ArrayList<String> meeting_tag_array = new ArrayList<>();
                for (int i = 0; i < array2.size(); i++){
                    if (!array2.get(i).equals("")){
                        meeting_tag_array.add(array2.get(i));
                    }
                }

                FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getContext());
                rv_tags.setLayoutManager(gridLayout);
                rv_tags.setAdapter(new MeetingSelectTagAdapter(getContext(), final_array, (ArrayList<String>) meeting_tag_array));
//                rv_tags.setAdapter(new MeetingSelectTagAdapter(getContext(), final_array, (ArrayList<String>) meetingListPOJO.getMeetingTag()));

                txt_email.setText(meetingListPOJO.getRequestorEmailId());

                ArrayList<String> arrayList1 = new ArrayList<>();

                if (meetingListPOJO.getGiverExperienceTags() != null){
                    label_experience.setText("Total "+meetingListPOJO.getRequestorExperienceTags().get(0));
                }

                arrayList1.addAll(meetingListPOJO.getRequestorDesignationTags());

                rv_experience.setAdapter(new ExperienceListAdapter(getContext(), arrayList1));
            }

            if (!meetingListPOJO.getMeetingL1AttribName().equals("")){
                txt_reason.setText("Meeting for "+meetingListPOJO.getMeetingReason() + " (" + meetingListPOJO.getMeetingL1AttribName() +")");
            }else {
                txt_reason.setText("Meeting for "+meetingListPOJO.getMeetingReason());
            }

            txt_details.setText(meetingListPOJO.getMeetingReqText());

            //String str = Utility.getMeetingDateWithTime(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime())) + Utility.getMeetingTime(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime()),
            //       Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanEndTime()));

            txt_date.setText(Utility.getMeetingDateWithTime(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime())) +", " + Utility.getMeetingTime(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime()),
                    Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanEndTime()))) ;
            txt_time.setText(Utility.getMeetingTime(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime()),
                    Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanEndTime())));
        } catch (Exception e){
            e.getMessage();
        }
    }

    @OnClick({R.id.txt_cancel, R.id.join_meeting, R.id.img_close, R.id.layout_avail, R.id.btn_email,
            R.id.btn_linkedin, R.id.txt_add_calender})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_cancel:
                meetingCancel();
                break;

            case R.id.join_meeting:
                if (Utility.getCallJoin(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime()))) {
                    if (Utility.getCallEdJoinJoin(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanEndTime()))){
                        final Toast toast = Toast.makeText(getContext(), "Meeting session has been ended",Toast.LENGTH_SHORT);
                        toast.show();
                        new CountDownTimer(2000, 1000)
                        {
                            public void onTick(long millisUntilFinished) {toast.show();}
                            public void onFinish() {toast.cancel();}
                        }.start();
                    } else {
                        startMeeting();
                    }

                } else {

                    final Toast toast = Toast.makeText(getContext(), "Meeting is yet to start",Toast.LENGTH_SHORT);
                    toast.show();
                    new CountDownTimer(2000, 1000)
                    {
                        public void onTick(long millisUntilFinished) {toast.show();}
                        public void onFinish() {toast.cancel();}
                    }.start();
                    //
                    // Toast.makeText(getActivity(), "Meeting is yet to start", Toast.LENGTH_LONG).show();
                    //Toast.makeText(getActivity(), ""+Utility.getCallJoin(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime())), Toast.LENGTH_SHORT).show();
                }
                //dismiss();

                break;

            case R.id.img_close:

                //((MeetingsFragment)getParentFragment()).onResume();
                dismiss();

                break;

            case R.id.layout_avail:

                alertDialogForRechedule();

                break;

            case R.id.btn_email:

                getToEmail();

                break;

            case R.id.btn_linkedin:
                getToLinkedin();

                break;


            case R.id.txt_add_calender:

                getAddCalenderEvent();
                break;

        }
    }


    public void getAddCalenderEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.alert_dialog_yesno, null);
        TextView txt_add = view1.findViewById(R.id.txt_add);
        TextView txt_cancel = view1.findViewById(R.id.txt_cancel);
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                getPermission();
            }
        });
        dialogs.show();
    }

    public boolean getPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR},
                        111);
            } else {
                getAddCalender();
                //return true;
            }
        } else {
            getAddCalender();
            // return true;
        }
        return false;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 111:
                getPermission();
                break;
        }
    }
    private void getAddCalender() {


        HashMap<String, Object> visitEvent = new HashMap<String, Object>();
        visitEvent.put("meeting_request_id", meetingListPOJO.getMeetingCode());
        cleverTap.pushEvent(Utility.Clicked_Add_to_Calendar,visitEvent);

        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        long lnsTime = 0, lneTime = 0;
        Date dateObject;
        try {
            dateObject = in_format.parse(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime()));
            lnsTime = dateObject.getTime();
            Log.e("null", Long.toString(lnsTime));

            dateObject = in_format.parse(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanEndTime()));
            lneTime = dateObject.getTime();
            Log.e("null", Long.toString(lneTime));
        } catch ( Exception e){
            e.getMessage();
        }
        String  title = "" ;
        if (meetingListPOJO.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
            title = "Thriive Meet with "+ meetingListPOJO.getGiverName() ;
        } else {
            title = "Thriive Meet with "+ meetingListPOJO.getRequestorName() ;
        }

        try {
            String[] proj =
                    new String[]{
                            CalendarContract.Instances._ID,
                            CalendarContract.Instances.BEGIN,
                            CalendarContract.Instances.END,
                            CalendarContract.Instances.EVENT_ID};
            Cursor cursor =
                    CalendarContract.Instances.query(getActivity().getContentResolver(),
                            proj, lnsTime, lneTime, title);
            if (cursor.getCount() > 0) {
                successDialog();
                // deal with conflict
                // Toast.makeText(getActivity(), "Already exist", Toast.LENGTH_SHORT).show();
            }
            else {
//                TimeZone timeZone = TimeZone.getDefault();
//
//                ContentResolver cr = getActivity().getContentResolver();
//                ContentValues values = new ContentValues();
//                values.put(CalendarContract.Events.DTSTART, lnsTime);
//                values.put(CalendarContract.Events.DTEND, lneTime);
//                values.put(CalendarContract.Events.TITLE, title);
//                values.put(CalendarContract.Events.DESCRIPTION, "Meeting for " + meetingListPOJO.getMeetingReason());
//                values.put(CalendarContract.Events.CALENDAR_ID, 1);
//                values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
//                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
//                //  successDialog();
////            Uri eventUri = getActivity().getApplicationContext()
////                    .getContentResolver()
////                    .insert(uri, values);
//                //  successDialog();
//// get the event ID that is the last element in the Uri
//                long eventID = Long.parseLong(uri.getLastPathSegment());
//                Log.d(TAG, "eventID " + eventID);
//                if (eventID != -1) {
//                    successDialog();
//                }


                try {
                    if (Build.VERSION.SDK_INT >= 14) {
                        Intent intent = new Intent(Intent.ACTION_INSERT)
                                .setData(CalendarContract.Events.CONTENT_URI)
                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, lnsTime)
                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, lneTime)
                                .putExtra(CalendarContract.Events.TITLE, title)
                                .putExtra(CalendarContract.Events.DESCRIPTION, "Meeting for " + meetingListPOJO.getMeetingReason())
                                //   .putExtra(Events.EVENT_LOCATION, "The gym")
                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                        //  .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
                        getContext().startActivity(intent);
                    } else {
                        //    Calendar cal = Calendar.getInstance();
                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra("beginTime", lnsTime);
                        intent.putExtra("allDay", true);
                        intent.putExtra("rrule", "FREQ=YEARLY");
                        intent.putExtra("endTime", lneTime);
                        intent.putExtra("title", title);
                        getContext().startActivity(intent);
                    }
                } catch (Exception e){
                    e.getMessage();
                }

            }
        } catch (Exception e){
            Log.d(TAG, " "+  e.getLocalizedMessage());
            e.getMessage();
        }



    }

    public void successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_request_meeting_success, null);
        TextView label_close = view1.findViewById(R.id.label_close);
        TextView label_title = view1.findViewById(R.id.label_title);

        label_title.setText("This meeting is already added to your Calender.");
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

    private void getToLinkedin() {
        if (meetingListPOJO.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
            if (meetingListPOJO.getGiverLinkedinUrl().equals("")){
                Toast.makeText(getActivity(), "Sorry linkedin not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                    visitEvent.put("meeting_request_id", meetingListPOJO.getMeetingCode());
                    cleverTap.pushEvent(Utility.Clicked_Matched_Users_LinkedIn,visitEvent);

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(meetingListPOJO.getGiverLinkedinUrl()));
                    intent.setPackage("com.linkedin.android");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(meetingListPOJO.getGiverLinkedinUrl()));
                        getActivity().startActivity(intent);
                    } catch (Exception e1) {
                    }
                }

            }

        } else {
            if (meetingListPOJO.getRequestorLinkedinUrl().equals("")){
                Toast.makeText(getActivity(), "Sorry linkedin not found", Toast.LENGTH_SHORT).show();

            } else {
                HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                visitEvent.put("meeting_request_id", meetingListPOJO.getMeetingCode());
                cleverTap.pushEvent(Utility.Clicked_Matched_Users_LinkedIn,visitEvent);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(meetingListPOJO.getRequestorLinkedinUrl()));
                    intent.setPackage("com.linkedin.android");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(meetingListPOJO.getRequestorLinkedinUrl()));
                        getActivity().startActivity(intent);
                    } catch (Exception e1) {
                    }
                }
            }
        }
    }

    private void getToEmail() {
        if (meetingListPOJO.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
            if (meetingListPOJO.getGiverEmailId().equals("")){
                Toast.makeText(getActivity(), "Sorry email not found", Toast.LENGTH_SHORT).show();

            } else {
                HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                visitEvent.put("meeting_request_id", meetingListPOJO.getMeetingCode());
                cleverTap.pushEvent(Utility.Clicked_Matched_Users_Email,visitEvent);

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ meetingListPOJO.getGiverEmailId()});
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

                getActivity().startActivity(emailIntent);

//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("*/*");
//                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{ meetingListPOJO.getGiverEmailId()});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "");
//                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                    getActivity().startActivity(intent);
//                }
            }

        } else {
            if (meetingListPOJO.getRequestorEmailId().equals("")){
                Toast.makeText(getActivity(), "Sorry email not found", Toast.LENGTH_SHORT).show();

            } else {
                //add Matched_Users_Email
                HashMap<String, Object> visitEvent = new HashMap<String, Object>();
                visitEvent.put("meeting_request_id", meetingListPOJO.getMeetingCode());
                cleverTap.pushEvent(Utility.Clicked_Matched_Users_Email,visitEvent);

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ meetingListPOJO.getRequestorEmailId()});
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

                getActivity().startActivity(emailIntent);

//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("*/*");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{meetingListPOJO.getRequestorEmailId()});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "");
//                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                    getActivity().startActivity(intent);
//                }
            }
        }
    }


    public void alertDialogForRechedule() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_reschedule_confirm, null);
        TextView label_close = view1.findViewById(R.id.label_close);
        Button btn_reschedule = view1.findViewById(R.id.btn_reschedule);

        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        btn_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                rescheduleMeeting();
            }
        });
        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        dialogs.show();
    }

    public void rescheduleMeeting() {

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            final View view1 = layoutInflater.inflate(R.layout.dialog_rescedule_layout, null);
            ImageView img_close = view1.findViewById(R.id.img_close);
            ImageView img_info = view1.findViewById(R.id.img_info);
            layout_empty_view = view1.findViewById(R.id.layout_empty_view);
            layout_empty_view.setVisibility(View.VISIBLE);
            rv_time_slots = view1.findViewById(R.id.rv_time_slots);
            rv_time_slots.setVisibility(View.GONE);
            ImageView img_backward = view1.findViewById(R.id.img_backward);
            ImageView img_forward = view1.findViewById(R.id.img_forward);
            LinearLayout layout_circle = view1.findViewById(R.id.layout_circle);
            layout_confirm = view1.findViewById(R.id.layout_confirm);
            layout_confirm_disabled = view1.findViewById(R.id.layout_confirm_disabled);

            layout_confirm.setVisibility(View.GONE);
            layout_confirm_disabled.setVisibility(View.VISIBLE);

            txt_toaster = view1.findViewById(R.id.txt_toaster);

            txt_year = view1.findViewById(R.id.txt_year);
            txt_month = view1.findViewById(R.id.txt_month);
            txt_day = view1.findViewById(R.id.txt_day);
            rv_select_time_slots = view1.findViewById(R.id.rv_select_time_slots);

            setDefaultDate();

            builder.setView(view1);
            dialogDetails = builder.create();
            dialogDetails.setCancelable(false);

            img_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCustomToast(true);
                }
            });
            layout_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, Object> meeting_reschedule = new HashMap<String, Object>();
                    meeting_reschedule.put("meeting_request_id", meetingListPOJO.getMeetingCode());
                    meeting_reschedule.put("meeting_start_datetime", startTime);
                    meeting_reschedule.put("usertype", usertype);
                    cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Reschedule, meeting_reschedule);
                    getResheduledMeeting();

                }
            });

            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDetails.dismiss();
                }
            });

            img_backward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    previousDate();
                }
            });

            img_forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextDate();
                }
            });

            layout_circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    meetingEditDate();
                }
            });

            dialogDetails.show();
        } catch(Exception e){
            e.getMessage();
        }

    }

    private void setDefaultDate(){
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = df.format(calendar.getTime());
        currentDate = df.format(calendar.getTime());
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy", Locale.getDefault());
        txtYear = dfYear.format(calendar.getTime());

        SimpleDateFormat dfMM = new SimpleDateFormat("dd MMM", Locale.getDefault());
        txtMonth = dfMM.format(calendar.getTime());

        SimpleDateFormat dfDay = new SimpleDateFormat("EEE", Locale.getDefault());
        txtDay = dfDay.format(calendar.getTime());

        txt_year.setText(txtYear);
        txt_month.setText(txtMonth);
        txt_day.setText(txtDay);

        String utc_date = Utility.convertLocaleToUtc(selectedDate);
        if (utc_date != null && utc_date.length() > 0){
            get_meeting_time_slots(utc_date);
        }
    }

    public void meetingEditDate()  {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_select_slot_date, null);

        ImageView img_close = view1.findViewById(R.id.img_close);
        Button btn_done = view1.findViewById(R.id.btn_done);
        CalendarView calender_view = view1.findViewById(R.id.calender);
        //calender_view.setMinDate(new Date().getTime());
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,+1);
        calender_view.setMinDate(calendar.getTime().getTime());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(selectedDate);
            calender_view.setDate(date.getTime(),true,true);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("NEW_DATE", selectedDate);

        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);

        calender_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                month = month + 1;
                // output to log cat **not sure how to format year to two places here**
                String newDate = year+"-"+month+"-"+date;
                Log.d("NEW_DATE", newDate);
                selectedDate = newDate;
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = format.parse(selectedDate);
                    calendar.setTime(date1);
                    SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
                    txtYear = dfYear.format(date1);

                    SimpleDateFormat dfMonth = new SimpleDateFormat("dd MMM");
                    txtMonth = dfMonth.format(date1.getTime());

                    SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
                    txtDay = dfDay.format(date1.getTime());

                    txt_year.setText(""+txtYear);
                    txt_month.setText(txtMonth);
                    txt_day.setText(txtDay);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                String utc_date = Utility.convertLocaleToUtc(selectedDate);
                if (utc_date != null && utc_date.length() > 0){
                    get_meeting_time_slots(utc_date);
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
            }
        });
        dialogs.show();

    }

    private void nextDate(){

        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = df.format(calendar.getTime());
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = format.parse(selectedDate);

            SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
            txtYear = dfYear.format(date1);

            SimpleDateFormat dfMonth = new SimpleDateFormat("dd MMM");
            txtMonth = dfMonth.format(date1.getTime());

            SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
            txtDay = dfDay.format(date1.getTime());

            txt_year.setText(""+txtYear);
            txt_month.setText(txtMonth);
            txt_day.setText(txtDay);

            String utc_date = Utility.convertLocaleToUtc(selectedDate);
            if (utc_date != null && utc_date.length() > 0){
                get_meeting_time_slots(utc_date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

//    private void previousDate(){
//        calendar.add(Calendar.DATE, -1);
//        //calendar.se.setMinDate(System.currentTimeMillis() - 1000);
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        selectedDate = df.format(calendar.getTime());
//
//        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
//        txtYear = dfYear.format(calendar.getTime());
//
//        SimpleDateFormat dfMM = new SimpleDateFormat("dd MMM");
//        txtMonth = dfMM.format(calendar.getTime());
//
//        SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
//        txtDay = dfDay.format(calendar.getTime());
//
//        txt_year.setText(txtYear);
//        txt_month.setText(txtMonth);
//        txt_day.setText(txtDay);
//
//        String utc_date = Utility.convertLocaleToUtc(selectedDate);
//        if (utc_date != null && utc_date.length() > 0){
//            get_meeting_time_slots(utc_date);
//        }
//
//    }

    private void previousDate(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date current_date = format.parse(currentDate);
            Date selected_date = format.parse(selectedDate);
            if (selected_date.before(current_date) || selected_date.equals(current_date)){
                Toast.makeText(getActivity(), "You can't select past date.", Toast.LENGTH_SHORT).show();
            }else {
                calendar.add(Calendar.DATE, -1);
                //calendar.se.setMinDate(System.currentTimeMillis() - 1000);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate = df.format(calendar.getTime());

                SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
                String year = dfYear.format(calendar.getTime());

                SimpleDateFormat dfMM = new SimpleDateFormat("dd MMM");
                String month = dfMM.format(calendar.getTime());

                SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
                String day = dfDay.format(calendar.getTime());

                txt_year.setText(year);
                txt_month.setText(month);
                txt_day.setText(day);

                String utc_date = Utility.convertLocaleToUtc(selectedDate);
                if (utc_date != null && utc_date.length() > 0){

                    get_meeting_time_slots(utc_date);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void get_meeting_time_slots(String utc_date) {
        try {
            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonRequestTimeSlots> call = apiInterface.getTimeSlotsForGiver(sharedData.getStringData(SharedData.API_URL) +
                    "api/MRCalls/get-time-slots-for-reschedule", loginPOJO.getActiveToken(), meetingListPOJO.getRequestorId(), meetingListPOJO.getGiverId(), utc_date);
            call.enqueue(new Callback<CommonRequestTimeSlots>() {
                @Override
                public void onResponse(Call<CommonRequestTimeSlots> call, Response<CommonRequestTimeSlots> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonRequestTimeSlots pojo = response.body();
                        progressHUD.dismiss();
                        if (pojo != null){
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo.getOK()) {
                                if (pojo.getSlotsListPOJOS().size() > 0 ){
                                    rv_select_time_slots.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                                    DetailRescheduleSlotAdapter meetingRequestTimeSlotAdapter = new DetailRescheduleSlotAdapter(getActivity(),NextMeetingDetailsFragment.this,
                                            (ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>) pojo.getSlotsListPOJOS(),selectedDate);
                                    rv_select_time_slots.setAdapter(meetingRequestTimeSlotAdapter);
                                    meetingRequestTimeSlotAdapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(getActivity(), ""+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        progressHUD.dismiss();
                        Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<CommonRequestTimeSlots> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }

    public void showCustomToast(boolean flag) {
        try {
            if (flag){
                txt_toaster.setVisibility(View.VISIBLE);
                TimeZone timeZone = TimeZone.getDefault();
                Log.d(TAG, "time zone "+ timeZone.getID());
                String  time_stamp = "Limit of 1 time per day. Times are based on the region you’ve selected. Time is displayed in "+timeZone.getDisplayName();
                txt_toaster.setText(time_stamp);
            }else{
                txt_toaster.setVisibility(View.VISIBLE);
                String  time_stamp = "This time is unavailable because it has already been allocated for a meeting.";
                txt_toaster.setText(time_stamp);
            }


        } catch(Exception e){
            e.getMessage();
        }

        txt_toaster.postDelayed(new Runnable() {
            public void run() {
                txt_toaster.setVisibility(View.GONE);
            }
        }, 2000);
    }

    public void setSelectedTimeSlot(CommonRequestTimeSlots.EntitySlotsListPOJO slot,String time){
        if (slot != null){
            layout_empty_view.setVisibility(View.GONE);
            rv_time_slots.setVisibility(View.VISIBLE);
            layout_confirm.setVisibility(View.VISIBLE);
            layout_confirm_disabled.setVisibility(View.GONE);
            startTime = slot.getSlot_from_date();
            ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> arrayList = new ArrayList<>();
            arrayList.add(slot);
//            rv_time_slots.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            rv_time_slots.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            SelectedRescheduleTimeSlot meetingRequestTimeSlotAdapter = new SelectedRescheduleTimeSlot(getActivity(),
                    (ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>) arrayList);
            rv_time_slots.setAdapter(meetingRequestTimeSlotAdapter);
            meetingRequestTimeSlotAdapter.notifyDataSetChanged();
        }
    }




    public void getMeetingSlot() {
        //  meetingCode = ""+m;
        try {
            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonEntitySlotsPOJO> call = apiInterface.getEntitySlots(sharedData.getStringData(SharedData.API_URL) +
                    "api/Entity/get-entity-slots", loginPOJO.getActiveToken(), loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonEntitySlotsPOJO>() {
                @SuppressLint("NewApi")
                @Override
                public void onResponse(Call<CommonEntitySlotsPOJO> call, Response<CommonEntitySlotsPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonEntitySlotsPOJO reasonPOJO = response.body();
                        progressHUD.dismiss();
                        try {
                            Log.d(TAG,""+reasonPOJO.getMessage());
                            if (reasonPOJO.getOK()) {
                                //.makeText(getApplicationContext(), "Success "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();


                                entitySlotList = (ArrayList<CommonEntitySlotsPOJO.EntitySlotList>) reasonPOJO.getEntitySlotList();
                                // meetingAvailability();
                                meetingEditSlot(entitySlotList);

                            } else {
                                Toast.makeText(getContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.getMessage();
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonEntitySlotsPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(getContext(), " "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }

    }

    public void meetingEditSlot(List<CommonEntitySlotsPOJO.EntitySlotList> entitySlotList) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_availability, null);
        dialogEditSlot = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
        Button btn_confirm = dialogView.findViewById(R.id.btn_newSlot);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        RecyclerView rv_slots = dialogView.findViewById(R.id.rv_slots);
        ArrayList<CommonEntitySlotsPOJO.EntitySlotList> arrayList = new ArrayList<>();

        if (entitySlotList.size() > 3){
            for (int i = 0; i <= 2; i++) {
                arrayList.add(entitySlotList.get(i));
            }
        } else {
            arrayList.addAll(entitySlotList);
        }

        SlotListFragmentAdapter adapter  = new SlotListFragmentAdapter(getActivity(), NextMeetingDetailsFragment.this,
                (ArrayList<CommonEntitySlotsPOJO.EntitySlotList>) entitySlotList, "MEETINGDETAILS");
        rv_slots.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        rv_slots.setAdapter(adapter);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditSlot.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetingEditDate();
            }
        });
        dialogEditSlot.setContentView(dialogView);
        dialogEditSlot.show();
    }

    public void meetingConfirmation(String s_time, String e_time){
        startTime = s_time;
        endTime = e_time;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_meeting_confirmation, null);

        Button accept = view1.findViewById(R.id.btn_yes);
        Button decline = view1.findViewById(R.id.btn_no);
        TextView txt_title = view1.findViewById(R.id.txt_title);
        TextView txt_subTitle = view1.findViewById(R.id.txt_subTitle);
        TextView txt_date = view1.findViewById(R.id.txt_date);
        TextView txt_time = view1.findViewById(R.id.txt_time);
        TextView txt_country = view1.findViewById(R.id.txt_country);


        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);

        txt_title.setText(Html.fromHtml("You’re rescheduling a meeting with"));
        if (meetingListPOJO.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))){
            txt_subTitle.setText(Html.fromHtml( meetingListPOJO.getGiverPersonaTags().get(0)+ " for " +
                    meetingListPOJO.getMeetingReason() + ""));
            txt_country.setText("from " + meetingListPOJO.getGiverCountryName());

            txt_time.setText(Utility.getMeetingTime(startTime, endTime) + " (" + meetingListPOJO.getRequestorCountryName() + ")");
        } else {
            txt_subTitle.setText(Html.fromHtml( meetingListPOJO.getRequestorPersonaTags().get(0)+ " for " +
                    meetingListPOJO.getMeetingReason() + ""));
            txt_country.setText("from " + meetingListPOJO.getRequestorCountryName());

            txt_time.setText(Utility.getMeetingTime(startTime, endTime) + " (" + meetingListPOJO.getGiverCountryName() + " time)");
        }
        txt_date.setText(Utility.getMeetingDateWithTime(startTime));
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
                startTime = s_time;
                endTime = e_time;
                HashMap<String, Object> meeting_reschedule = new HashMap<String, Object>();
                meeting_reschedule.put("meeting_request_id", meetingListPOJO.getMeetingCode());
                meeting_reschedule.put("meeting_start_datetime", startTime);
                meeting_reschedule.put("usertype", usertype);
                cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Reschedule,meeting_reschedule);
                //getResheduledMeeting();
            }
        });
        dialogs.show();
    }

//    public void meetingEditDate() {
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_date, null);
//        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
//        Button btn_next = dialogView.findViewById(R.id.btn_next);
//        ImageView img_close = dialogView.findViewById(R.id.img_close);
//        CalendarView calender_view = dialogView.findViewById(R.id.calender);
//        calender_view.setMinDate(new Date().getTime());
//        selectedDate = DateFormat.format("yyyy-MM-dd", calender_view.getDate()).toString();
//        Log.d("NEW_DATE", selectedDate);
//        calender_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView arg0, int year, int month,
//                                            int date) {
//                month = month + 1;
//                // output to log cat **not sure how to format year to two places here**
//                String newDate = year+"-"+month+"-"+date;
//                Log.d("NEW_DATE", newDate);
//              //  Toast.makeText(getContext(),date+ "/"+month+"/"+year + "  "+arg0.getDate(),Toast.LENGTH_LONG).show();
//                selectedDate = newDate;
//            }
//        });
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        btn_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                meetingEditTime();
//            }
//        });
//        dialog.setContentView(dialogView);
//        dialog.show();
//
//
//    }

    public void meetingEditTime() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_time, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);

        Button btn_confirm = dialogView.findViewById(R.id.btn_confirm);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        TimePicker time_picker = dialogView.findViewById(R.id.time_picker);
        int hour = time_picker.getCurrentHour();
        int min = time_picker.getCurrentMinute();
        startTime = Utility.convertDate(selectedDate + " " + new StringBuilder().append(hour).append(":").append(min)
                .append(":").append("00"));
        Log.d(TAG, startTime);
        endTime = Utility.getOneHour(startTime) ;
        Log.d(TAG, endTime);
        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                int hour = time_picker.getCurrentHour();
                int min = time_picker.getCurrentMinute();


                startTime = Utility.convertDate(selectedDate + " " +new StringBuilder().append(hour).append(":").append(min)
                        .append(":").append("00"));
                Log.d(TAG, startTime);

                endTime = Utility.getOneHour(startTime) ;
                Log.d(TAG, endTime);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.getCheckSlotTime(startTime)){
                    Toast.makeText(getContext(), "Please choose current or future time.", Toast.LENGTH_LONG).show();
                } else {
                    //getResheduledMeeting();
                    dialog.dismiss();

                    meetingConfirmation(startTime, endTime);

                }
//                if(Utility.getCallJoin(startTime)){
//                    Toast.makeText(getContext(), "Please choose current or future time", Toast.LENGTH_LONG).show();
//                } else {
//                    getResheduledMeeting();
//                    dialog.dismiss();
//
//                }


            }
        });
        dialog.setContentView(dialogView);
        dialog.show();

    }


    private void getResheduledMeeting() {
        try {
            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.getRescheduleMeeting(sharedData.getStringData(SharedData.API_URL)
                            + "api/Meeting/reschedule-meeting",loginPOJO.getActiveToken(),
                    meetingListPOJO.getMeetingCode(), loginPOJO.getRowcode(), startTime,endTime);
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
                                // Toast.makeText(getContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_CANCEL));
                                dismiss();
                                successDialogForReshedule();
                                //  successDialog();
                            } else {
                                Toast.makeText(getContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.getMessage();
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    //  Toast.makeText(NotificationListActivity.this, "Getting Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e ){
            e.getMessage();
        }
    }

    private void startMeeting() {
        NetworkInfo networkInfo = checkInternet(getActivity());
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            try {
                progressHUD = KProgressHUD.create(getActivity())
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(false)
                        .show();
                Call<CommonStartMeetingPOJO> call = apiInterface.getMeetingStart(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/meeting-start",loginPOJO.getActiveToken(),
                        meetingListPOJO.getMeetingId(), true, loginPOJO.getRowcode());
                call.enqueue(new Callback<CommonStartMeetingPOJO>() {
                    @Override
                    public void onResponse(Call<CommonStartMeetingPOJO> call, Response<CommonStartMeetingPOJO> response) {
                        if(response.isSuccessful()) {
                            Log.d("TAG", response.toString());
                            CommonStartMeetingPOJO reasonPOJO = response.body();
                            progressHUD.dismiss();
                            try {
                                Log.d(TAG,""+reasonPOJO.getMessage());
                                // Log.d(TAG,""+reasonPOJO.getMrParams().getReasonName());
                                if (reasonPOJO.getOK()) {
                                    meetingDataPOJO = reasonPOJO.getMeetingData();
                                    sharedData.addStringData(SharedData.MEETING_TOKEN, meetingDataPOJO.getMeetingToken());
                                    if (sharedData.getIntData(SharedData.USER_ID) == reasonPOJO.getMeetingData().getGiverId()){
                                        sharedData.addStringData(SharedData.MEETING_PARSON_NAME, reasonPOJO.getMeetingData().getRequestorName());
                                    }else {
                                        sharedData.addStringData(SharedData.MEETING_PARSON_NAME, reasonPOJO.getMeetingData().getGiverName());
                                    }
                                    callMeeting();

                                    //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                    dismiss();
                                    EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_CANCEL));
                                }
                            } catch (Exception e){
                                e.getMessage();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<CommonStartMeetingPOJO> call, Throwable t) {
                        progressHUD.dismiss();
                        //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e){
                e.getMessage();
            }
        } else {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void callMeeting(){
        HashMap<String, Object> meeting_join = new HashMap<String, Object>();
        meeting_join.put("meeting_request_id", meetingListPOJO.getMeetingCode());
        meeting_join.put("usertype", usertype);
        cleverTap.pushEvent(Utility.User_Joins_Meeting,meeting_join);

        Intent intent = new Intent(getActivity(), MeetingJoinActivity.class);
        intent.putExtra("meeting_id",""+ meetingDataPOJO.getMeetingId());
        intent.putExtra("meeting_channel", meetingDataPOJO.getMeetingChannel());
        intent.putExtra("meeting_token", meetingDataPOJO.getMeetingToken());
        intent.putExtra("meeting_code", meetingDataPOJO.getMeetingCode());
        intent.putExtra("start_time", meetingDataPOJO.getPlanStartTime());
        intent.putExtra("end_time", meetingDataPOJO.getPlanEndTime());
        intent.putExtra("intent_type", "FLOW");

        Log.d(TAG,  meetingDataPOJO.getPlanStartTime() + " "+  meetingDataPOJO.getPlanEndTime());
        Log.d(TAG,  meetingDataPOJO.getActualStartTime() + " "+  meetingDataPOJO.getAcutalEndTime());
        dismiss();
        startActivityForResult(intent,123);
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
                cancelReason = btn_TimeMatch.getText().toString();

                getCancelMeeting();
            }
        });
        btm_noTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                cancelReason = btm_noTime.getText().toString();
                getCancelMeeting();
                //  meetingCancelConfirmation();
            }
        });
        dialogs.show();

    }

    public void getCancelMeeting() {
        HashMap<String, Object> meeting_cancel = new HashMap<String, Object>();
        meeting_cancel.put("meeting_request_id", meetingListPOJO.getMeetingCode());
        meeting_cancel.put("cancel_reason", cancelReason);
        meeting_cancel.put("usertype", usertype);
        cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Cancel,meeting_cancel);
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getCancelMeeting(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/cancel-meeting", loginPOJO.getActiveToken(),
                meetingListPOJO.getMeetingCode(), loginPOJO.getRowcode(), cancelReason);
        call.enqueue(new Callback<CommonPOJO>() {
            @Override
            public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d("TAG", response.toString());
                    CommonPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    try {
                        Log.d("TAG",""+reasonPOJO.getMessage());
                        if (reasonPOJO.getOK()) {
                            //  Toast.makeText(getActivity(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();

                            dismiss();
                            //   ((MeetingsFragment)getParentFragment()).onResume();
//

                            EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_CANCEL));

                        } else {
                            Toast.makeText(getContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e){
                        e.getMessage();
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(getActivity(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void successDialogForReshedule() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_meeting_reschedule_confirmed, null);
        TextView label_close = view1.findViewById(R.id.label_close);
        TextView label_title = view1.findViewById(R.id.label_title);

        label_title.setText(getActivity().getResources().getText(R.string.msg_reshedule));
        //    tv_msg.setText("Session Added Successfully.");
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
}