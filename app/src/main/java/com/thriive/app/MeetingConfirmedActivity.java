package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.thriive.app.adapters.ExpertiseAdapter;
import com.thriive.app.adapters.MeetingTagAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingConfirmedActivity extends AppCompatActivity {
    @BindView(R.id.txt_giverName)
    TextView txt_giverName;

    @BindView(R.id.img_giver)
    CircleImageView img_giver;

    @BindView(R.id.img_requestor)
    CircleImageView img_requestor;

    @BindView(R.id.txt_meeting)
    TextView txt_meeting;

    @BindView(R.id.txt_region)
    TextView txt_region;

    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;
    @BindView(R.id.txt_reason)
    TextView txt_reason;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.layout_data)
    RelativeLayout layout_data;

    private KProgressHUD progressHUD;

    private APIInterface apiInterface;
    private LoginPOJO.ReturnEntity loginPOJO;

    public static final String  TAG = MeetingConfirmedActivity.class.getName();

    private SharedData sharedData;
    CommonMeetingListPOJO.MeetingListPOJO  meetingListPOJO;

    private CleverTapAPI cleverTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_confirmed);
        ButterKnife.bind(this);
        apiInterface = APIClient.getApiInterface();
        cleverTap = CleverTapAPI.getDefaultInstance(getApplicationContext());

        sharedData = new SharedData(getApplicationContext());
        loginPOJO = Utility.getLoginData(getApplicationContext());
        getMeetingById();
        HashMap<String, Object> visitEvent = new HashMap<String, Object>();
        visitEvent.put("meeting_request_id", getIntent().getStringExtra("meeting_id"));
        cleverTap.pushEvent(Utility.View_Matched_Users_Profile,visitEvent);
    }

    private void getMeetingById() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonMeetingPOJO> call = apiInterface.getMeetingById(sharedData.getStringData(SharedData.API_URL) + "api/Meeting/get-meeting", loginPOJO.getActiveToken(),
                getIntent().getStringExtra("meeting_id"));
        call.enqueue(new Callback<CommonMeetingPOJO>() {
            @Override
            public void onResponse(Call<CommonMeetingPOJO> call, Response<CommonMeetingPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    try {
                        CommonMeetingPOJO pojo = response.body();
                        if (pojo != null){
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo.getOK()) {

                                meetingListPOJO  = pojo.getMeetingObject();
                                setData(pojo.getMeetingObject());

                            } else {
                                progressHUD.dismiss();
                                Toast.makeText(getApplicationContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e){
                        e.getMessage();
                    }


                }
            }
            @Override
            public void onFailure(Call<CommonMeetingPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @OnClick({R.id.btn_view, R.id.txt_notNow})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_view:
                // finish();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("intent_type", "MEETING_DETAILS");
                intent.putExtra("meeting_id", meetingListPOJO.getMeetingId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Utility.saveMeetingDetailsData(getApplicationContext(), meetingListPOJO);
                startActivity(intent);
                finish();

//                MeetingDetailsFragment meetingDetailsFragment =
//                        (MeetingDetailsFragment) MeetingDetailsFragment.newInstance();
//                meetingDetailsFragment.show(getSupportFragmentManager(), "MeetingDetailsFragment");
                break;


            case R.id.txt_notNow:

                finish();
                break;

        }
    }
    private void setData(CommonMeetingListPOJO.MeetingListPOJO meetingObject) {
        txt_reason.setText("Meeting for " +meetingObject.getMeetingReason());
        txt_meeting.setText(meetingObject.getGiverName() + " has confirmed to meet with you");
        txt_giverName.setText(meetingObject.getGiverSubTitle());
        txt_region.setText(meetingObject.getGiverCountryName());
        txt_time.setText(Utility.getMeetingDateWithTime(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime())) + ", " +
                Utility.getMeetingTime(Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanStartTime()),
                        Utility.ConvertUTCToUserTimezone(meetingListPOJO.getPlanEndTime())));
        if (meetingObject.getGiverPicUrl().equals("")){
            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_medium);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(getResources().getColor(R.color.whiteTwo))
                    .useFont(typeface)
                    .fontSize(60) /* size in px */
                    .bold()
                    .toUpperCase()
                    .width(140)  // width in px
                    .height(140) // height in px
                    .endConfig()
                    .buildRect(Utility.getInitialsName(meetingObject.getGiverName()) , getResources().getColor(R.color.darkSeaGreen));
            img_giver.setImageDrawable(drawable);
        } else {
            img_giver.setMinimumWidth(120);
            img_giver.setMaxHeight(120);
            img_giver.setMinimumHeight(120);
            img_giver.setMaxWidth(120);
            Glide.with(this)
                    .load(meetingObject.getGiverPicUrl())
                    .into(img_giver);
        }

        if (meetingObject.getRequestorPicUrl().equals("")){
            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_medium);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(getResources().getColor(R.color.whiteTwo))
                    .useFont(typeface)
                    .fontSize(60) /* size in px */
                    .bold()
                    .toUpperCase()
                    .width(140)  // width in px
                    .height(140) // height in px
                    .endConfig()
                    .buildRect(Utility.getInitialsName(meetingObject.getRequestorName()) , getResources().getColor(R.color.terracota));
            img_requestor.setImageDrawable(drawable);
        } else {
            img_requestor.setMinimumWidth(120);
            img_requestor.setMaxHeight(120);
            img_requestor.setMinimumHeight(120);
            img_requestor.setMaxWidth(120);
            Glide.with(this)
                    .load(meetingObject.getRequestorPicUrl())
                    .into(img_requestor);
        }
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getApplicationContext());
        gridLayout.setFlexWrap(FlexWrap.WRAP);
        gridLayout.setJustifyContent(JustifyContent.CENTER);
        rv_tags.setLayoutManager(gridLayout );
        rv_tags.setAdapter(new MeetingTagAdapter(getApplicationContext(), (ArrayList<String>) meetingObject.getGiverPersonaTags()));
        progressHUD.dismiss();
        layout_data.setVisibility(View.VISIBLE);
    }
}