package com.thriive.app.api;

import com.thriive.app.models.CommonCountryPOJO;
import com.thriive.app.models.CommonDomainPOJO;
import com.thriive.app.models.CommonEntityImagePOJO;
import com.thriive.app.models.CommonEntityPOJO;
import com.thriive.app.models.CommonEntitySlotsPOJO;
import com.thriive.app.models.CommonHomePOJO;
import com.thriive.app.models.CommonMeetingCountPOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.CommonMetaPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonPersonaPOJO;
import com.thriive.app.models.CommonReasonPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.CommonStartMeetingPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;

import butterknife.BindView;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface
APIInterface {



    @FormUrlEncoded
    @POST("AppLogin/app-login")
    Call<LoginPOJO> login(@Field("primary_login_key") String primary_login_key, @Field("entity_password") String entity_password,
                          @Field("login_method") String login_method, @Field("app_ver") String app_ver, @Field("platform_ver") String platform_ver,
                          @Field("push_token") String push_token, @Field("voip_token") String voip_token,
                          @Field("app_platform") String app_platform, @Field("time_zone_name") String time_zone_name,
                          @Field("time_stamp") String time_stamp);

// "requestor_id": 1,
//         "requestor_name": "Akshay",
//         "requestor_persona_id": 1,
//         "requestor_persona_name": "Entrepreneur",



    @FormUrlEncoded
    @POST("MRCalls/get-reason")
    Call<CommonReasonPOJO> getReason(@Header("Authorization") String authorization,@Field("requestor_id") String requestor_id, @Field("requestor_name") String requestor_name,
                                     @Field("requestor_persona_id") int requestor_persona_id,
                                     @Field("requestor_persona_name") String requestor_persona_name);


    @FormUrlEncoded
    @POST("MRCalls/get-persona")
    Call<CommonPersonaPOJO> getPersona(@Header("Authorization") String authorization,@Field("requestor_id") String requestor_id, @Field("requestor_name") String requestor_name,
                                       @Field("requestor_persona_id") String requestor_persona_id, @Field("requestor_persona_name") String requestor_persona_name,
                                       @Field("reason_id") String reason_id);



    @FormUrlEncoded
    @POST("MRCalls/get-meta")
    Call<CommonMetaPOJO> getMeta(@Header("Authorization") String authorization,@Field("requestor_id") String requestor_id, @Field("requestor_name") String requestor_name,
                                 @Field("requestor_persona_id") String requestor_persona_id, @Field("requestor_persona_name") String requestor_persona_name,
                                 @Field("reason_id") String reason_id, @Field("giver_persona_id") String giver_persona_id);

//      "query" : "tech",
//              "pageSize" : 10,
//              "skipCount" : 0
    @FormUrlEncoded
    @POST("MRCalls/search-domain")
    Call<CommonDomainPOJO> getSearchDomain(@Header("Authorization") String authorization,@Field("query") String query, @Field("pageSize") String pageSize,
                                           @Field("skipCount") String skipCount);


    @FormUrlEncoded
    @POST("MRCalls/save-meeting-request")
    Call<CommonPOJO> getSaveMeetingRequest(@Header("Authorization") String authorization, @Field("requestor_id") int requestor_id,
                                           @Field("reason_id") String reason_id,
                                           @Field("giver_persona_id") String giver_persona_id,
                                           @Field("sel_domain_id") String sel_domain_id,
                                           @Field("sel_sub_domain_id") String sel_sub_domain_id,
                                           @Field("sel_expertise_id") String sel_expertise_id,
                                           @Field("sel_country_id") String sel_country_id);

    @FormUrlEncoded
    @POST("Meeting/get-pending-meetings")
    Call<PendingMeetingRequestPOJO> getPendingMeeting(@Header("Authorization") String authorization, @Field("rowcode") String rowcode);



    @FormUrlEncoded
    @POST("Meeting/get-pending-requests")
    Call<CommonMeetingListPOJO> getPendingRequest(@Header("Authorization") String authorization, @Field("rowcode") String rowcode);


    @FormUrlEncoded
    @POST("Meeting/get-scheduled-meetings")
    Call<CommonMeetingListPOJO> getScheduledMeeting(@Header("Authorization") String authorization, @Field("rowcode") String rowcode);

    @FormUrlEncoded
    @POST("Meeting/accept-meeting")
    Call<CommonPOJO> getAcceptMeeting(@Header("Authorization") String authorization,
                                      @Field("meeting_code") String meeting_code,
                                      @Field("rowcode") String rowcode,
                                      @Field("start_time") String start_time,
                                      @Field("end_time") String end_time);



    @FormUrlEncoded
    @POST("Meeting/reschedule-meeting")
    Call<CommonPOJO> getRescheduleMeeting(@Header("Authorization") String authorization,
                                      @Field("meeting_code") String meeting_code,
                                      @Field("rowcode") String rowcode,
                                      @Field("start_time") String start_time,
                                      @Field("end_time") String end_time);

    @FormUrlEncoded
    @POST("Meeting/reject-meeting")
    Call<CommonPOJO> getRejectMeeting(@Header("Authorization") String authorization,
                                      @Field("meeting_code") String meeting_code,
                                      @Field("rowcode") String rowcode);

    @FormUrlEncoded
    @POST("Meeting/cancel-meeting")
    Call<CommonPOJO> getCancelMeeting(@Header("Authorization") String authorization,
                                      @Field("meeting_code") String meeting_code,
                                      @Field("rowcode") String rowcode,
                                      @Field("cancel_reason") String cancel_reason);

    @FormUrlEncoded
    @POST("MRCalls/save-meeting-request")
    Call<CommonPOJO> getSavePendingMeeting(@Header("Authorization") String authorization,
                                       @Field("requestor_id") int requestor_id,
                                       @Field("reason_id") String reason_id,
                                       @Field("giver_persona_id") String giver_persona_id,
                                       @Field("sel_domain_id") String sel_domain_id,
                                       @Field("sel_sub_domain_id") String sel_sub_domain_id,
                                       @Field("sel_expertise_id") String sel_expertise_id);


    @FormUrlEncoded
    @POST("Meeting/get-meetings-home")
    Call<CommonHomePOJO> getMeetingHome(@Header("Authorization") String authorization,
                                        @Field("rowcode") String rowcode, @Field("push_token") String push_token,
                                        @Field("time_zone_name") String time_zone_name, @Field("time_stamp") String time_stamp);


    @FormUrlEncoded
    @POST("Meeting/meeting-start")
    Call<CommonStartMeetingPOJO> getMeetingStart(@Header("Authorization") String authorization,
                                                 @Field("meeting_id")int meeting_id,
                                                 @Field("force_new_token") boolean force_new_token, @Field("rowcode") String rowcode);


    @FormUrlEncoded
    @POST("Entity/get-entity-slots")
    Call<CommonEntitySlotsPOJO> getEntitySlots(@Header("Authorization") String authorization, @Field("rowcode") String rowcode);


    @FormUrlEncoded
    @POST("Meeting/get-meeting")
    Call<CommonMeetingPOJO> getMeetingById(@Header("Authorization") String authorization, @Field("meeting_id") String meeting_id);

//Meeting/meeting-end

    @FormUrlEncoded
    @POST("Meeting/meeting-end")
    Call<CommonStartMeetingPOJO> getMeetingEnd(@Header("Authorization") String authorization,
                                   @Field("meeting_id") String meeting_id,
                                   @Field("rowcode") String rowcode);


    @FormUrlEncoded
    @POST("Meeting/get-history-meetings")
    Call<CommonMeetingListPOJO> getMeetingHistory(@Header("Authorization") String authorization,
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

    @FormUrlEncoded
    @POST("meeting/save-meeting-review")
    Call<CommonPOJO> getSaveMeetingReview(@Header("Authorization") String authorization,
                                          @Field("meeting_id") String meeting_id,
                                          @Field("rowcode") String rowcode,
                                          @Field("response_code") String response_code,
                                          @Field("response_text") String response_text,
                                          @Field("response_int") int response_int);

    @FormUrlEncoded
    @POST("meeting/save-app-review")
    Call<CommonPOJO> getSaveAppReview(@Header("Authorization") String authorization,
                                          @Field("rowcode") String rowcode,
                                          @Field("response_code") String response_code,
                                          @Field("response_text") String response_text,
                                          @Field("response_int") int response_int);
    @FormUrlEncoded
    @POST("meeting/save-app-review")
    Call<CommonPOJO> getLogout(@Header("Authorization") String authorization,
                               @Field("rowcode") String rowcode);

    @FormUrlEncoded
    @POST("AppLogin/forgot-password")
    Call<CommonPOJO> getForgetPassword(@Field("email_id") String email_id);

    @FormUrlEncoded
    @POST("AppLogin/change-password")
    Call<CommonPOJO> getChangePassword(@Field("primary_login_key") String primary_login_key, @Field("entity_password") String entity_password);

//    {
//        "rowcode" : "ASF364",
//            "first_name": "Akshay",
//            "last_name": "Zadgaonkar"

    @FormUrlEncoded
    @POST("Entity/save-entity-name")
    Call<CommonEntityPOJO> getSaveEntityName(@Header("Authorization") String authorization,
                                             @Field("rowcode") String rowcode,
                                             @Field("first_name") String first_name,
                                             @Field("last_name") String last_name);

    @FormUrlEncoded
    @POST("Entity/save-entity-designation")
    Call<CommonEntityPOJO> getSaveEntityDesignation(@Header("Authorization") String authorization,
                                             @Field("rowcode") String rowcode,
                                             @Field("designation_name") String designation_name);


//    "pic_base64_string" : "asdf32423zs!@#" }
    @FormUrlEncoded
    @POST("Entity/upload-entity-photo")
    Call<CommonEntityImagePOJO> getUploadEntityPhoto(@Header("Authorization") String authorization,
                                                     @Field("rowcode") String rowcode,
                                                     @Field("pic_base64_string") String pic_base64_string);

    @GET("Entity/get-country-list")
    Call<CommonCountryPOJO> getCountryList();


    @FormUrlEncoded
    @POST("Entity/save-entity-country")
    Call<CommonEntityPOJO> getSaveEntityRegion(@Header("Authorization")String activeToken,@Field("rowcode") String rowcode,
                                               @Field("country_id") int countryCode,  @Field("country_name") String country_name);


    @FormUrlEncoded
    @POST("Entity/get-request-count")
    Call<CommonMeetingCountPOJO> getMeetingCount(@Header("Authorization")String activeToken, @Field("rowcode") String rowcode);
}


