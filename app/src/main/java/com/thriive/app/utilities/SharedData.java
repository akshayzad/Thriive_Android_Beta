package com.thriive.app.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedData {
    Context context;
    SharedPreferences prefs;

    public static String first_time_open = "first_time_open";
    public static String LOGIN_STATUS="LOGIN_STATUS";
    public static String domainId ="domainId";
    public static String subDomainId ="subDomainId";
    public static String isLogged = "isLogged";
    public static String isFirstVisit = "isFirstVisit";
    public static String isMeetingRequestVisit = "isMeetingRequestVisit";
    public static String requestMeetingBook = "isFirstVisit";
    public static final String USER_ID = "USER_ID";
    public static final String MEETING_TOKEN = "MEETING_TOKEN";
    public static final String SHOW_DIALOG = "SHOW_DIALOG";
    public static final String MEETING_ID = "MEETING_ID";
    public static final String PUSH_TOKEN = "PUSH_TOKEN";
    public static final String CALLING_NAME = "CALLING_NAME";
    public static final String MEETING_BOOKED = "MEETING_BOOKED" ;
    public static final String MEETING_DONE = "MEETING_DONE";
    public static final String MEETING_TOTAL = "MEETING_TOTAL";

    public static String PREFS_2 = "PREFS_2";
    private SharedPreferences prefs2 ;

    public SharedData(Context context)
    {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs2 = context.getSharedPreferences(PREFS_2, Context.MODE_PRIVATE);
    }

    public void addStringData(String key, String value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public void addIntData(String key, Integer value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key,value);
        editor.apply();
    }
    public void addBooleanData(String key, Boolean value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public void clearPref(Context context) {
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences(PREFS_2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public String getStringData(String key)
    {
        return prefs.getString(key,"");
    }

    public int getIntData(String key)
    {
        return prefs.getInt(key,0);
    }

    public boolean getBooleanData(String key)
    {
        return prefs.getBoolean(key,false);
    }


    public SharedPreferences getPrefs()
    {
        return prefs;
    }

    public void setPrefs(SharedPreferences prefs)
    {
        this.prefs = prefs;
    }
    public SharedPreferences getPrefs2()
    {
        return prefs2;
    }

    public void addStringSharedPrefs2(String key, String value)
    {
        SharedPreferences.Editor editor = prefs2.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public void addStringSharedPrefs2(String key, Boolean value)
    {
        SharedPreferences.Editor editor = prefs2.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
