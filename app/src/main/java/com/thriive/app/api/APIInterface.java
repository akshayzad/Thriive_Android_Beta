package com.thriive.app.api;

import com.thriive.app.models.CommonDomainPOJO;
import com.thriive.app.models.CommonEntitySlotsPOJO;
import com.thriive.app.models.CommonHomePOJO;
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

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
                          @Field("push_token") String push_token, @Field("voip_token") String voip_token, @Field("app_platform") String app_platform);

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
    Call<CommonHomePOJO> getMeetingHome(@Header("Authorization") String authorization, @Field("rowcode") String rowcode);


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


}


