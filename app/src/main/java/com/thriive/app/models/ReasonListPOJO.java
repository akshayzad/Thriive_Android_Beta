package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReasonListPOJO {
    @SerializedName("reason_id")
    private Integer reasonId;
    @SerializedName("reason_name")
    private String reasonName;
    @SerializedName("deleted")
    private Boolean deleted;
    @SerializedName("rowcode")
    private String rowcode;

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
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
