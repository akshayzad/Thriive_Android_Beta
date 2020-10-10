package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonCountryPOJO {

    @SerializedName("IsOK")
    @Expose
    private Boolean isOK;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("country_list")
    @Expose
    private List<CountryListPOJO> countryList = null;

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

    public List<CountryListPOJO> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<CountryListPOJO> countryList) {
        this.countryList = countryList;
    }
}
