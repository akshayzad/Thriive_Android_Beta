package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjectiveBodyPOJO {
    @SerializedName("rowcode")
    private String rowcode;
    @SerializedName("result")
    private List<CommonObjectivesPOJO.ObjectivesList> result = null;

    public String getRowcode() {
        return rowcode;
    }

    public void setRowcode(String rowcode) {
        this.rowcode = rowcode;
    }

    public List<CommonObjectivesPOJO.ObjectivesList> getResult() {
        return result;
    }

    public void setResult(List<CommonObjectivesPOJO.ObjectivesList> result) {
        this.result = result;
    }
}
