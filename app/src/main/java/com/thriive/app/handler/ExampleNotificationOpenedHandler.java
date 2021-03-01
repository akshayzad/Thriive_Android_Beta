package com.thriive.app.handler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.thriive.app.HomeActivity;
import com.thriive.app.MeetingJoinActivity;
import com.thriive.app.NotificationListActivity;
import com.thriive.app.ThriiveApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    private static String TAG = com.thriive.app.handler.ExampleNotificationOpenedHandler.class.getName();
    Context context;

    public ExampleNotificationOpenedHandler(Context context) {
        this.context = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        if (result != null) {
            JSONObject data = result.notification.payload.additionalData;
            Log.d(TAG, data.toString());
            Log.e(TAG, "notificationOpened: "+data.toString() );
            try {
              //  JSONObject jsonObject = data.getJSONObject("data");
                String meeting_trigger = data.getString("meeting_trigger");
                String  meeting_id = data.getString("meeting_id");
                Intent intent = null;
                switch (meeting_trigger){
                    case "meeting_request_submitted":
                        intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("intent_type", "NOTI");
                        intent.putExtra("meeting_id", meeting_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);


                        break;

                    case "meeting_confirmed":
                        intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("intent_type", "NOTI");
                        intent.putExtra("meeting_id", meeting_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);


                        break;

                    case "giver_meeting_request":
//                        intent = new Intent(context, NotificationListActivity.class);
                        intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("meeting_id", meeting_id);
                        intent.putExtra("intent_type", "NOTI");
                        intent.putExtra("view_type", "NOTI");
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;


                    case "giver_meeting_confirmation":
                        intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("intent_type", "NOTI");
                        intent.putExtra("meeting_id", meeting_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;


                    case "meeting_reschedule":
                        intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("intent_type", "NOTI");
                        intent.putExtra("meeting_id", meeting_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;


                    case "meeting_cancelled":
                        intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("intent_type", "FLOW");
                        intent.putExtra("meeting_id", meeting_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;

                    case "one_day_prior_reminder":
                        intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("intent_type", "NOTI");
                        intent.putExtra("meeting_id", meeting_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;

                    case "one_hour_prior_reminder":
                        intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("intent_type", "NOTI");
                        intent.putExtra("meeting_id", meeting_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;

                    case "meeting_started":

                        JSONObject jsonObject = data.getJSONObject("meeting_object");
                        String  meeting_channel = jsonObject.getString("meeting_channel");
                        String  meeting_token = jsonObject.getString("meeting_token");
                        String  meeting_code = jsonObject.getString("meeting_code");
                        intent = new Intent(context, MeetingJoinActivity.class);
                        intent.putExtra("meeting_id", meeting_id);

                        intent.putExtra("meeting_channel", meeting_channel );
                        intent.putExtra("meeting_token", meeting_token);
                        intent.putExtra("meeting_code", meeting_code);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            // meeting_trigger
        }
    }

}
