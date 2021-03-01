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
        @SerializedName("request_date")
        String requestDate;
        @SerializedName("meeting_label")
        private String meetingLabel;
        @SerializedName("giver_email_id")
        private String giver_email_id;
        @SerializedName("requestor_email_id")
        private String requestor_email_id;

        @SerializedName("slot_list")
        private List<MeetingDetailPOJO> slot_list = null;

        @SerializedName("sel_meeting")
        private SelMeetingPOJO sel_meeting;

        public String getGiver_email_id() {
            return giver_email_id;
        }

        public void setGiver_email_id(String giver_email_id) {
            this.giver_email_id = giver_email_id;
        }

        public String getRequestor_email_id() {
            return requestor_email_id;
        }

        public void setRequestor_email_id(String requestor_email_id) {
            this.requestor_email_id = requestor_email_id;
        }

        public SelMeetingPOJO getSel_meeting() {
            return sel_meeting;
        }

        public void setSel_meeting(SelMeetingPOJO sel_meeting) {
            this.sel_meeting = sel_meeting;
        }

        public List<MeetingDetailPOJO> getSlot_list() {
            return slot_list;
        }

        public void setSlot_list(List<MeetingDetailPOJO> slot_list) {
            this.slot_list = slot_list;
        }

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

        public String getRequestDate() {
            return requestDate;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public String getMeetingLabel() {
            return meetingLabel;
        }

        public void setMeetingLabel(String meetingLabel) {
            this.meetingLabel = meetingLabel;
        }
    }


}
