package com.thriive.app;

import android.app.Application;
import android.util.Log;

import com.onesignal.OneSignal;

import com.thriive.app.handler.ExampleNotificationOpenedHandler;
import com.thriive.app.handler.ExampleNotificationReceivedHandler;
import com.thriive.app.utilities.SharedData;

public class ThriiveApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(this))
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler(this))
                .init();

    }
}
