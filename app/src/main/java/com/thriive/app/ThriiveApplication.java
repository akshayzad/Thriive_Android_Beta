package com.thriive.app;

import android.app.Application;
import android.util.Log;

import com.onesignal.OneSignal;

import com.thriive.app.handler.ExampleNotificationOpenedHandler;
import com.thriive.app.handler.ExampleNotificationReceivedHandler;
import com.thriive.app.utilities.SharedData;

public class ThriiveApplication extends Application {
    SharedData sharedData;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedData = new SharedData(this);

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(this))
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler(this))
                .init();
       // Fabric.with(this, new Crashlytics());
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                String token = userId;
                sharedData.addStringData(SharedData.PUSH_TOKEN, token);
                Log.d("token","one signal token "+token);
            }
        });


    }
}
