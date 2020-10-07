package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingMeetingRequestPOJO {
    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("meeting_request_list")
    private List<MeetingRequestList> meetingRequestList = null;

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

    public List<MeetingRequestList> getMeetingRequestList() {
        return meetingRequestList;
    }

    public void setMeetingRequestList(List<MeetingRequestList> meetingRequestList) {
        this.meetingRequestList = meetingRequestList;
    }

    public class MeetingRequestList {

        @SerializedName("domain_tags")
        private List<String> domainTags = null;
        @SerializedName("sub_domain_tags")
        private List<String> subDomainTags = null;
        @SerializedName("expertise_tags")
        private List<String> expertiseTags = null;
        @SerializedName("country_tags")
        private List<String> countryTags = null;
        @SerializedName("meeting_request_id")
        private Integer meetingRequestId;
        @SerializedName("reason_name")
        private String reasonName;
        @SerializedName("giver_persona_name")
        private String giverPersonaName;
        @SerializedName("requestor_id")
        private Integer requestorId;


        public List<String> getDomainTags() {
            return domainTags;
        }

        public void setDomainTags(List<String> domainTags) {
            this.domainTags = domainTags;
        }

        public List<String> getSubDomainTags() {
            return subDomainTags;
        }

        public void setSubDomainTags(List<String> subDomainTags) {
            this.subDomainTags = subDomainTags;
        }

        public List<String> getExpertiseTags() {
            return expertiseTags;
        }

        public void setExpertiseTags(List<String> expertiseTags) {
            this.expertiseTags = expertiseTags;
        }

        public List<String> getCountryTags() {
            return countryTags;
        }

        public void setCountryTags(List<String> countryTags) {
            this.countryTags = countryTags;
        }

        public Integer getMeetingRequestId() {
            return meetingRequestId;
        }

        public void setMeetingRequestId(Integer meetingRequestId) {
            this.meetingRequestId = meetingRequestId;
        }

        public String getReasonName() {
            return reasonName;
        }

        public void setReasonName(String reasonName) {
            this.reasonName = reasonName;
        }

        public String getGiverPersonaName() {
            return giverPersonaName;
        }

        public void setGiverPersonaName(String giverPersonaName) {
            this.giverPersonaName = giverPersonaName;
        }

        public Integer getRequestorId() {
            return requestorId;
        }

        public void setRequestorId(Integer requestorId) {
            this.requestorId = requestorId;
        }





    }
}
