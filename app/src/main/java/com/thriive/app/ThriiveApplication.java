package com.thriive.app;

import android.app.Application;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.onesignal.OneSignal;

import com.thriive.app.handler.ExampleNotificationOpenedHandler;
import com.thriive.app.handler.ExampleNotificationReceivedHandler;
import com.thriive.app.utilities.SharedData;

public class ThriiveApplication extends Application {
    @Override
    public void onCreate() {
       // ActivityLifecycleCallback.register(this);
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // OneSignal Initialization
        CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(this))
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler(this))
                .init();

    }
}
