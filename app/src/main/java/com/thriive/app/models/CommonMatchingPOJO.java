package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonMatchingPOJO {

    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;

    @SerializedName("mr_id")
    private String mr_id;

    @SerializedName("entity_list")
    private List<EntityListPOJO> entityList = null;

    public String getMr_id() {
        return mr_id;
    }

    public void setMr_id(String mr_id) {
        this.mr_id = mr_id;
    }

    public List<EntityListPOJO> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<EntityListPOJO> entityList) {
        this.entityList = entityList;
    }

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


    public class EntityListPOJO {

        @SerializedName("entity_name")
        private String entity_name;

        @SerializedName("designation_name")
        private String designation_name;

        @SerializedName("company_name")
        private String company_name;

        @SerializedName("pic_url")
        private String pic_url;

        @SerializedName("expertise_list")
        private List<String> expertiseList = null;

        @SerializedName("domain_list")
        private List<String> domainList = null;


        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getEntity_name() {
            return entity_name;
        }

        public void setEntity_name(String entity_name) {
            this.entity_name = entity_name;
        }

        public String getDesignation_name() {
            return designation_name;
        }

        public void setDesignation_name(String designation_name) {
            this.designation_name = designation_name;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public List<String> getExpertiseList() {
            return expertiseList;
        }

        public void setExpertiseList(List<String> expertiseList) {
            this.expertiseList = expertiseList;
        }

        public List<String> getDomainList() {
            return domainList;
        }

        public void setDomainList(List<String> domainList) {
            this.domainList = domainList;
        }
    }


}
