package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryListPOJO {
    @SerializedName("country_id")
    private Integer countryId;
    @SerializedName("country_name")
    private String countryName;
    @SerializedName("deleted")
    private Boolean deleted;
    @SerializedName("rowcode")
    private String rowcode;

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getRowcode() {
        return rowcode;
    }

    public void setRowcode(String rowcode) {
        this.rowcode = rowcode;
    }
}
