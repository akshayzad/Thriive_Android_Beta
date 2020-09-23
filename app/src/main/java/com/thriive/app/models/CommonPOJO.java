package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonPOJO {
    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;

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
}
