package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonMetaListPOJO {

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
        private String requestorName;
        @SerializedName("requestor_persona_id")
        private Integer requestorPersonaId;
        @SerializedName("requestor_persona_name")
        private String requestorPersonaName;
        @SerializedName("reason_list")
        private Object reasonList;
        @SerializedName("reason_id")
        private Integer reasonId;
        @SerializedName("reason_name")
        private Object reasonName;
        @SerializedName("persona_list")
        private Object personaList;
        @SerializedName("giver_persona_id")
        private Integer giverPersonaId;
        @SerializedName("giver_persona_name")
        private Object giverPersonaName;
        @SerializedName("domain_list")
        private List<DomainList> domainList = null;
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


        public Integer getRequestorId() {
            return requestorId;
        }

        public void setRequestorId(Integer requestorId) {
            this.requestorId = requestorId;
        }

        public String getRequestorName() {
            return requestorName;
        }

        public void setRequestorName(String requestorName) {
            this.requestorName = requestorName;
        }

        public Integer getRequestorPersonaId() {
            return requestorPersonaId;
        }

        public void setRequestorPersonaId(Integer requestorPersonaId) {
            this.requestorPersonaId = requestorPersonaId;
        }

        public String getRequestorPersonaName() {
            return requestorPersonaName;
        }

        public void setRequestorPersonaName(String requestorPersonaName) {
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

        public Object getPersonaList() {
            return personaList;
        }

        public void setPersonaList(Object personaList) {
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

        public List<DomainList> getDomainList() {
            return domainList;
        }

        public void setDomainList(List<DomainList> domainList) {
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

    public class DomainList {


        @SerializedName("domain_id")
        private Integer domainId;
        @SerializedName("domain_name")
        private String domainName;
        @SerializedName("sub_domain_list")
        private List<SubDomainList> subDomainList = null;

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

        public List<SubDomainList> getSubDomainList() {
            return subDomainList;
        }

        public void setSubDomainList(List<SubDomainList> subDomainList) {
            this.subDomainList = subDomainList;
        }
    }

    public class SubDomainList {

        @SerializedName("sub_domain_id")
        private Integer subDomainId;
        @SerializedName("sub_domain_name")
        private String subDomainName;
        @SerializedName("domain_id")
        private Integer domainId;

        public Integer getSubDomainId() {
            return subDomainId;
        }

        public void setSubDomainId(Integer subDomainId) {
            this.subDomainId = subDomainId;
        }

        public String getSubDomainName() {
            return subDomainName;
        }

        public void setSubDomainName(String subDomainName) {
            this.subDomainName = subDomainName;
        }

        public Integer getDomainId() {
            return domainId;
        }

        public void setDomainId(Integer domainId) {
            this.domainId = domainId;
        }
    }
}
