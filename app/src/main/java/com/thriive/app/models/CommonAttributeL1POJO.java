package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonAttributeL1POJO {

    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("mr_params")
    private MrParams mrParams;

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

    public MrParams getMrParams() {
        return mrParams;
    }

    public void setMrParams(MrParams mrParams) {
        this.mrParams = mrParams;
    }

    public class MrParams {

        @SerializedName("requestor_id")
        private Integer requestorId;
        @SerializedName("requestor_name")
        private Object requestorName;
        @SerializedName("requestor_persona_id")
        private Integer requestorPersonaId;
        @SerializedName("requestor_persona_name")
        private Object requestorPersonaName;
        @SerializedName("reason_list")
        private List<ReasonListPOJO> reasonList = null;
        @SerializedName("reason_id")
        private Integer reasonId;
        @SerializedName("reason_name")
        private Object reasonName;
        @SerializedName("l1_attrib_list")
        private List<AttributeL1ListPOJO> attributeL1ListPOJOS = null;
        @SerializedName("giver_persona_id")
        private Integer giverPersonaId;
        @SerializedName("giver_persona_name")
        private Object giverPersonaName;
        @SerializedName("domain_list")
        private Object domainList;
        @SerializedName("country_list")
        private List<CountryListPOJO> countryList = null;
        @SerializedName("flag_domain")
        private Boolean flagDomain;
        @SerializedName("flag_keywords")
        private Boolean flagKeywords;
        @SerializedName("keywords_list")
        private Object keywordsList;
        @SerializedName("expertise_list")
        private Object expertiseList;
        @SerializedName("flag_expertise")
        private Boolean flagExpertise;

        @SerializedName("l1_screen_title")
        private String l1_screen_title;

        public String getL1_screen_title() {
            return l1_screen_title;
        }

        public void setL1_screen_title(String l1_screen_title) {
            this.l1_screen_title = l1_screen_title;
        }

        public Integer getRequestorId() {
            return requestorId;
        }

        public void setRequestorId(Integer requestorId) {
            this.requestorId = requestorId;
        }

        public Object getRequestorName() {
            return requestorName;
        }

        public void setRequestorName(Object requestorName) {
            this.requestorName = requestorName;
        }

        public Integer getRequestorPersonaId() {
            return requestorPersonaId;
        }

        public void setRequestorPersonaId(Integer requestorPersonaId) {
            this.requestorPersonaId = requestorPersonaId;
        }

        public Object getRequestorPersonaName() {
            return requestorPersonaName;
        }

        public void setRequestorPersonaName(Object requestorPersonaName) {
            this.requestorPersonaName = requestorPersonaName;
        }

        public List<ReasonListPOJO> getReasonList() {
            return reasonList;
        }

        public void setReasonList(List<ReasonListPOJO> reasonList) {
            this.reasonList = reasonList;
        }

        public Integer getReasonId() {
            return reasonId;
        }

        public void setReasonId(Integer reasonId) {
            this.reasonId = reasonId;
        }

        public Object getReasonName() {
            return reasonName;
        }

        public void setReasonName(Object reasonName) {
            this.reasonName = reasonName;
        }


        public List<AttributeL1ListPOJO> getAttributeL1ListPOJOS() {
            return attributeL1ListPOJOS;
        }

        public void setAttributeL1ListPOJOS(List<AttributeL1ListPOJO> attributeL1ListPOJOS) {
            this.attributeL1ListPOJOS = attributeL1ListPOJOS;
        }

        public Integer getGiverPersonaId() {
            return giverPersonaId;
        }

        public void setGiverPersonaId(Integer giverPersonaId) {
            this.giverPersonaId = giverPersonaId;
        }

        public Object getGiverPersonaName() {
            return giverPersonaName;
        }

        public void setGiverPersonaName(Object giverPersonaName) {
            this.giverPersonaName = giverPersonaName;
        }

        public Object getDomainList() {
            return domainList;
        }

        public void setDomainList(Object domainList) {
            this.domainList = domainList;
        }

        public List<CountryListPOJO> getCountryList() {
            return countryList;
        }

        public void setCountryList(List<CountryListPOJO> countryList) {
            this.countryList = countryList;
        }

        public Boolean getFlagDomain() {
            return flagDomain;
        }

        public void setFlagDomain(Boolean flagDomain) {
            this.flagDomain = flagDomain;
        }

        public Boolean getFlagKeywords() {
            return flagKeywords;
        }

        public void setFlagKeywords(Boolean flagKeywords) {
            this.flagKeywords = flagKeywords;
        }

        public Object getKeywordsList() {
            return keywordsList;
        }

        public void setKeywordsList(Object keywordsList) {
            this.keywordsList = keywordsList;
        }

        public Object getExpertiseList() {
            return expertiseList;
        }

        public void setExpertiseList(Object expertiseList) {
            this.expertiseList = expertiseList;
        }

        public Boolean getFlagExpertise() {
            return flagExpertise;
        }

        public void setFlagExpertise(Boolean flagExpertise) {
            this.flagExpertise = flagExpertise;
        }
    }
}
