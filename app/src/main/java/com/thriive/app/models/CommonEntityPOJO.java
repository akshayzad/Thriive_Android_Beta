package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonEntityPOJO {

    @SerializedName("IsOK")
    @Expose
    private Boolean isOK;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("entity_object")
    @Expose
    private LoginPOJO.ReturnEntity entityObject;

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

    public LoginPOJO.ReturnEntity getEntityObject() {
        return entityObject;
    }

    public void setEntityObject(LoginPOJO.ReturnEntity entityObject) {
        this.entityObject = entityObject;
    }
}
