package com.thriive.app.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.LoginPOJO;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.util.Locale.getDefault;

public class Utility {

    public static final int MEETING_BOOK = 400 ;
    public static final int MEETING_REQUEST = 100;
    public static final int MEETING_CANCEL = 200;
    public static final int END_CALL_FLAG = 300;
    public static final int END_CALL_DIALOG = 500;
    public static final String PRIVACY_POLICY = "Privacy Policy";
    public static final String TERMS = "Terms of Service";

    public static String UTILITY_URL = "https://niticode.com/";

    public static void saveLoginData(Context context, LoginPOJO.ReturnEntity loginPOJOData){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginPOJOData);
        editor.putString("login_data",json);
        editor.apply();
    }

    public static LoginPOJO.ReturnEntity getLoginData(Context context){
        String login = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE).getString("login_data", "");
        Gson gson = new Gson();
        return  gson.fromJson(login, LoginPOJO.ReturnEntity.class);
    }

    public static void clearLogin(Context context){
        SharedPreferences preferences =context.getSharedPreferences("login_pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
    public static void clearMeetingDetails(Context context){
        SharedPreferences preferences =context.getSharedPreferences("meeting_pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void saveMeetingDetailsData(Context context, CommonMeetingListPOJO.MeetingListPOJO loginPOJOData){
        SharedPreferences sharedPreferences = context.getSharedPreferences("meeting_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginPOJOData);
        editor.putString("meeting_data",json);
        editor.apply();
    }

    public static CommonMeetingListPOJO.MeetingListPOJO getMeetingDetailsData(Context context){
        String login = context.getSharedPreferences("meeting_pref", Context.MODE_PRIVATE).getString("meeting_data", "");
        Gson gson = new Gson();
        return  gson.fromJson(login, CommonMeetingListPOJO.MeetingListPOJO.class);
    }


    public static NetworkInfo checkInternet(Context context){
        ConnectivityManager ConnectionManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return ConnectionManager.getActiveNetworkInfo();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public  static  String getEncodedName(String name){
        String[] splited = name.trim().split("\\s+");
        StringBuilder s1 = new StringBuilder();
        try {
            String split_one=splited[0];
            for (int i = 0; i < split_one.length(); i++){
                if (i == 0){
                    s1.append(split_one.charAt(i));
                } else {
                    s1.append("x");
                }
            }
        } catch (Exception e){
            e.getMessage();
        }


        StringBuilder s2 = new StringBuilder();
        try {
            String split_second=splited[1];
            for (int i = 0; i < split_second.length(); i++){
                if (i == 0){
                    s2.append(split_second.charAt(i));
                } else {
                    s2.append("x");
                }
            }
        } catch (Exception e){
            e.getMessage();
        }
        return  s1 + "  " + s2;
    }
    public static String getMeetingDate(String date, String endDate){
        String dtStart = "";
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat out_format = new SimpleDateFormat("d MMM");

        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm aa");

        try {
            String resultDate = out_format.format(in_format.parse(date));
            String sDate = time_format.format(in_format.parse(date));
            String eDate = time_format.format(in_format.parse(endDate));
            Log.d("samedate",resultDate);
            dtStart = resultDate + ", " + sDate + " - "+ eDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return  dtStart;

    }

    public static String getScheduledMeetingDate(String date, String endDate){
        String dtStart = "";
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat out_format = new SimpleDateFormat("d MMM");

        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm aa");

        try {
            String resultDate = out_format.format(in_format.parse(date));
            String sDate = time_format.format(in_format.parse(date));
            String eDate = time_format.format(in_format.parse(endDate));
            Log.d("samedate",resultDate);
            dtStart =   "" + sDate + " - "+ eDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return  dtStart;

    }

    public  static String getTimeStamp()
    {
        String time_stamp = "";
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                ZonedDateTime now = null;
                now = ZonedDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
                        .withZone(ZoneId.systemDefault());
                System.out
                        .println(ZonedDateTime.parse(now.format(formatter), formatter));
                time_stamp = "" + ZonedDateTime.parse(now.format(formatter), formatter);
                Log.d("time_stamp", time_stamp);
            }
        } catch (Exception e){
            e.getMessage();
        }

        return ""+time_stamp;
    }

    public static String getScheduleMeetingDate(String date){
        String dtStart = "";
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat out_format = new SimpleDateFormat("EEEE, MMM dd");

        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm aa");

        try {
            String resultDate = out_format.format(in_format.parse(date));
            //String sDate = time_format.format(in_format.parse(date));
            //   String eDate = time_format.format(in_format.parse(endDate));
            Log.d("samedate",resultDate);
            dtStart = resultDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return  dtStart;

    }

    public static String getCallJoin(String datetime) {
        String call = "";
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sourceFormat.setTimeZone(TimeZone.getDefault());

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        format.setTimeZone(TimeZone.getDefault());

        Calendar calendar = Calendar.getInstance();
        Date parsed = null;
        try {
            parsed = sourceFormat.parse(datetime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        String server_date = sourceFormat.format(parsed);
        String system_date = sourceFormat.format(calendar.getTime());
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sourceFormat.parse(server_date);
            d2 = sourceFormat.parse(system_date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Log.d("D1", " "+ d1);
        Log.d("D2", " "+ d2);
        long diff = d1.getTime() - d2.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        Log.d("time", "D " + diffDays + " H " + diffHours + " M "
                + diffMinutes + " S " + diffSeconds);
        if (diffDays == 0){
            if (diffHours == 0){
                if (diffMinutes <= 0) {
                    call = "called";
                } else {

//                    call = String.format("%1$02d", hours) + ":" + String.format("%1$02d", minutes)
//                            + ":" + String.format("%1$02d", seconds);
                    call =  "Meeting is yet to start.";
                }
            }
        }

        return  call;
    }

    public static String getMeetingDate(String date){
        String dtStart = "";
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat out_format = new SimpleDateFormat("d MMM, yyyy");

        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm aa");

        try {
            String resultDate = out_format.format(in_format.parse(date));
            //String sDate = time_format.format(in_format.parse(date));
         //   String eDate = time_format.format(in_format.parse(endDate));
            Log.d("samedate",resultDate);
            dtStart = resultDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return  dtStart;

    }

    public static String getMeetingTime(String date, String endDate){
        String dtStart = "2019-08-15T09:27:37Z";
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
       // SimpleDateFormat out_format = new SimpleDateFormat("d MMM, yyyy");

        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm aa");

        try {
            //String resultDate = out_format.format(in_format.parse(date));
            String sDate = time_format.format(in_format.parse(date));
            String eDate = time_format.format(in_format.parse(endDate));
         //   Log.d("samedate",resultDate);
            dtStart = sDate + " - "+ eDate ;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return  dtStart;

    }

    public static String getSlotDate(String sdate){
        String dtStart = "";
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //in_format.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat out_format = new SimpleDateFormat("dd-MM-yyyy");
      //  out_format.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm aa");
        ////time_format.setTimeZone(TimeZone.getDefault());
        try {
            String resultDate = out_format.format(in_format.parse(sdate));
           // String sDate = time_format.format(in_format.parse(sdate));
            //String eDate = time_format .format(in_format.parse(edate));
            dtStart = resultDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return  dtStart;

    }



    public static String getSlotTime(String sdate, String edate){
        String dtStart = "";
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
       // in_format.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat out_format = new SimpleDateFormat("dd-mm-yyyy");
        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm aa");
        //time_format.setTimeZone(TimeZone.getDefault());
        try {
         //   String resultDate = out_format.format(in_format.parse(sdate));
            String sDate = time_format.format(in_format.parse(sdate));
            String eDate = time_format .format(in_format.parse(edate));
            dtStart =  sDate + " - "+ eDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return  dtStart;

    }


    public static String getInitialsName(String name){
        String[] splited = name.trim().split("\\s+");
        String split_one=splited[0];
        String split_second=splited[1];

        StringBuilder s1 = new StringBuilder();
        try {
            for (int i = 0; i < split_one.length(); i++){
                if (i == 0){
                    s1.append(split_one.charAt(i));
                }
            }
        } catch (Exception e){
            e.getMessage();
        }


        StringBuilder s2 = new StringBuilder();
        try {
            for (int i = 0; i < split_second.length(); i++){
                if (i == 0){
                    s2.append(split_second.charAt(i));
                }
            }
        } catch (Exception e){
            e.getMessage();
        }

        return "" +s1 + "" +s2;
    }


    public static String convertDate(String date){
         String dtStart = "";
        SimpleDateFormat out_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

       // SimpleDateFormat time_format = new SimpleDateFormat("hh:mm aa");

        try {
            String resultDate = out_format.format(in_format.parse(date));
            Log.d("samedate",resultDate);
            dtStart = resultDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return  dtStart;

    }
    public static String getOneHour(String date){
       // String dtStart = "2019-08-15T09:27:37Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date myDateTime = null;

        //Parse your string to SimpleDateFormat
        try
        {
            myDateTime = simpleDateFormat.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        System.out.println("This is the Actual Date:"+myDateTime);
        Calendar cal = new GregorianCalendar();
        cal.setTime(myDateTime);

        //Adding 21 Hours to your Date
        cal.add(Calendar.HOUR_OF_DAY, 1);
        System.out.println("This is Hours Added Date:"+cal.getTime());


//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' hh:mm aa");
        System.out.println(simpleDateFormat.format(cal.getTime()));



        return simpleDateFormat.format(cal.getTime());
    }


    public static  String ConvertUTCToUserTimezone(String datetime)
    {
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        in_format.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat out_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        out_format.setTimeZone(TimeZone.getDefault());

        Date parsed = null;
        try {
            parsed = in_format.parse(datetime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        String date = out_format.format(parsed);
        Log.d("UTILITY", "utc "+ parsed);
        Log.d("UTILITY", "user "+ date);

        return ""+date;
    }


    public static String ConvertUserTimezoneToUTC(String datetime) {
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        in_format.setTimeZone(TimeZone.getDefault());

        SimpleDateFormat out_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        out_format.setTimeZone(TimeZone.getTimeZone("UTC"));


        Date parsed = null;
        try {
            parsed = in_format.parse(datetime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        String date = out_format.format(parsed);
        Log.d("UTILITY", "user "+ parsed);
        Log.d("UTILITY", "utc "+ date);

        return ""+date;

    }
}
