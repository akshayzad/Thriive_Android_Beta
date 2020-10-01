package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonHomePOJO {


    @SerializedName("IsOK")
    @Expose
    private Boolean isOK;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("meeting_request_list")
    @Expose
    private List<PendingMeetingRequestPOJO.MeetingRequestList> meetingRequestList = null;
    @SerializedName("meeting_scheduled_list")
    @Expose
    private List<CommonMeetingListPOJO.MeetingListPOJO> meetingScheduledList = null;

    @SerializedName("pending_request_count")
    int pendingRequestCount;

    public int getPendingRequestCount() {
        return pendingRequestCount;
    }

    public void setPendingRequestCount(int pendingRequestCount) {
        this.pendingRequestCount = pendingRequestCount;
    }

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

    public List<PendingMeetingRequestPOJO.MeetingRequestList> getMeetingRequestList() {
        return meetingRequestList;
    }

    public void setMeetingRequestList(List<PendingMeetingRequestPOJO.MeetingRequestList> meetingRequestList) {
        this.meetingRequestList = meetingRequestList;
    }

    public List<CommonMeetingListPOJO.MeetingListPOJO> getMeetingScheduledList() {
        return meetingScheduledList;
    }

    public void setMeetingScheduledList(List<CommonMeetingListPOJO.MeetingListPOJO> meetingScheduledList) {
        this.meetingScheduledList = meetingScheduledList;
    }
}
