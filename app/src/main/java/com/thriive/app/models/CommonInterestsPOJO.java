package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonInterestsPOJO {
    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("result")
    private List<InterestsList> result = null;

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

    public List<InterestsList> getResult() {
        return result;
    }

    public void setResult(List<InterestsList> result) {
        this.result = result;
    }

    public class InterestsList {

        @SerializedName("sub_domain_list")
        private List<Object> subDomainList = null;
        @SerializedName("IsSelected")
        private Boolean isSelected;
        @SerializedName("domain_id")
        private Integer domainId;
        @SerializedName("domain_name")
        private String domainName;
        @SerializedName("deleted")
        private Boolean deleted;
        @SerializedName("rowcode")
        private String rowcode;

        public List<Object> getSubDomainList() {
            return subDomainList;
        }

        public void setSubDomainList(List<Object> subDomainList) {
            this.subDomainList = subDomainList;
        }

        public Boolean getSelected() {
            return isSelected;
        }

        public void setSelected(Boolean selected) {
            isSelected = selected;
        }

        public Integer getDomainId() {
            return domainId;
        }

        public void setDomainId(Integer domainId) {
            this.domainId = domainId;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
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
