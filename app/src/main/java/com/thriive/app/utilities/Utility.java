package com.thriive.app.utilities;

import android.app.Activity;
import android.content.Intent;

public class Utility {

    public  static void getIntent(Activity activity, Activity intent_activity){
        Intent intent =  new Intent(activity, intent_activity.getClass());
        //activity.getClass().sta

    }
}
