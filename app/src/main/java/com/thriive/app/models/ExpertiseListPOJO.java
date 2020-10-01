package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpertiseListPOJO {

    @SerializedName("expertise_id")
    private Integer expertiseId;
    @SerializedName("expertise_name")
    private String expertiseName;
    @SerializedName("deleted")
    private Boolean deleted;
    @SerializedName("rowcode")
    private String rowcode;

    public Integer getExpertiseId() {
        return expertiseId;
    }

    public void setExpertiseId(Integer expertiseId) {
        this.expertiseId = expertiseId;
    }

    public String getExpertiseName() {
        return expertiseName;
    }

    public void setExpertiseName(String expertiseName) {
        this.expertiseName = expertiseName;
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
