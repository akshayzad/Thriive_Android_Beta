package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommonMeetingListPOJO {

    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("meeting_list")
    private ArrayList<MeetingListPOJO> meetingList = null;

    public Boolean getOK() {
        return isOK;
    }

    public void setOK(Boolean OK) {
        isOK = OK;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<MeetingListPOJO> getMeetingList() {
        return meetingList;
    }

    public void setMeetingList(ArrayList<MeetingListPOJO> meetingList) {
        this.meetingList = meetingList;
    }

    public class MeetingListPOJO {



        @SerializedName("meeting_id")
        @Expose
        private Integer meetingId;
        @SerializedName("meeting_token")
        @Expose
        private String meetingToken;
        @SerializedName("meeting_request_id")
        @Expose
        private Integer meetingRequestId;
        @SerializedName("meeting_code")
        @Expose
        private String meetingCode;
        @SerializedName("meeting_channel")
        @Expose
        private String meetingChannel;
        @SerializedName("meeting_reason")
        @Expose
        private String meetingReason;
        @SerializedName("meeting_domain")
        @Expose
        private String meetingDomain;
        @SerializedName("meeting_sub_domain")
        @Expose
        private String meetingSubDomain;
        @SerializedName("meeting_expertise")
        @Expose
        private Object meetingExpertise;
        @SerializedName("plan_start_time")
        @Expose
        private String planStartTime;
        @SerializedName("plan_end_time")
        @Expose
        private String planEndTime;
        @SerializedName("giver_id")
        @Expose
        private Integer giverId;
        @SerializedName("giver_name")
        @Expose
        private String giverName;
        @SerializedName("giver_pic_url")
        @Expose
        private String giverPicUrl;
        @SerializedName("giver_email_id")
        @Expose
        private String giverEmailId;
        @SerializedName("giver_linkedin_url")
        @Expose
        private String giverLinkedinUrl;
        @SerializedName("date_match")
        @Expose
        private String dateMatch;
        @SerializedName("giver_persona_tags")
        @Expose
        private List<String> giverPersonaTags = null;
        @SerializedName("giver_objective_tags")
        @Expose
        private List<String> giverObjectiveTags = null;
        @SerializedName("giver_experience_tags")
        @Expose
        private List<String> giverExperienceTags = null;
        @SerializedName("giver_domain_tags")
        @Expose
        private List<String> giverDomainTags = null;
        @SerializedName("giver_sub_domain_tags")
        @Expose
        private List<String> giverSubDomainTags = null;
        @SerializedName("giver_designation_tags")
        @Expose
        private List<String> giverDesignationTags = null;
        @SerializedName("giver_expertise_tags")
        @Expose
        private List<String> giverExpertiseTags = null;
        @SerializedName("giver_response_int")
        @Expose
        private Integer giverResponseInt;
        @SerializedName("requestor_id")
        @Expose
        private Integer requestorId;
        @SerializedName("requestor_name")
        @Expose
        private String requestorName;
        @SerializedName("requestor_pic_url")
        @Expose
        private String requestorPicUrl;
        @SerializedName("requestor_email_id")
        @Expose
        private String requestorEmailId;
        @SerializedName("requestor_linkedin_url")
        @Expose
        private String requestorLinkedinUrl;
        @SerializedName("requestor_persona_tags")
        @Expose
        private List<String> requestorPersonaTags = null;
        @SerializedName("requestor_objective_tags")
        @Expose
        private List<String> requestorObjectiveTags = null;
        @SerializedName("requestor_experience_tags")
        @Expose
        private List<String> requestorExperienceTags = null;
        @SerializedName("requestor_domain_tags")
        @Expose
        private List<String> requestorDomainTags = null;
        @SerializedName("requestor_sub_domain_tags")
        @Expose
        private List<String> requestorSubDomainTags = null;
        @SerializedName("requestor_designation_tags")
        @Expose
        private List<String> requestorDesignationTags = null;
        @SerializedName("requestor_expertise_tags")
        @Expose
        private List<String> requestorExpertiseTags = null;
        @SerializedName("requestor_response_int")
        @Expose
        private Integer requestorResponseInt;

        public Integer getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(Integer meetingId) {
            this.meetingId = meetingId;
        }

        public String getMeetingToken() {
            return meetingToken;
        }

        public void setMeetingToken(String meetingToken) {
            this.meetingToken = meetingToken;
        }

        public Integer getMeetingRequestId() {
            return meetingRequestId;
        }

        public void setMeetingRequestId(Integer meetingRequestId) {
            this.meetingRequestId = meetingRequestId;
        }

        public String getMeetingCode() {
            return meetingCode;
        }

        public void setMeetingCode(String meetingCode) {
            this.meetingCode = meetingCode;
        }

        public String getMeetingChannel() {
            return meetingChannel;
        }

        public void setMeetingChannel(String meetingChannel) {
            this.meetingChannel = meetingChannel;
        }

        public String getMeetingReason() {
            return meetingReason;
        }

        public void setMeetingReason(String meetingReason) {
            this.meetingReason = meetingReason;
        }

        public String getMeetingDomain() {
            return meetingDomain;
        }

        public void setMeetingDomain(String meetingDomain) {
            this.meetingDomain = meetingDomain;
        }

        public String getMeetingSubDomain() {
            return meetingSubDomain;
        }

        public void setMeetingSubDomain(String meetingSubDomain) {
            this.meetingSubDomain = meetingSubDomain;
        }

        public Object getMeetingExpertise() {
            return meetingExpertise;
        }

        public void setMeetingExpertise(Object meetingExpertise) {
            this.meetingExpertise = meetingExpertise;
        }

        public String getPlanStartTime() {
            return planStartTime;
        }

        public void setPlanStartTime(String planStartTime) {
            this.planStartTime = planStartTime;
        }

        public String getPlanEndTime() {
            return planEndTime;
        }

        public void setPlanEndTime(String planEndTime) {
            this.planEndTime = planEndTime;
        }

        public Integer getGiverId() {
            return giverId;
        }

        public void setGiverId(Integer giverId) {
            this.giverId = giverId;
        }

        public String getGiverName() {
            return giverName;
        }

        public void setGiverName(String giverName) {
            this.giverName = giverName;
        }

        public String getGiverPicUrl() {
            return giverPicUrl;
        }

        public void setGiverPicUrl(String giverPicUrl) {
            this.giverPicUrl = giverPicUrl;
        }

        public String getGiverEmailId() {
            return giverEmailId;
        }

        public void setGiverEmailId(String giverEmailId) {
            this.giverEmailId = giverEmailId;
        }

        public String getGiverLinkedinUrl() {
            return giverLinkedinUrl;
        }

        public void setGiverLinkedinUrl(String giverLinkedinUrl) {
            this.giverLinkedinUrl = giverLinkedinUrl;
        }

        public String getDateMatch() {
            return dateMatch;
        }

        public void setDateMatch(String dateMatch) {
            this.dateMatch = dateMatch;
        }

        public List<String> getGiverPersonaTags() {
            return giverPersonaTags;
        }

        public void setGiverPersonaTags(List<String> giverPersonaTags) {
            this.giverPersonaTags = giverPersonaTags;
        }

        public List<String> getGiverObjectiveTags() {
            return giverObjectiveTags;
        }

        public void setGiverObjectiveTags(List<String> giverObjectiveTags) {
            this.giverObjectiveTags = giverObjectiveTags;
        }

        public List<String> getGiverExperienceTags() {
            return giverExperienceTags;
        }

        public void setGiverExperienceTags(List<String> giverExperienceTags) {
            this.giverExperienceTags = giverExperienceTags;
        }

        public List<String> getGiverDomainTags() {
            return giverDomainTags;
        }

        public void setGiverDomainTags(List<String> giverDomainTags) {
            this.giverDomainTags = giverDomainTags;
        }

        public List<String> getGiverSubDomainTags() {
            return giverSubDomainTags;
        }

        public void setGiverSubDomainTags(List<String> giverSubDomainTags) {
            this.giverSubDomainTags = giverSubDomainTags;
        }

        public List<String> getGiverDesignationTags() {
            return giverDesignationTags;
        }

        public void setGiverDesignationTags(List<String> giverDesignationTags) {
            this.giverDesignationTags = giverDesignationTags;
        }

        public List<String> getGiverExpertiseTags() {
            return giverExpertiseTags;
        }

        public void setGiverExpertiseTags(List<String> giverExpertiseTags) {
            this.giverExpertiseTags = giverExpertiseTags;
        }

        public Integer getGiverResponseInt() {
            return giverResponseInt;
        }

        public void setGiverResponseInt(Integer giverResponseInt) {
            this.giverResponseInt = giverResponseInt;
        }

        public Integer getRequestorId() {
            return requestorId;
        }

        public void setRequestorId(Integer requestorId) {
            this.requestorId = requestorId;
        }

        public String getRequestorName() {
            return requestorName;
        }

        public void setRequestorName(String requestorName) {
            this.requestorName = requestorName;
        }

        public String getRequestorPicUrl() {
            return requestorPicUrl;
        }

        public void setRequestorPicUrl(String requestorPicUrl) {
            this.requestorPicUrl = requestorPicUrl;
        }

        public String getRequestorEmailId() {
            return requestorEmailId;
        }

        public void setRequestorEmailId(String requestorEmailId) {
            this.requestorEmailId = requestorEmailId;
        }

        public String getRequestorLinkedinUrl() {
            return requestorLinkedinUrl;
        }

        public void setRequestorLinkedinUrl(String requestorLinkedinUrl) {
            this.requestorLinkedinUrl = requestorLinkedinUrl;
        }

        public List<String> getRequestorPersonaTags() {
            return requestorPersonaTags;
        }

        public void setRequestorPersonaTags(List<String> requestorPersonaTags) {
            this.requestorPersonaTags = requestorPersonaTags;
        }

        public List<String> getRequestorObjectiveTags() {
            return requestorObjectiveTags;
        }

        public void setRequestorObjectiveTags(List<String> requestorObjectiveTags) {
            this.requestorObjectiveTags = requestorObjectiveTags;
        }

        public List<String> getRequestorExperienceTags() {
            return requestorExperienceTags;
        }

        public void setRequestorExperienceTags(List<String> requestorExperienceTags) {
            this.requestorExperienceTags = requestorExperienceTags;
        }

        public List<String> getRequestorDomainTags() {
            return requestorDomainTags;
        }

        public void setRequestorDomainTags(List<String> requestorDomainTags) {
            this.requestorDomainTags = requestorDomainTags;
        }

        public List<String> getRequestorSubDomainTags() {
            return requestorSubDomainTags;
        }

        public void setRequestorSubDomainTags(List<String> requestorSubDomainTags) {
            this.requestorSubDomainTags = requestorSubDomainTags;
        }

        public List<String> getRequestorDesignationTags() {
            return requestorDesignationTags;
        }

        public void setRequestorDesignationTags(List<String> requestorDesignationTags) {
            this.requestorDesignationTags = requestorDesignationTags;
        }

        public List<String> getRequestorExpertiseTags() {
            return requestorExpertiseTags;
        }

        public void setRequestorExpertiseTags(List<String> requestorExpertiseTags) {
            this.requestorExpertiseTags = requestorExpertiseTags;
        }

        public Integer getRequestorResponseInt() {
            return requestorResponseInt;
        }

        public void setRequestorResponseInt(Integer requestorResponseInt) {
            this.requestorResponseInt = requestorResponseInt;
        }
    }
}
