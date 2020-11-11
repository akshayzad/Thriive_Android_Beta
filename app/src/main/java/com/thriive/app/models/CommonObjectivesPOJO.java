package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonObjectivesPOJO {
    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("result")
    private List<ObjectivesList> result = null;

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

    public List<ObjectivesList> getResult() {
        return result;
    }

    public void setResult(List<ObjectivesList> result) {
        this.result = result;
    }

    public class ObjectivesList {
        @SerializedName("IsSelected")
        private Boolean isSelected;
        @SerializedName("objective_id")
        private Integer objectiveId;
        @SerializedName("objective_name")
        private String objectiveName;
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

        public Integer getObjectiveId() {
            return objectiveId;
        }

        public void setObjectiveId(Integer objectiveId) {
            this.objectiveId = objectiveId;
        }

        public String getObjectiveName() {
            return objectiveName;
        }

        public void setObjectiveName(String objectiveName) {
            this.objectiveName = objectiveName;
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
