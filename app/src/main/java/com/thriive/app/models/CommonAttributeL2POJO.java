package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonAttributeL2POJO {

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
        @SerializedName("l1_attrib_id")
        private Integer l1_attrib_id;
        @SerializedName("persona_id")
        private Integer persona_id;
        @SerializedName("l2_attrib_list")
        private List<AttributeL2ListPOJO> attributeL2ListPOJOS = null;
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

        @SerializedName("l2_screen_title")
        private String l2_screen_title;

        public String getL2_screen_title() {
            return l2_screen_title;
        }

        public void setL2_screen_title(String l2_screen_title) {
            this.l2_screen_title = l2_screen_title;
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

        public Integer getL1_attrib_id() {
            return l1_attrib_id;
        }

        public void setL1_attrib_id(Integer l1_attrib_id) {
            this.l1_attrib_id = l1_attrib_id;
        }

        public Integer getPersona_id() {
            return persona_id;
        }

        public void setPersona_id(Integer persona_id) {
            this.persona_id = persona_id;
        }

        public List<AttributeL2ListPOJO> getAttributeL2ListPOJOS() {
            return attributeL2ListPOJOS;
        }

        public void setAttributeL2ListPOJOS(List<AttributeL2ListPOJO> attributeL2ListPOJOS) {
            this.attributeL2ListPOJOS = attributeL2ListPOJOS;
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
