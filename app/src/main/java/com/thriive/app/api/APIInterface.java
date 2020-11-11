package com.thriive.app.api;

import com.thriive.app.models.BaseUrlPOJo;
import com.thriive.app.models.CommonCountryPOJO;
import com.thriive.app.models.CommonDomainPOJO;
import com.thriive.app.models.CommonEntityImagePOJO;
import com.thriive.app.models.CommonEntityPOJO;
import com.thriive.app.models.CommonEntitySlotsPOJO;
import com.thriive.app.models.CommonExpertisePOJO;
import com.thriive.app.models.CommonHomePOJO;
import com.thriive.app.models.CommonInterestsPOJO;
import com.thriive.app.models.CommonMeetingCountPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.CommonMetaListPOJO;
import com.thriive.app.models.CommonMetaPOJO;
import com.thriive.app.models.CommonObjectivesPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonPersonaPOJO;
import com.thriive.app.models.CommonReasonPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.CommonScheduleMeetingPOJO;
import com.thriive.app.models.CommonStartMeetingPOJO;
import com.thriive.app.models.ExpertiseBodyPOJO;
import com.thriive.app.models.IntrestsBodyPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.MetaListPOJO;
import com.thriive.app.models.ObjectiveBodyPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface
APIInterface {

    @Headers({"Accept:application/json", "Content-Type:application/json;"})
    @POST
    Call<BaseUrlPOJo> GetBaseUrl(@Header("Content-Type") String content_type, @Url String url,@Body RequestBody body);

    @FormUrlEncoded
    @POST
    Call<LoginPOJO> login(@Url String url, @Field("primary_login_key") String primary_login_key,
                          @Field("entity_password") String entity_password,
                          @Field("login_method") String login_method, @Field("app_ver") String app_ver,
                          @Field("platform_ver") String platform_ver,
                          @Field("push_token") String push_token, @Field("voip_token") String voip_token,
                          @Field("app_platform") String app_platform, @Field("time_zone_name") String time_zone_name,
                          @Field("time_stamp") String time_stamp,
                          @Field("first_name") String first_name, @Field("last_name") String last_name,
                          @Field("email_id") String email_id);

// "requestor_id": 1,
//         "requestor_name": "Akshay",
//         "requestor_persona_id": 1,
//         "requestor_persona_name": "Entrepreneur",



    @FormUrlEncoded
    @POST
    Call<CommonReasonPOJO> getReason(@Url String url, @Header("Authorization") String authorization,@Field("requestor_id") String requestor_id, @Field("requestor_name") String requestor_name,
                                     @Field("requestor_persona_id") int requestor_persona_id,
                                     @Field("requestor_persona_name") String requestor_persona_name);


    @FormUrlEncoded
    @POST
    Call<CommonPersonaPOJO> getPersona(@Url String url,@Header("Authorization") String authorization,@Field("requestor_id") String requestor_id, @Field("requestor_name") String requestor_name,
                                       @Field("requestor_persona_id") String requestor_persona_id, @Field("requestor_persona_name") String requestor_persona_name,
                                       @Field("reason_id") String reason_id);

   // api/MRCalls/get-meta-v2
   @FormUrlEncoded
   @POST
   Call<CommonMetaPOJO> getMetaList(@Url String url,@Header("Authorization") String authorization, @Field("requestor_id") String requestor_id, @Field("requestor_name") String requestor_name,
                                    @Field("requestor_persona_id") String requestor_persona_id, @Field("requestor_persona_name") String requestor_persona_name,
                                    @Field("reason_id") String reason_id, @Field("giver_persona_id") String giver_persona_id);

    @FormUrlEncoded
    @POST("api/MRCalls/get-meta-v2")
    Call<CommonMetaPOJO> getMeta(@Url String url,@Header("Authorization") String authorization,@Field("requestor_id") String requestor_id, @Field("requestor_name") String requestor_name,
                                 @Field("requestor_persona_id") String requestor_persona_id, @Field("requestor_persona_name") String requestor_persona_name,
                                 @Field("reason_id") String reason_id, @Field("giver_persona_id") String giver_persona_id);

//      "query" : "tech",
//              "pageSize" : 10,
//              "skipCount" : 0
    @FormUrlEncoded
    @POST
    Call<CommonDomainPOJO> getSearchDomain(@Url String url,@Header("Authorization") String authorization,
                                           @Field("query") String query, @Field("pageSize") String pageSize,
                                           @Field("skipCount") String skipCount);
    @FormUrlEncoded
    @POST
    Call<MetaListPOJO> getSearchDomainV2(@Url String url,@Header("Authorization") String authorization, @Field("query") String query, @Field("pageSize") String pageSize,
                                         @Field("skipCount") String skipCount);

    @FormUrlEncoded
    @POST
    Call<CommonPOJO> getSaveMeetingRequest(@Url String url, @Header("Authorization") String authorization, @Field("requestor_id") int requestor_id,
                                           @Field("reason_id") String reason_id,
                                           @Field("giver_persona_id") String giver_persona_id,
                                           @Field("sel_domain_id") String sel_domain_id,
                                           @Field("sel_sub_domain_id") String sel_sub_domain_id,
                                           @Field("sel_expertise_id") String sel_expertise_id,
                                           @Field("sel_country_id") String sel_country_id);

    @FormUrlEncoded
    @POST
    Call<PendingMeetingRequestPOJO> getPendingMeeting(@Url String url,@Header("Authorization") String authorization, @Field("rowcode") String rowcode);



    @FormUrlEncoded
    @POST
    Call<CommonMeetingListPOJO> getPendingRequest(@Url String url, @Header("Authorization") String authorization, @Field("rowcode") String rowcode);


    @FormUrlEncoded
    @POST
    Call<CommonScheduleMeetingPOJO> getScheduledMeeting(@Url String url, @Header("Authorization") String authorization, @Field("rowcode") String rowcode,
                                                        @Field("push_token") String push_token,
                                                        @Field("time_zone_name") String time_zone_name, @Field("time_stamp") String time_stamp);

    @FormUrlEncoded
    @POST
    Call<CommonPOJO> getAcceptMeeting(@Url String url, @Header("Authorization") String authorization,
                                      @Field("meeting_code") String meeting_code,
                                      @Field("rowcode") String rowcode,
                                      @Field("start_time") String start_time,
                                      @Field("end_time") String end_time);



    @FormUrlEncoded
    @POST
    Call<CommonPOJO> getRescheduleMeeting(@Url String url,@Header("Authorization") String authorization,
                                      @Field("meeting_code") String meeting_code,
                                      @Field("rowcode") String rowcode,
                                      @Field("start_time") String start_time,
                                      @Field("end_time") String end_time);

    @FormUrlEncoded
    @POST
    Call<CommonPOJO> getRejectMeeting(@Url String url, @Header("Authorization") String authorization,
                                      @Field("meeting_code") String meeting_code,
                                      @Field("rowcode") String rowcode);

    @FormUrlEncoded
    @POST
    Call<CommonPOJO> getCancelMeeting(@Url String url, @Header("Authorization") String authorization,
                                      @Field("meeting_code") String meeting_code,
                                      @Field("rowcode") String rowcode,
                                      @Field("cancel_reason") String cancel_reason);

    @FormUrlEncoded
    @POST("api/MRCalls/save-meeting-request")
    Call<CommonPOJO> getSavePendingMeeting(@Url String url, @Header("Authorization") String authorization,
                                       @Field("requestor_id") int requestor_id,
                                       @Field("reason_id") String reason_id,
                                       @Field("giver_persona_id") String giver_persona_id,
                                       @Field("sel_domain_id") String sel_domain_id,
                                       @Field("sel_sub_domain_id") String sel_sub_domain_id,
                                       @Field("sel_expertise_id") String sel_expertise_id);


    @FormUrlEncoded
    @POST
    Call<CommonHomePOJO> getMeetingHome(@Url String url,@Header("Authorization") String authorization,
                                        @Field("rowcode") String rowcode, @Field("push_token") String push_token,
                                        @Field("time_zone_name") String time_zone_name, @Field("time_stamp") String time_stamp);


    @FormUrlEncoded
    @POST
    Call<CommonStartMeetingPOJO> getMeetingStart(@Url String url, @Header("Authorization") String authorization,
                                                 @Field("meeting_id")int meeting_id,
                                                 @Field("force_new_token") boolean force_new_token, @Field("rowcode") String rowcode);


    @FormUrlEncoded
    @POST
    Call<CommonEntitySlotsPOJO> getEntitySlots(@Url String url,@Header("Authorization") String authorization, @Field("rowcode") String rowcode);


    @FormUrlEncoded
    @POST
    Call<CommonMeetingPOJO> getMeetingById(@Url String url, @Header("Authorization") String authorization, @Field("meeting_id") String meeting_id);

//Meeting/meeting-end

    @FormUrlEncoded
    @POST
    Call<CommonStartMeetingPOJO> getMeetingEnd(@Url String url, @Header("Authorization") String authorization,
                                   @Field("meeting_id") String meeting_id,
                                   @Field("rowcode") String rowcode);


    @FormUrlEncoded
    @POST
    Call<CommonMeetingListPOJO> getMeetingHistory(@Url String url, @Header("Authorization") String authorization,
                                                                  @Field("rowcode") String rowcode);


//
//        "meeting_id":23,
//                "rowcode":"23bed6fd",
//                "response_code":"good_review",
//                "response_text":"Good but not relevant",
//                "response_int":3

//
//    {
//        "meeting_id":23,
//            "rowcode":"23bed6fd",
//            "response_code":"good_review",
//            "response_text":"Good but not relevant",
//            "response_int":3
//    }


//    {
//
//        "rowcode":"23bed6fd",
//            "response_code":"good_review",
//            "response_text":"Good but not relevant",
//            "response_int":3,
//            "flag_thumbs":1,
//            "flag_no_show":0,
//            "rating_app":2,
//            "rating_meeting":4
//    }
    @FormUrlEncoded
    @POST
    Call<CommonPOJO> getSaveMeetingReview(@Url String url, @Header("Authorization") String authorization,
                                          @Field("meeting_id") String meeting_id,
                                          @Field("rowcode") String rowcode,
                                          @Field("response_code") String response_code,
                                          @Field("response_text") String response_text,
                                          @Field("response_int") int response_int,
                                          @Field("flag_thumbs") int flag_thumbs,
                                          @Field("flag_no_show") int flag_no_show,
                                          @Field("rating_app") int rating_app,
                                          @Field("rating_meeting") int rating_meeting);

    @FormUrlEncoded
    @POST
    Call<CommonPOJO> getSaveAppReview(@Url String url, @Header("Authorization") String authorization,
                                          @Field("rowcode") String rowcode,
                                          @Field("response_code") String response_code,
                                          @Field("response_text") String response_text,
                                          @Field("response_int") int response_int);
    @FormUrlEncoded
    @POST
    Call<CommonPOJO> getLogout(@Url String url, @Header("Authorization") String authorization,
                               @Field("primary_login_key") String primary_login_key);

    @FormUrlEncoded
    @POST
    Call<CommonPOJO> getForgetPassword(@Url String url, @Field("email_id") String email_id);

    @FormUrlEncoded
    @POST
    Call<CommonPOJO> getChangePassword(@Url String url, @Field("primary_login_key") String primary_login_key, @Field("entity_password") String entity_password);

//    {
//        "rowcode" : "ASF364",
//            "first_name": "Akshay",
//            "last_name": "Zadgaonkar"

    @FormUrlEncoded
    @POST
    Call<CommonEntityPOJO> getSaveEntityName(@Url String url, @Header("Authorization") String authorization,
                                             @Field("rowcode") String rowcode,
                                             @Field("first_name") String first_name,
                                             @Field("last_name") String last_name);

    @FormUrlEncoded
    @POST
    Call<CommonEntityPOJO> getSaveEntityDesignation(@Url String url, @Header("Authorization") String authorization,
                                             @Field("rowcode") String rowcode,
                                             @Field("designation_name") String designation_name);


//    "pic_base64_string" : "asdf32423zs!@#" }
    @FormUrlEncoded
    @POST
    Call<CommonEntityImagePOJO> getUploadEntityPhoto(@Url String url, @Header("Authorization") String authorization,
                                                     @Field("rowcode") String rowcode,
                                                     @Field("pic_base64_string") String pic_base64_string);

    @GET
    Call<CommonCountryPOJO> getCountryList(@Url String url, @Header("Authorization") String authorization);


    @FormUrlEncoded
    @POST
    Call<CommonEntityPOJO> getSaveEntityRegion(@Url String url, @Header("Authorization")String activeToken,@Field("rowcode") String rowcode,
                                               @Field("country_id") int countryCode,  @Field("country_name") String country_name);


    @FormUrlEncoded
    @POST
    Call<CommonMeetingCountPOJO> getMeetingCount(@Url String url,@Header("Authorization")String activeToken, @Field("rowcode") String rowcode);


    @FormUrlEncoded
    @POST
    Call<CommonExpertisePOJO> getExpertise(@Url String url, @Header("Authorization") String authorization,
                                           @Field("rowcode") String rowcode);

    @FormUrlEncoded
    @POST
    Call<CommonObjectivesPOJO> getObjectives(@Url String url, @Header("Authorization") String authorization,
                                             @Field("rowcode") String rowcode);

    @FormUrlEncoded
    @POST
    Call<CommonInterestsPOJO> getInterests(@Url String url, @Header("Authorization") String authorization,
                                           @Field("rowcode") String rowcode);

//    @Headers({"Accept:application/json", "Content-Type:application/json;"})
//    @POST
//    Call<CommonPOJO> saveExpertise(@Header("Content-Type") String content_type, @Url String url,
//                                   @Header("Authorization") String authorization,  @Body RequestBody body);

//    @FormUrlEncoded

    @POST
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<CommonPOJO> saveExpertise(@Url String url, @Header("Authorization") String authorization,
                                    @Body ExpertiseBodyPOJO expertiseList);

    @POST
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<CommonPOJO> saveObjectives(@Url String url, @Header("Authorization") String authorization,
                                    @Body ObjectiveBodyPOJO bodyPOJO);

    @POST
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<CommonPOJO> saveInterests(@Url String url, @Header("Authorization") String authorization,
                                   @Body IntrestsBodyPOJO intrestsBodyPOJO);


}


