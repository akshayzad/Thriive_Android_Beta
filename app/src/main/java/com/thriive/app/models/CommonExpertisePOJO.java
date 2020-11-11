package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonExpertisePOJO {
    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("result")
    private List<ExpertiseList> result = null;

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

    public List<ExpertiseList> getResult() {
        return result;
    }

    public void setResult(List<ExpertiseList> result) {
        this.result = result;
    }

    public class ExpertiseList {
        @SerializedName("IsSelected")
        private Boolean isSelected;
        @SerializedName("expertise_id")
        private Integer expertiseId;
        @SerializedName("expertise_name")
        private String expertiseName;
        @SerializedName("deleted")
        private Boolean deleted;
        @SerializedName("rowcode")
        private String rowcode;

        public Boolean getSelected() {
            return isSelected;
        }

        public void setSelected(Boolean selected) {
            isSelected = selected;
        }

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
}
