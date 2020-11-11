package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExpertiseBodyPOJO {
    @SerializedName("rowcode")
    private String rowcode;
    @SerializedName("result")
    private List<CommonExpertisePOJO.ExpertiseList> result = null;

    public String getRowcode() {
        return rowcode;
    }

    public void setRowcode(String rowcode) {
        this.rowcode = rowcode;
    }

    public List<CommonExpertisePOJO.ExpertiseList> getResult() {
        return result;
    }

    public void setResult(List<CommonExpertisePOJO.ExpertiseList> result) {
        this.result = result;
    }
}
