package com.thriive.app.api;

import android.content.Intent;
import android.os.Handler;

import com.thriive.app.HomeActivity;
import com.thriive.app.LoginActivity;
import com.thriive.app.ThriiveApplication;
import com.thriive.app.fragments.NewHomeFragment;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static APIInterface apiInterface = null;

    private APIClient(){

    }
    public static APIInterface getApiInterface(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        okhttp3.Response response = chain.proceed(request);

                        // todo deal with the issues the way you need to
                        if (response.code() == 401) {
                            /*ThriiveApplication.getAppContext().startActivity(
                                    new Intent(
                                            ThriiveApplication.getAppContext(),
                                            LoginActivity.class
                                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            );*/

//                            ((HomeActivity)ThriiveApplication.getAppContext()).finish();

                            SharedData sharedData = new SharedData(ThriiveApplication.getAppContext());

                            sharedData.addBooleanData(SharedData.isFirstVisit, false);
                            sharedData.addBooleanData(SharedData.isLogged, false);
                            sharedData.clearPref(ThriiveApplication.getAppContext());
                            Utility.clearLogin(ThriiveApplication.getAppContext());
                            Utility.clearMeetingDetails(ThriiveApplication.getAppContext());
                            /*Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {*/
                                    // progressHUD.dismiss();
                                    Intent intent = new Intent(ThriiveApplication.getAppContext(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    ThriiveApplication.getAppContext().startActivity(intent);
//                                    ((HomeActivity)ThriiveApplication.getAppContext()).finishAffinity();
                                /*}
                            }, 2000);*/

                            return response;
                        }

                        return response;
                    }
                })
                .build();

        if(apiInterface == null){
            apiInterface = new Retrofit.Builder()
                    .baseUrl("https://niticode.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build().create(APIInterface.class);
        }
        return apiInterface;
    }

}
