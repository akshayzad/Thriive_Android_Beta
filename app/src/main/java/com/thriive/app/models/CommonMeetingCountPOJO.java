package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonMeetingCountPOJO {

    @SerializedName("IsOK")
    @Expose
    private Boolean isOK;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("done_count")
    @Expose
    private Integer doneCount;
    @SerializedName("template_message")
    @Expose
    private String templateMessage;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getDoneCount() {
        return doneCount;
    }

    public void setDoneCount(Integer doneCount) {
        this.doneCount = doneCount;
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

    public String getTemplateMessage() {
        return templateMessage;
    }

    public void setTemplateMessage(String templateMessage) {
        this.templateMessage = templateMessage;
    }
}
