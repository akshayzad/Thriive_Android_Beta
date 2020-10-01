package com.thriive.app;

import android.app.Application;
import android.util.Log;

import com.onesignal.OneSignal;

import com.thriive.app.handler.ExampleNotificationOpenedHandler;
import com.thriive.app.handler.ExampleNotificationReceivedHandler;

public class ThriiveApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(this))
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                String token = userId;
                Log.d("token","one signal token "+token);
            }
        });


    }
}
