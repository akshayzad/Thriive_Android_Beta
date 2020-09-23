package com.thriive.app.api;

import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.LoginPOJO;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {
//
//    @POST("apis/getmasters")
//    Call<MasterPOJO> getMasters();
//
//    @POST("apis/getprofession")
//    Call<CommonProfessionPOJO> getProfession();
//
//    @POST("apis/getlanguange")
//    Call<LanguagePOJO> getLanguage();
//
//    @POST("apis/gettopic")
//    Call<TopicPOJO> getTopic();



    @FormUrlEncoded
    @POST("login")
    Call<LoginPOJO> login(@Field("primary_login_key") String primary_login_key, @Field("entity_password") String entity_password,
                          @Field("login_method") String login_method);



}


