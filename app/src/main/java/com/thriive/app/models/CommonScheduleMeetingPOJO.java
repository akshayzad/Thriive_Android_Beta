package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommonScheduleMeetingPOJO {

    @SerializedName("IsOK")
    @Expose
    private Boolean isOK;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("meeting_list")
    private ArrayList<CommonMeetingListPOJO.MeetingListPOJO> meetingList = null;
    @SerializedName("pending_request_count")
    int pendingRequestCount;

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

    public ArrayList<CommonMeetingListPOJO.MeetingListPOJO> getMeetingList() {
        return meetingList;
    }

    public void setMeetingList(ArrayList<CommonMeetingListPOJO.MeetingListPOJO> meetingList) {
        this.meetingList = meetingList;
    }

    public int getPendingRequestCount() {
        return pendingRequestCount;
    }

    public void setPendingRequestCount(int pendingRequestCount) {
        this.pendingRequestCount = pendingRequestCount;
    }
}
