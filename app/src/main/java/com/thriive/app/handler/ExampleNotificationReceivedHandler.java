package com.thriive.app.handler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;
import com.thriive.app.HomeActivity;
import com.thriive.app.MeetingJoinActivity;
import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.ThriiveApplication;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Random;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    private static String TAG = ExampleNotificationReceivedHandler.class.getName();
    String ADMIN_CHANNEL_ID ="Thriive_APP";
    Context context;

    private static RemoteViews contentView;
    private static Notification notification;
    private static NotificationManager notificationManager;
    private static final int NotificationID = 1005;
    private static NotificationCompat.Builder mBuilder;
    SharedData sharedData;

    String giver_name = "";
    private String meeting_code, meeting_token, meeting_id,meeting_channel, start_time, end_time;
    public ExampleNotificationReceivedHandler(ThriiveApplication thriiveApplication) {
        this.context = thriiveApplication;
    }

    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        Log.d(TAG, data.toString());
        sharedData = new SharedData(context);
        if (data != null) {
            //JSONObject jsonObject = null;
            try {

                String meeting_trigger = data.getString("meeting_trigger");
                String meeting_id = "";
                String message = data.getString("message");
                String title = data.getString("title");
                switch (meeting_trigger){
                    case "meeting_request_submitted":
                        CustomIntentNotification(title, message);

                        break;

                    case "meeting_confirmed":
                        meeting_id = data.getString("meeting_id");
                        HomeIntentNotification(title, message, meeting_id);
                        break;

                    case "giver_meeting_request":
                        meeting_id = data.getString("meeting_id");
                      //  EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_REQUEST, meeting_id));
                        NotificationIntentNotification(title, message, meeting_id);
                        break;


                    case "giver_meeting_confirmation":
                        meeting_id = data.getString("meeting_id");
                        HomeIntentNotification(title, message, meeting_id);
                        break;


                    case "meeting_reschedule":
                        //customViewNotification();
                        meeting_id = data.getString("meeting_id");
                       HomeIntentNotification(title, message, meeting_id);
                        break;

                    case "meeting_cancelled":

                        CustomIntentNotification(title, message);
                        break;

                    case "one_day_prior_reminder":
                        meeting_id = data.getString("meeting_id");
                        HomeIntentNotification(title, message, meeting_id);
                        break;

                    case "one_hour_prior_reminder":
                        meeting_id = data.getString("meeting_id");
                        HomeIntentNotification(title, message, meeting_id);
                        break;

                    case "meeting_started":
                        JSONObject jsonObject = data.getJSONObject("meeting_object");
                        meeting_id = data.getString("meeting_id");
                        meeting_channel = jsonObject.getString("meeting_channel");
                        meeting_token = jsonObject.getString("meeting_token");
                        sharedData.addStringData(SharedData.MEETING_TOKEN, meeting_token);
                        meeting_code = jsonObject.getString("meeting_code");
                        start_time = jsonObject.getString("plan_start_time");
                        end_time = jsonObject.getString("plan_end_time");
                     //   giver_name = jsonObject.getString("giver_name");
                       // sharedData.addStringData(SharedData.CALLING_NAME, giver_name);
                        meetingJoinNotification(title, message, meeting_id);

                        break;

                    case "meeting_ended":
                     //   meeting_id = data.getString("meeting_id");
                        EventBus.getDefault().post(new EventBusPOJO(Utility.END_CALL_FLAG));
                        CustomIntentNotification(title, message);

                        break;

                    case "meeting_reminder":
                        meeting_id = data.getString("meeting_id");
                        HomeIntentNotification(title, message, meeting_id);
                        break;

//                    default:
//                        CustomIntentNotification(title, message);
//                        break;


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

         //   OneSignal.cancelNotification(notification.androidNotificationId);
        }
    }


    private void meetingJoinNotification( String title, String message, String meeting_id) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        Intent intent = new Intent(context, MeetingJoinActivity.class);
        intent.setAction(meeting_id+""+notificationID);
        intent.putExtra("meeting_id", ""+meeting_id);
        intent.putExtra("meeting_channel", meeting_channel);
        intent.putExtra("meeting_token", meeting_token);
        intent.putExtra("meeting_code", meeting_code);
        intent.putExtra("start_time", start_time);
        intent.putExtra("end_time", end_time);
        intent.putExtra("intent_type", "NOTI");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.app_logo);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon
                )
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(R.color.whiteTwo))
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setLargeIcon(largeIcon);
            notificationBuilder.setColor(context.getResources().getColor(R.color.whiteTwo));
        }
        notificationManager.notify(notificationID, notificationBuilder.build());

    }

    private void NotificationIntentNotification( String title,String message, String meeting_id ) {
        String ADMIN_CHANNEL_ID ="Thriive_APP";


        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        Intent intent = new Intent(context, NotificationListActivity.class);
        intent.setAction(meeting_id+""+notificationID);
        intent.putExtra("intent_type", "NOTI");
        intent.putExtra("meeting_id", meeting_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.app_logo);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setLargeIcon(largeIcon)
                .setColor(context.getResources().getColor(R.color.whiteTwo))
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setLargeIcon(largeIcon);
            notificationBuilder.setColor(context.getResources().getColor(R.color.whiteTwo));
        }
        notificationManager.notify(notificationID, notificationBuilder.build());

    }


    private void CustomIntentNotification( String title,String message ) {
        String ADMIN_CHANNEL_ID ="Thriive_APP";
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        Intent intent = new Intent(context, HomeActivity.class);
        intent.setAction(meeting_id+""+notificationID);
        intent.putExtra("intent_type", "FLOW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.app_logo);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setLargeIcon(largeIcon)
                .setColor(context.getResources().getColor(R.color.whiteTwo))
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setLargeIcon(largeIcon);
            notificationBuilder.setColor(context.getResources().getColor(R.color.whiteTwo));
        }
        notificationManager.notify(notificationID, notificationBuilder.build());

    }

    private void HomeIntentNotification( String title,String message, String meeting_id ) {
        String ADMIN_CHANNEL_ID ="Thriive_APP";


        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setAction(meeting_id+""+notificationID);
        intent.putExtra("intent_type", "NOTI");
        intent.putExtra("meeting_id", meeting_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.app_logo);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setLargeIcon(largeIcon)
                .setColor(context.getResources().getColor(R.color.whiteTwo))
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setLargeIcon(largeIcon);
            notificationBuilder.setColor(context.getResources().getColor(R.color.whiteTwo));
        }
        notificationManager.notify(notificationID, notificationBuilder.build());

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to device notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }


}
