package com.thriive.app;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.CleverTapInstanceConfig;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;

import com.thriive.app.handler.ExampleNotificationOpenedHandler;
import com.thriive.app.handler.ExampleNotificationReceivedHandler;
import com.thriive.app.handler.MyFcmMessageListenerService;
import com.thriive.app.utilities.SharedData;

public class ThriiveApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
       // ActivityLifecycleCallback.register(this);
        super.onCreate();

        context = getApplicationContext();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //clevarTap Notification
        CleverTapAPI cleverTapAPI = CleverTapAPI.getDefaultInstance(getApplicationContext());
        CleverTapAPI.createNotificationChannel(getApplicationContext(),"Thriive_Push","Thriive_Push","Thriive Push Notification", NotificationManager.IMPORTANCE_MAX,true);

        CleverTapInstanceConfig clevertapAdditionalInstanceConfig =  CleverTapInstanceConfig.createInstance(context, "WW6-ZW6-895Z", "601-600");
        clevertapAdditionalInstanceConfig.setDebugLevel(CleverTapAPI.LogLevel.DEBUG); // default is CleverTapAPI.LogLevel.INFO
        clevertapAdditionalInstanceConfig.setAnalyticsOnly(true); // disables the user engagement features of the instance, default is false
        clevertapAdditionalInstanceConfig.useGoogleAdId(true); // enables the collection of the Google ADID by the instance, default is false

        //One Signal
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(this))
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler(this))
                .init();

    }

    public static Context getAppContext() {
        return context;
    }
}
