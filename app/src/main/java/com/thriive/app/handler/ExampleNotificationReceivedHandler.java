package com.thriive.app.handler;

import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.utilities.Utility;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    private static String TAG = ExampleNotificationReceivedHandler.class.getName();
    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        Log.d(TAG, data.toString());
        if (data != null) {
            //JSONObject jsonObject = null;
            try {

                String meeting_trigger = data.getString("meeting_trigger");
                String meeting_id = data.getString("meeting_id");
                switch (meeting_trigger){
                    case "giver_meeting_request":
                        EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_REQUEST, meeting_id));
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
