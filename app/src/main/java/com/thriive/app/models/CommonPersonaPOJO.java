package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonPersonaPOJO {
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
        private Object reasonList;
        @SerializedName("reason_id")
        private Integer reasonId;
        @SerializedName("reason_name")
        private Object reasonName;
        @SerializedName("persona_list")
        private List<PersonaListPOJO> personaList = null;
        @SerializedName("giver_persona_id")
        private Integer giverPersonaId;
        @SerializedName("giver_persona_name")
        private Object giverPersonaName;
        @SerializedName("domain_list")
        private Object domainList;
        @SerializedName("country_list")
        private Object countryList;
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

        @SerializedName("persona_screen_title")
        private String persona_screen_title;

        public String getPersona_screen_title() {
            return persona_screen_title;
        }

        public void setPersona_screen_title(String persona_screen_title) {
            this.persona_screen_title = persona_screen_title;
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

        public Object getReasonList() {
            return reasonList;
        }

        public void setReasonList(Object reasonList) {
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

        public List<PersonaListPOJO> getPersonaList() {
            return personaList;
        }

        public void setPersonaList(List<PersonaListPOJO> personaList) {
            this.personaList = personaList;
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

        public Object getCountryList() {
            return countryList;
        }

        public void setCountryList(Object countryList) {
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
