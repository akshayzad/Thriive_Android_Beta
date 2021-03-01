package com.thriive.app.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.BuildConfig;

import com.google.gson.Gson;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.HomeDisplayPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.MeetingListPOJO;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Utility {

    public static final int MEETING_BOOK = 400 ;
    public static final int MEETING_REQUEST = 100;
    public static final int MEETING_CANCEL = 200;
    public static final int END_CALL_FLAG = 300;
    public static final int END_CALL_DIALOG = 500;
    public static final String PRIVACY_POLICY = "Privacy Policy";
    public static final String TERMS = "Terms of Service";
    public static final String REGISTER = "Register";

    public static final String CLAVER_TAB_Meeting_Request_Initiated = "Meeting Request Initiated";
    public static final String CLAVER_TAB_Meeting_Request_Step1 = "Meeting Request Step1";
    public static final String CLAVER_TAB_Meeting_Request_Step2 = "Meeting Request Step2";
    public static final String CLAVER_TAB_Meeting_Request_Step3 = "Meeting Request Step3";
    public static final String CLAVER_TAB_Meeting_Requested = "Meeting Requested";
    public static final String CLAVER_TAB_Meeting_Reschedule = "Meeting Reschedule";
    public static final String CLAVER_TAB_Meeting_Cancel= "Meeting Cancel";
    public  static final String Meeting_Request_Viewed ="Meeting Request Viewed";
    public static final String Meeting_Request_Accepted = "Meeting Request Accepted";
    public static final String Meeting_Request_Declined ="Meeting Request Declined";
    public static final  String View_Matched_Users_Profile = "View Matched Users Profile";
    public static final String Clicked_Matched_Users_Email = "Clicked Matched Users Email";
    public static final  String Clicked_Matched_Users_LinkedIn = "Clicked Matched Users LinkedIn";
    public static final String Clicked_Add_to_Calendar = "Clicked Add to Calendar";
    public static final String User_Joins_Meeting = "User Joins Meeting";

    public static final String Meeting_Started = "Meeting Started";
    public static final String Meeting_Ended = "Meeting Ended";
    public static final String Meeting_Rated = "Meeting Rated";

    public static final String Viewed_Alerts = "Viewed Alerts";
    public static final String Updated_Preferences= "Updated Preferences";
    public static final String Viewed_Connections = "Viewed Connections";
    public static final String Updated_Profile_Info= "Updated Profile Info";
    public static final String User_Profile ="Login(IdentitySet)";

    public static final String BASEURL = "https://api.thriive.app/api/default/GetBaseUrl" ;

    public static final String SLOT_JSON = "{\n" +
            "    \"IsOK\": true,\n" +
            "    \"Message\": \"\",\n" +
            "    \"slot_list\": [\n" +
            "        {\n" +
            "            \"from_hour\":10,\n" +
            "\t    \"from_min\":30,\n" +
            "\t    \"to_hour\":11,\n" +
            "\t    \"to_min\":0,\n" +
            "\t    \"for_date\": \"2020-10-01T00:00:00Z\"\n" +
            "\t    \n" +
            "        },\n" +
            " \t{\n" +
            "            \"from_hour\":11,\n" +
            "\t    \"from_min\":30,\n" +
            "\t    \"to_hour\":12,\n" +
            "\t    \"to_min\":0,\n" +
            "\t    \"for_date\": \"2020-10-01T00:00:00Z\"\n" +
            "\t    \n" +
            "        },\n" +
            "    \t{\n" +
            "            \"from_hour\":14,\n" +
            "\t    \"from_min\":30,\n" +
            "\t    \"to_hour\":15,\n" +
            "\t    \"to_min\":0,\n" +
            "\t    \"for_date\": \"2020-10-01T00:00:00Z\"\n" +
            "\t    \n" +
            "        },\n" +
            " \t{\n" +
            "            \"from_hour\":16,\n" +
            "\t    \"from_min\":30,\n" +
            "\t    \"to_hour\":17,\n" +
            "\t    \"to_min\":0,\n" +
            "\t    \"for_date\": \"2020-10-01T00:00:00Z\"\n" +
            "\t    \n" +
            "        }\n" +
            "        \n" +
            "    ]\n" +
            "}";

    public static RequestBody getJsonEncode(Activity activity) {

//        PackageManager manager = activity.getPackageManager();
//        PackageInfo info = null;
//        try {
//            info = manager.getPackageInfo(activity.getPackageName(), PackageManager.GET_ACTIVITIES);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        Log.d("TAG", "PackageName = " + info.packageName + "\nVersionCode = "
//                + info.versionCode + "\nVersionName = " + info.versionName);

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("platform_name", "android");
        jsonParams.put("internal_app_version", 9);

        Log.e("params", jsonParams.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams)).toString());
        Log.d("params", jsonParams.toString());
        return body;
    }

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

    public static void saveNextMeetingDetailsData(Context context, HomeDisplayPOJO.MeetingData meetingPOJOData){
        SharedPreferences sharedPreferences = context.getSharedPreferences("meeting_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(meetingPOJOData);
        editor.putString("next_meeting_data",json);
        editor.apply();
    }

    public static HomeDisplayPOJO.MeetingData getNextMeetingDetailsData(Context context){
        String login = context.getSharedPreferences("meeting_pref", Context.MODE_PRIVATE).getString("next_meeting_data", "");
        Gson gson = new Gson();
        return  gson.fromJson(login, HomeDisplayPOJO.MeetingData.class);
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

    public static void closeKeyboard(Context context){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (Activity) context;
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

    public static boolean getCheckSlotTime(String datetime) {
        boolean call = false;
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
            if (diffHours <= 0) {
                if (diffMinutes <= 0) {
                    if (diffSeconds <= 0) {
                        call = true;
                    } else {
                        call = false;
                    }
                } else {
                    call = false;
                }
            } else {
                call = false;
            }
        } else {
            call = false;
        }

        return call;
    }


    public static boolean getCallJoin(String datetime) {
        boolean call = false;
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
            if (diffHours <= 0) {
                if (diffMinutes <= 0) {
                    if (diffSeconds <= 0) {
                        call = true;
                    } else {
                        call = true;
                    }
                } else {
                    call = false;
                }
            } else {
                call = false;
            }
        } else {
            call = false;
        }

        return call;
    }

    public static boolean getCallEdJoinJoin(String datetime) {
        boolean call = false;
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
            if (diffHours <= 0) {
                if (diffMinutes <= 0) {
                    if (diffSeconds <= 0) {
                        call = true;
                    } else {
                        call = false;
                    }
                } else {
                    call = false;
                }
            } else {
                call = false;
            }
        } else {
            call = false;
        }

        return call;
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

    public static String getMeetingDateWithTime(String date){
        String dtStart = "";
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat out_format = new SimpleDateFormat("d MMM");

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
        String dtStart = "";
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
        String split_one = "", split_second = "";
        try {
             split_one=splited[0];
             split_second=splited[1];
        } catch (Exception e){

        }


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

    public static String convertLocaleToUtc(String datetime){
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd");
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
        cal.add(Calendar.MINUTE, 30);
        System.out.println("This is Hours Added Date:"+cal.getTime());


//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' hh:mm aa");
        System.out.println(simpleDateFormat.format(cal.getTime()));



        return simpleDateFormat.format(cal.getTime());
    }


    public static  String ConvertUTCToUserTimezoneForSlot(String datetime)
    {
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        in_format.setTimeZone(TimeZone.getTimeZone("UTC"));

//        SimpleDateFormat out_format = new SimpleDateFormat("MMM d");
        SimpleDateFormat out_format = new SimpleDateFormat("d MMM");
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

    public static  String ConvertUTCToUserTimezoneForSlotDay(String datetime)
    {
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        in_format.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat out_format = new SimpleDateFormat("EEE");
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


    public static  String ConvertUTCToUserTimezoneForSlotTime(String datetime)
    {
        SimpleDateFormat in_format = new SimpleDateFormat("HH:mm");
        in_format.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat out_format = new SimpleDateFormat("HH:mm");
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

    public static String ConvertUTCToUserTimezone(String datetime)
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

    public static  String ConvertUTCToUserTimezoneWithTime(String datetime)
    {
        SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        in_format.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat out_format = new SimpleDateFormat("HH:mm");
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

    public static long getTimeDifferenceWithCurrentTime(String datetime){
        boolean call = false;
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

//        long diffSeconds = diff / 1000 % 60;
//        long diffMinutes = diff / (60 * 1000) % 60;
//        long diffHours = diff / (60 * 60 * 1000) % 24;
//        long diffDays = diff / (24 * 60 * 60 * 1000);
//        Log.d("time", "D " + diffDays + " H " + diffHours + " M "
//                + diffMinutes + " S " + diffSeconds);
//        if (diffDays == 0){
//            if (diffHours <= 0) {
//                if (diffMinutes <= 0) {
//                    if (diffSeconds <= 0) {
//                        call = true;
//                    } else {
//                        call = false;
//                    }
//                } else {
//                    call = false;
//                }
//            } else {
//                call = false;
//            }
//        } else {
//            call = false;
//        }

        return diff;
    }

    public static long getNotificationTimer(String addedDate, String datetime){
        boolean call = false;
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sourceFormat.setTimeZone(TimeZone.getDefault());

        Calendar calendar = Calendar.getInstance();
        Date parsed = null;
        Date parsed1 = null;
        try {
            parsed = sourceFormat.parse(datetime);
            parsed1 = sourceFormat.parse(addedDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        String server_date = sourceFormat.format(parsed);
        String system_date = sourceFormat.format(parsed1);
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

        return diff;
    }
}
