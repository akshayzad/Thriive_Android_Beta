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
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.MeetingJoinActivity;
import com.thriive.app.R;

import com.thriive.app.adapters.ExperienceListAdapter;
import com.thriive.app.adapters.ExpertiseAdapter;
import com.thriive.app.adapters.SlotListAdapter;
import com.thriive.app.adapters.SlotListFragmentAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonEntitySlotsPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonStartMeetingPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.SwipeController;
import com.thriive.app.utilities.SwipeControllerActions;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */
public class MeetingDetailsFragment extends BottomSheetDialogFragment {
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

    private CommonStartMeetingPOJO.MeetingDataPOJO meetingDataPOJO;
    private CommonMeetingListPOJO.MeetingListPOJO meetingListPOJO;
    private SharedData sharedData;
    private KProgressHUD progressHUD;
    private LoginPOJO.ReturnEntity loginPOJO;
    private APIInterface apiInterface;
    private String cancelReason = "";

    private static  String TAG = MeetingDetailsFragment.class.getName();
    String startTime, endTime, selectedDate;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_CALENDAR
    };
    private ArrayList<CommonEntitySlotsPOJO.EntitySlotList> entitySlotList = new ArrayList<>();

    private  BottomSheetDialog dialogEditSlot;
    public MeetingDetailsFragment() {
        // Required empty public constructor
    }

    public static MeetingDetailsFragment newInstance() {
        return new MeetingDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.SheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_details, container,
                false);
        unbinder = ButterKnife.bind(this, view);
        sharedData = new SharedData(getActivity());
        apiInterface = APIClient.getApiInterface();
        loginPOJO = Utility.getLoginData(getActivity());
        meetingListPOJO = Utility.getMeetingDetailsData(getContext());

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
               // behavior.setPeekHeight(0); // Remove this line to hide a dark background if you manually hide the dialog.
            }
        });
       // arrayList.add(new CommonReOJO());

        setData();

        // get the views and attach the listener


        img_bg.setMaxHeight(layout_data.getHeight());

        return view;

    }

    private void setData() {
        try {
          //  txt_tag.setText(""+meetingListPOJO.getMeetingLabel());
            if (meetingListPOJO.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))){
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

//                if (meetingListPOJO.getGiverDesignationTags().size() > 0){
//                    txt_profession.setText(meetingListPOJO.getGiverDesignationTags().get(0));
//                } else {
//                    txt_profession.setText("");
//                }

                txt_profession.setText(""+meetingListPOJO.getGiverSubTitle());
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(meetingListPOJO.getGiverDomainTags());
                arrayList.addAll(meetingListPOJO.getGiverSubDomainTags());
                FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getContext());
                rv_tags.setLayoutManager(gridLayout );
                if (meetingListPOJO.getGiverExpertiseTags() != null){
                    rv_tags.setAdapter(new ExpertiseAdapter(getContext(), (ArrayList<String>) meetingListPOJO.getGiverExpertiseTags()));
                }

                txt_email.setText(meetingListPOJO.getGiverEmailId());
                ArrayList<String> arrayList1 = new ArrayList<>();
//            for (int i = 0; i<meetingListPOJO.getGiverDesignationTags().size(); i++)
//            {
//                if (i != 0){
//                    arrayList1.add(meetingListPOJO.getGiverDesignationTags().get(i));
//                }
//            }
              //  arrayList1.addAll(meetingListPOJO.getGiverExperienceTags());

                if (meetingListPOJO.getGiverExperienceTags() != null){
                    label_experience.setText("Total " +meetingListPOJO.getGiverExperienceTags().get(0));
                }
                arrayList1.addAll(meetingListPOJO.getGiverDesignationTags());

                rv_experience.setLayoutManager(new FlexboxLayoutManager(getContext()) );
                rv_experience.setAdapter(new ExperienceListAdapter(getContext(), arrayList1));

            } else {
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

//                TextDrawable drawable = TextDrawable.builder().width(60)  // width in px
//                        .height(60)
//                        .buildRound(""+meetingListPOJO.getGiverName().charAt(0), R.color.darkGreyBlue);
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

//                if (meetingListPOJO.getRequestorDesignationTags().size() > 0){
//                    txt_profession.setText(meetingListPOJO.getRequestorDesignationTags().get(0));
//                } else {
//                    txt_profession.setText("");
//                }
                txt_profession.setText(""+meetingListPOJO.getRequestorSubTitle());
                //0));
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(meetingListPOJO.getRequestorDomainTags());
                arrayList.addAll(meetingListPOJO.getRequestorSubDomainTags());
                FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getContext());
                rv_tags.setLayoutManager(gridLayout);
//                if (meetingListPOJO.getMeetingTag() != null){
//                    rv_tags.setAdapter(new ExpertiseAdapter(getContext(), (ArrayList<String>) meetingListPOJO.getMeetingTag()));
//                }
                if (meetingListPOJO.getRequestorExpertiseTags() != null){
                    rv_tags.setAdapter(new ExpertiseAdapter(getContext(), (ArrayList<String>) meetingListPOJO.getRequestorExpertiseTags()));
                }


             //   rv_tags.setAdapter(new ExpertiseAdapter(getContext(), (ArrayList<String>) meetingListPOJO.getMeetingTag()));
                txt_email.setText(meetingListPOJO.getRequestorEmailId());

                ArrayList<String> arrayList1 = new ArrayList<>();
//            for (int i = 0; i<meetingListPOJO.getRequestorDesignationTags().size(); i++)
//            {
//                if (i != 0){
//                    arrayList1.add(meetingListPOJO.getRequestorDesignationTags().get(i));
//                }
//            }
              //  arrayList1.addAll(meetingListPOJO.getRequestorExperienceTags());

                if (meetingListPOJO.getGiverExperienceTags() != null){
                    label_experience.setText("Total "+meetingListPOJO.getRequestorExperienceTags().get(0));
                }
                arrayList1.addAll(meetingListPOJO.getRequestorDesignationTags());

                rv_experience.setAdapter(new ExperienceListAdapter(getContext(), arrayList1));
            }

            txt_reason.setText("Meeting for " +meetingListPOJO.getMeetingReason());
            txt_date.setText(Utility.getMeetingDate(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime())));
            txt_time.setText(Utility.getMeetingTime(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime()),
                    Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanEndTime())));
        } catch (Exception e){
            e.getMessage();
        }


    }



    @OnClick({R.id.txt_cancel, R.id.join_meeting, R.id.img_close, R.id.layout_avail, R.id.btn_email, R.id.btn_linkedin, R.id.txt_add_calender})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_cancel:
                meetingCancel();
                break;

            case R.id.join_meeting:
                if (Utility.getCallJoin(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime()))) {
                    startMeeting();
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

                getMeetingSlot();

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

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(meetingListPOJO.getGiverLinkedinUrl()));
                    intent.setPackage("com.linkedin.android");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(meetingListPOJO.getGiverLinkedinUrl())));
                } finally {

                }

            }

        } else {
            if (meetingListPOJO.getRequestorLinkedinUrl().equals("")){
                Toast.makeText(getActivity(), "Sorry linkedin not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(meetingListPOJO.getRequestorLinkedinUrl()));
                    intent.setPackage("com.linkedin.android");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(meetingListPOJO.getRequestorLinkedinUrl())));
                } finally {

                }
            }
        }
    }

    private void getToEmail() {
        if (meetingListPOJO.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
            if (meetingListPOJO.getGiverEmailId().equals("")){
                Toast.makeText(getActivity(), "Sorry email not found", Toast.LENGTH_SHORT).show();

            } else {
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


    public void getMeetingSlot() {
      //  meetingCode = ""+m;
        try {
            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonEntitySlotsPOJO> call = apiInterface.getEntitySlots(loginPOJO.getActiveToken(), loginPOJO.getRowcode());
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

        SlotListFragmentAdapter adapter  = new SlotListFragmentAdapter(getActivity(), MeetingDetailsFragment.this,
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
        txt_date.setText(Utility.getMeetingDate(startTime));
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
                getResheduledMeeting();
            }
        });
        dialogs.show();
    }

    public void meetingEditDate() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_meeting_date, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
        Button btn_next = dialogView.findViewById(R.id.btn_next);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        CalendarView calender_view = dialogView.findViewById(R.id.calender);
        calender_view.setMinDate(new Date().getTime());
        selectedDate = DateFormat.format("yyyy-MM-dd", calender_view.getDate()).toString();
        Log.d("NEW_DATE", selectedDate);
        calender_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                month = month + 1;
                // output to log cat **not sure how to format year to two places here**
                String newDate = year+"-"+month+"-"+date;
                Log.d("NEW_DATE", newDate);
              //  Toast.makeText(getContext(),date+ "/"+month+"/"+year + "  "+arg0.getDate(),Toast.LENGTH_LONG).show();
                selectedDate = newDate;
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                meetingEditTime();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();


    }

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
                if(Utility.getCallJoin(startTime)){
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
            startTime = Utility.ConvertUserTimezoneToUTC(startTime);
            endTime  = Utility.ConvertUserTimezoneToUTC(endTime);
            Log.d(TAG, startTime + "  " + endTime);
            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.getRescheduleMeeting(loginPOJO.getActiveToken(),
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
                                if (dialogEditSlot != null){
                                    dialogEditSlot.dismiss();
                                }
                                // Toast.makeText(getContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_CANCEL));
                                dismiss();
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
        try {
            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonStartMeetingPOJO> call = apiInterface.getMeetingStart(loginPOJO.getActiveToken(),
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

                                sharedData.addStringData(SharedData.MEETING_TOKEN, meetingDataPOJO.getMeetingToken());
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

    }



    private void callMeeting(){
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
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getCancelMeeting(loginPOJO.getActiveToken(),
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

}