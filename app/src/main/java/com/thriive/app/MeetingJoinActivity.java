package com.thriive.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;



import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonStartMeetingPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.PreciseCountdown;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingJoinActivity extends AppCompatActivity {
    private static final String TAG = MeetingJoinActivity.class.getSimpleName();

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private RtcEngine mRtcEngine;
    private boolean mCallEnd;
    private boolean mMuted;

    private FrameLayout mLocalContainer;
    private RelativeLayout mRemoteContainer;
    private SurfaceView mLocalView;
    private SurfaceView mRemoteView;

    private ImageView mCallBtn;
    private ImageView mMuteBtn;
    private ImageView mSwitchCameraBtn;

    private String meeting_code, meeting_token, meeting_id,meeting_channel, start_time, end_time;

    private PreciseCountdown preciseCountdown, endCountDown;
    private APIInterface apiInterface;
    private LoginPOJO.ReturnEntity loginPOJO;
    private SharedData sharedData;
    private Handler popupHandler;
    private Runnable popupRunnable;



    private String rating_int = "";
    private  KProgressHUD progressHUD;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                }
            });
        }
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                }
            });
        }
        @Override
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"User offline, uid: " + (uid & 0xFFFFFFFFL));
                    onRemoteUserLeft();
                }
            });
        }
    };

    private void setupRemoteVideo(int uid) {
        int count = mRemoteContainer.getChildCount();
        View view = null;
        for (int i = 0; i < count; i++) {
            View v = mRemoteContainer.getChildAt(i);
            if (v.getTag() instanceof Integer && ((int) v.getTag()) == uid) {
                view = v;
            }
        }

        if (view != null) {
            return;
        }
        mRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        mRemoteContainer.addView(mRemoteView);
        // Initializes the video view of a remote user.
        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        mRemoteView.setTag(uid);
    }

    private void onRemoteUserLeft() {
        removeRemoteVideo();
    }

    private void removeRemoteVideo() {
        if (mRemoteView != null) {
            mRemoteContainer.removeView(mRemoteView);
        }
        // Destroys remote view
        mRemoteView = null;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_join);

        sharedData  = new SharedData(getApplicationContext());
        meeting_channel =  getIntent().getStringExtra("meeting_channel");
        meeting_token = getIntent().getStringExtra("meeting_token");
        meeting_id = getIntent().getStringExtra("meeting_id");
        meeting_token = sharedData.getStringData(SharedData.MEETING_TOKEN);
        loginPOJO = Utility.getLoginData(getApplicationContext());
        apiInterface = APIClient.getApiInterface();
        NotificationManager notificationManager = (NotificationManager) getBaseContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Log.d(TAG, meeting_id + " meeting_token " + meeting_token +  "meeting_channel  " + meeting_channel);
        initUI();
        if(getIntent().getStringExtra("intent_type").equals("NOTI")){
            getStartMeeting();
        } else {
            if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                    checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                    checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
                initEngineAndJoinChannel();
            }
            startTimer();
            preciseCountdown.start();
        }



        // Ask for permissions at runtime.
        // This is just an example set of permissions. Other permissions
        // may be needed, and please refer to our online documents.


    }

    private void getStartMeeting() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonStartMeetingPOJO> call = apiInterface.getMeetingStart(loginPOJO.getActiveToken(),
                    Integer.parseInt(meeting_id), true, loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonStartMeetingPOJO>() {
                @Override
                public void onResponse(Call<CommonStartMeetingPOJO> call, Response<CommonStartMeetingPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d("TAG", response.toString());
                        CommonStartMeetingPOJO reasonPOJO = response.body();
                        progressHUD.dismiss();
                        Log.d(TAG,""+reasonPOJO.getMessage());
                        // Log.d(TAG,""+reasonPOJO.getMrParams().getReasonName());
                        if (reasonPOJO != null){
                            if (reasonPOJO.getOK()) {
                                if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                                        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                                        checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
                                    initEngineAndJoinChannel();
                                }
                                startTimer();
                                preciseCountdown.start();
                                //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {

                                //Toast.makeText(MeetingJoinActivity.this, " Meeting has already ended", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                if (isTaskRoot()) {
                                    Intent i = new Intent(MeetingJoinActivity.this, HomeActivity.class);
                                    i.putExtra("intent_type", "FLOW");
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    setResult(000, i);
                                    startActivity(i);
                                    finish();
                                    //super.onBackPressed();
                                }else {
                                    Intent intent = new Intent();
                                    //EventBus.getDefault().post(new EventBusPOJO(Utility.END_CALL_DIALOG, meeting_id));
                                    setResult(000, intent);
                                    finish();

                                    // super.onBackPressed();
                                }
                            }
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

    @SuppressLint("WrongViewCast")
    private void initUI() {
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);

        mCallBtn = findViewById(R.id.btn_call);
        mMuteBtn = findViewById(R.id.btn_mute);
        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera);

       // mLogView = findViewById(R.id.log_recycler_view);

        // Sample logs are optional.
        showSampleLogs();
    }

    private void showSampleLogs() {
//        Log.d(TAG,"Welcome to Agora 1v1 video call");
//        mLogView.logW("You will see custom logs here");
//        mLogView.logE("You can also use this to show errors");
    }


    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i("LOG_TAG", "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MeetingJoinActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQ_ID);
            return false;
        }
        return true;
    }


//    private boolean checkSelfPermission(String permission, int requestCode) {
//        if (ContextCompat.checkSelfPermission(this, permission) !=
//                PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
//            return false;
//        }
//
//        return true;
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initEngineAndJoinChannel();
            } else {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(MeetingJoinActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQ_ID);
            }
        }
//
//        if (requestCode == PERMISSION_REQ_ID) {
//            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
//                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
//                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
//                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
//                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                finish();
//                return;
//            }
//
//            // Here we continue only if all permissions are granted.
//            // The permissions can also be granted in the system settings manually.
//            initEngineAndJoinChannel();
//        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    private void showCustomToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast=Toast.makeText(getApplicationContext(),msg ,Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,20,20);
                View view=toast.getView();
                TextView view1=(TextView)view.findViewById(android.R.id.message);
                view1.setPadding(10,10,10,10);
                view1.setTextColor(Color.BLACK);
                view.setBackgroundResource(R.drawable.rectangle_white);
                toast.show();

            }
        });
    }

    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               Toast toast =  Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
               toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
               toast.show();
            }
        });
    }

    private void initEngineAndJoinChannel() {
        // This is our usual steps for joining
        // a channel and starting a call.
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();

    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine.enableVideo();
        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        mLocalView = RtcEngine.CreateRendererView(getBaseContext());
        mLocalView.setZOrderMediaOverlay(true);
        mLocalContainer.addView(mLocalView);
        // Initializes the local video view.
        // RENDER_MODE_HIDDEN: Uniformly scale the video until it fills the visible boundaries. One dimension of the video may have clipped contents.
        mRtcEngine.setupLocalVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }

    private void joinChannel() {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.
        String token = getString(R.string.agora_access_token);
        if (TextUtils.isEmpty(meeting_token) || TextUtils.equals(meeting_token, "#YOUR ACCESS TOKEN#")) {
            meeting_token = null; // default, no token
        }
        mRtcEngine.joinChannel(meeting_token, meeting_channel, "Extra Optional Data", 0);
        mRtcEngine.setEnableSpeakerphone(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCallEnd) {
            leaveChannel();
        }
        /*
          Destroys the RtcEngine instance and releases all resources used by the Agora SDK.

          This method is useful for apps that occasionally make voice or video calls,
          to free up resources for other operations when not making calls.
         */
        RtcEngine.destroy();
    }

    private void leaveChannel() {
        if (mRtcEngine !=null){
            mRtcEngine.leaveChannel();
        }

    }

    public void onLocalAudioMuteClicked(View view) {
        mMuted = !mMuted;
        // Stops/Resumes sending the local audio stream.
        mRtcEngine.muteLocalAudioStream(mMuted);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn.setImageResource(res);
    }

    public void onSwitchCameraClicked(View view) {
        // Switches between front and rear cameras.
        if (mRtcEngine !=null){
            mRtcEngine.switchCamera();
        }

    }

    public void onCallClicked(View view) {

//        if (mCallEnd) {
//            startCall();
//            mCallEnd = false;
//            mCallBtn.setImageResource(R.drawable.btn_endcall);
//        } else {
//            endCall();
//            mCallEnd = true;
//            mCallBtn.setImageResource(R.drawable.btn_startcall);
//        }
//
//        showButtons(!mCallEnd);
//        getMeetingEnd();

        leaveMeeting();
    }


    private void getMeetingEnd() {
        try {
            Log.d(TAG, loginPOJO.getActiveToken() +  " \n "+
                    meeting_id + " "+ loginPOJO.getRowcode());
            Call<CommonStartMeetingPOJO> call = apiInterface.getMeetingEnd(loginPOJO.getActiveToken(),
                    meeting_id, loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonStartMeetingPOJO>() {
                @Override
                public void onResponse(Call<CommonStartMeetingPOJO> call, Response<CommonStartMeetingPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonStartMeetingPOJO pojo = response.body();
                        Log.d(TAG,""+pojo.getMessage());
                        if (pojo != null){
                            if (pojo.getOK()) {
                                endCall();
                                closeActivity();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonStartMeetingPOJO> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Getting Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }

    }


    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    private void endCall() {
        removeLocalVideo();
        removeRemoteVideo();
        leaveChannel();
    }

    private void removeLocalVideo() {
        if (mLocalView != null) {
            mLocalContainer.removeView(mLocalView);
        }
        mLocalView = null;
    }

    private void showButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mMuteBtn.setVisibility(visibility);
        mSwitchCameraBtn.setVisibility(visibility);
    }

    private void startTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                preciseCountdown = new PreciseCountdown(TimeUnit.MINUTES.toMillis(30), 1000, 0) {
                    @Override
                    public void onTick(long timeLeft) {
                        if (TimeUnit.MINUTES.toMillis(5) == timeLeft){
                            showCustomToast(getResources().getString(R.string.call_message));
                          //  ratingDialog();
                            Log.d(TAG, "!1 min");
                    }

                    }
                    @Override
                    public void onFinished() {
                        Log.d(TAG, "RESTART");
                        getMeetingEnd();
                    }
                };
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

    @Subscribe()
    public void onMessageEvent(EventBusPOJO event) {
        if (event.getEvent() == Utility.END_CALL_FLAG){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    endCall();
                    closeActivity();
                   // showMeetingDialog();
                }
            });

        }
    }


    @Override
    public void onBackPressed() {
        leaveMeeting();
    }


    public void leaveMeeting() {
        //   meetingId = meeting_Id;
        AlertDialog.Builder builder = new AlertDialog.Builder(MeetingJoinActivity.this, R.style.SheetDialog);
        LayoutInflater layoutInflater =  getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_leave_meeting, null);
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        builder.setView(view1);

        ImageView img_close = view1.findViewById(R.id.img_close);
        Button btn_yes = view1.findViewById(R.id.btn_yes);
        Button btn_no = view1.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                getMeetingEnd();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });

        // dialogs.setCancelable(false);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });

        dialogs.show();
    }

    private void closeActivity() {
        if (popupHandler != null) {
            popupHandler.removeCallbacks(popupRunnable);
            popupHandler.removeCallbacksAndMessages(null);
        }
        if (preciseCountdown != null){
            preciseCountdown.cancel();
        }

        sharedData.addStringData(SharedData.MEETING_ID, meeting_id);
        sharedData.addBooleanData(SharedData.SHOW_DIALOG, true);
        if (isTaskRoot()) {
            Intent i = new Intent(MeetingJoinActivity.this, HomeActivity.class);
            i.putExtra("intent_type", "FLOW");
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            EventBus.getDefault().post(new EventBusPOJO(Utility.END_CALL_DIALOG, meeting_id));
            setResult(123, i);
            finish();
            //super.onBackPressed();
        }else {
            Intent intent = new Intent();
            EventBus.getDefault().post(new EventBusPOJO(Utility.END_CALL_DIALOG, meeting_id));
            setResult(123, intent);
            finish();
            // super.onBackPressed();
        }
    }

}
