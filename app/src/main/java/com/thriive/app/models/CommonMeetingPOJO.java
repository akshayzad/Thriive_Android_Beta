package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonMeetingPOJO {

    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("meeting_object")
    private CommonMeetingListPOJO.MeetingListPOJO meetingObject;

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

    public CommonMeetingListPOJO.MeetingListPOJO getMeetingObject() {
        return meetingObject;
    }

    public void setMeetingObject(CommonMeetingListPOJO.MeetingListPOJO meetingObject) {
        this.meetingObject = meetingObject;
    }
}
