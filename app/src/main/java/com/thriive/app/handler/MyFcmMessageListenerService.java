package com.thriive.app.handler;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.NotificationInfo;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thriive.app.HomeActivity;
import com.thriive.app.R;

import java.util.Map;
import java.util.Random;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

@SuppressLint("Registered")
public class MyFcmMessageListenerService extends FirebaseMessagingService {
    Context context;
    String ADMIN_CHANNEL_ID ="Thriive_Push";
    @Override
    public void onMessageReceived(RemoteMessage message){
        try {
            Log.d("MYFCMLIST", " data " +message.getData().toString());
            if (message.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : message.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }

                NotificationInfo info = CleverTapAPI.getNotificationInfo(extras);
                if (info.fromCleverTap) {
                    CleverTapAPI.createNotification(getApplicationContext(), extras);
                    String msg = extras.getString("message");
                    String title = extras.getString("title");
                    CustomIntentNotification(title,msg);

                } else {
                    // not from CleverTap handle yourself or pass to another provider
                }
            }
        } catch (Throwable t) {
            Log.d("MYFCMLIST", "Error parsing FCM message", t);
        }
    }


    @Override
    public void onNewToken(String token) {
        CleverTapAPI.getDefaultInstance(this).pushFcmRegistrationId(token,true);
    }

    private void CustomIntentNotification( String title,String message ) {
        String ADMIN_CHANNEL_ID ="Thriive_Push";
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);

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

//        Intent intent1 = new Intent(getApplicationContext(), HomeActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        @SuppressLint("WrongConstant") NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),"id_product")
//                .setSmallIcon(R.drawable.small_icon) //your app icon
//                .setBadgeIconType(R.drawable.small_icon) //your app icon
//                .setChannelId(ADMIN_CHANNEL_ID)
//                .setContentTitle(title)
//                .setAutoCancel(true).setContentIntent(pendingIntent)
//                .setNumber(1)
//                .setColor(255)
//                .setContentText(message)
//                .setWhen(System.currentTimeMillis());
//        notificationManager.notify(notificationID, notificationBuilder.build());

    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void setupChannels(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            String id = "id_product";
//            // The user-visible name of the channel.
//            CharSequence name = "Product";
//            // The user-visible description of the channel.
//            String description = "Notifications regarding our products";
//            int importance = NotificationManager.IMPORTANCE_MAX;
//            @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(id, name, importance);
//            // Configure the notification channel.
//            mChannel.setDescription(description);
//            mChannel.enableLights(true);
//            // Sets the notification light color for notifications posted to this
//            // channel, if the device supports this feature.
//            mChannel.setLightColor(Color.RED);
//            notificationManager.createNotificationChannel(mChannel);
//        }
//    }

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