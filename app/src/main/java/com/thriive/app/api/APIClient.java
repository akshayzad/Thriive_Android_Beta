package com.thriive.app.api;

import com.thriive.app.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
