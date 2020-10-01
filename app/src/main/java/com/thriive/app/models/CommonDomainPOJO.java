package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonDomainPOJO {


    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("domain_list")
    private List<DomainListPOJO> domainList = null;

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

    public List<DomainListPOJO> getDomainList() {
        return domainList;
    }

    public void setDomainList(List<DomainListPOJO> domainList) {
        this.domainList = domainList;
    }
}
