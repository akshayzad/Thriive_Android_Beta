package com.thriive.app.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginPOJO {

    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("return_entity")
    private ReturnEntity returnEntity;

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

    public ReturnEntity getReturnEntity() {
        return returnEntity;
    }

    public void setReturnEntity(ReturnEntity returnEntity) {
        this.returnEntity = returnEntity;
    }

    public class ReturnEntity {
        @SerializedName("entity_persona_list")
        private List<EntityPersonaList> entityPersonaList = null;
        @SerializedName("entity_id")
        private Integer entityId;
        @SerializedName("entity_name")
        private String entityName;
        @SerializedName("flag_company")
        private Boolean flagCompany;
        @SerializedName("email_id")
        private String emailId;
        @SerializedName("phone")
        private String phone;
        @SerializedName("entity_password")
        private String entityPassword;
        @SerializedName("parent_company_id")
        private Integer parentCompanyId;
        @SerializedName("parent_company_name")
        private String parentCompanyName;
        @SerializedName("country_id")
        private Integer countryId;
        @SerializedName("country_name")
        private String countryName;
        @SerializedName("city_id")
        private Integer cityId;
        @SerializedName("city_name")
        private String cityName;
        @SerializedName("linkedin_url")
        private String linkedinUrl;
        @SerializedName("google_url")
        private String googleUrl;
        @SerializedName("linkedin_login_key")
        private String linkedinLoginKey;
        @SerializedName("google_login_key")
        private String googleLoginKey;
        @SerializedName("login_method")
        private String loginMethod;
        @SerializedName("pic_url")
        private String picUrl;
        @SerializedName("active_token")
        private String activeToken;
        @SerializedName("app_ver")
        private String appVer;
        @SerializedName("app_platform")
        private String appPlatform;
        @SerializedName("flag_invited")
        private Boolean flagInvited;
        @SerializedName("flag_signedup")
        private Boolean flagSignedup;
        @SerializedName("date_invited")
        private String dateInvited;
        @SerializedName("date_signedup")
        private String dateSignedup;
        @SerializedName("invited_by")
        private String invitedBy;
        @SerializedName("verified_by")
        private String verifiedBy;
        @SerializedName("req_persona_id")
        private Integer reqPersonaId;
        @SerializedName("req_persona_name")
        private String reqPersonaName;
        @SerializedName("deleted")
        private Boolean deleted;
        @SerializedName("rowcode")
        private String rowcode;
        @SerializedName("primary_login_key")
        private String primaryLoginKey;
        @SerializedName("first_name")
        private String firstName;
        @SerializedName("last_name")
        private String lastName;
        @SerializedName("invite_code")
        private String inviteCode;
        @SerializedName("oauth_token")
        private String oauthToken;
        @SerializedName("designation_id")
        private Integer designationId;
        @SerializedName("designation_name")
        private String designationName;
        @SerializedName("push_token")
        private String pushToken;
        @SerializedName("platform_ver")
        private String platformVer;
        @SerializedName("voip_token")
        private String voipToken;


        public List<EntityPersonaList> getEntityPersonaList() {
            return entityPersonaList;
        }

        public void setEntityPersonaList(List<EntityPersonaList> entityPersonaList) {
            this.entityPersonaList = entityPersonaList;
        }

        public Integer getEntityId() {
            return entityId;
        }

        public void setEntityId(Integer entityId) {
            this.entityId = entityId;
        }

        public String getEntityName() {
            return entityName;
        }

        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }

        public Boolean getFlagCompany() {
            return flagCompany;
        }

        public void setFlagCompany(Boolean flagCompany) {
            this.flagCompany = flagCompany;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEntityPassword() {
            return entityPassword;
        }

        public void setEntityPassword(String entityPassword) {
            this.entityPassword = entityPassword;
        }

        public Integer getParentCompanyId() {
            return parentCompanyId;
        }

        public void setParentCompanyId(Integer parentCompanyId) {
            this.parentCompanyId = parentCompanyId;
        }

        public String getParentCompanyName() {
            return parentCompanyName;
        }

        public void setParentCompanyName(String parentCompanyName) {
            this.parentCompanyName = parentCompanyName;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getLinkedinUrl() {
            return linkedinUrl;
        }

        public void setLinkedinUrl(String linkedinUrl) {
            this.linkedinUrl = linkedinUrl;
        }

        public String getGoogleUrl() {
            return googleUrl;
        }

        public void setGoogleUrl(String googleUrl) {
            this.googleUrl = googleUrl;
        }

        public String getLinkedinLoginKey() {
            return linkedinLoginKey;
        }

        public void setLinkedinLoginKey(String linkedinLoginKey) {
            this.linkedinLoginKey = linkedinLoginKey;
        }

        public String getGoogleLoginKey() {
            return googleLoginKey;
        }

        public void setGoogleLoginKey(String googleLoginKey) {
            this.googleLoginKey = googleLoginKey;
        }

        public String getLoginMethod() {
            return loginMethod;
        }

        public void setLoginMethod(String loginMethod) {
            this.loginMethod = loginMethod;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getActiveToken() {
            return activeToken;
        }

        public void setActiveToken(String activeToken) {
            this.activeToken = activeToken;
        }

        public String getAppVer() {
            return appVer;
        }

        public void setAppVer(String appVer) {
            this.appVer = appVer;
        }

        public String getAppPlatform() {
            return appPlatform;
        }

        public void setAppPlatform(String appPlatform) {
            this.appPlatform = appPlatform;
        }

        public Boolean getFlagInvited() {
            return flagInvited;
        }

        public void setFlagInvited(Boolean flagInvited) {
            this.flagInvited = flagInvited;
        }

        public Boolean getFlagSignedup() {
            return flagSignedup;
        }

        public void setFlagSignedup(Boolean flagSignedup) {
            this.flagSignedup = flagSignedup;
        }

        public String getDateInvited() {
            return dateInvited;
        }

        public void setDateInvited(String dateInvited) {
            this.dateInvited = dateInvited;
        }

        public String getDateSignedup() {
            return dateSignedup;
        }

        public void setDateSignedup(String dateSignedup) {
            this.dateSignedup = dateSignedup;
        }

        public String getInvitedBy() {
            return invitedBy;
        }

        public void setInvitedBy(String invitedBy) {
            this.invitedBy = invitedBy;
        }

        public String getVerifiedBy() {
            return verifiedBy;
        }

        public void setVerifiedBy(String verifiedBy) {
            this.verifiedBy = verifiedBy;
        }

        public Integer getReqPersonaId() {
            return reqPersonaId;
        }

        public void setReqPersonaId(Integer reqPersonaId) {
            this.reqPersonaId = reqPersonaId;
        }

        public String getReqPersonaName() {
            return reqPersonaName;
        }

        public void setReqPersonaName(String reqPersonaName) {
            this.reqPersonaName = reqPersonaName;
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

        public String getPrimaryLoginKey() {
            return primaryLoginKey;
        }

        public void setPrimaryLoginKey(String primaryLoginKey) {
            this.primaryLoginKey = primaryLoginKey;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public String getOauthToken() {
            return oauthToken;
        }

        public void setOauthToken(String oauthToken) {
            this.oauthToken = oauthToken;
        }

        public Integer getDesignationId() {
            return designationId;
        }

        public void setDesignationId(Integer designationId) {
            this.designationId = designationId;
        }

        public String getDesignationName() {
            return designationName;
        }

        public void setDesignationName(String designationName) {
            this.designationName = designationName;
        }

        public String getPushToken() {
            return pushToken;
        }

        public void setPushToken(String pushToken) {
            this.pushToken = pushToken;
        }

        public String getPlatformVer() {
            return platformVer;
        }

        public void setPlatformVer(String platformVer) {
            this.platformVer = platformVer;
        }

        public String getVoipToken() {
            return voipToken;
        }

        public void setVoipToken(String voipToken) {
            this.voipToken = voipToken;
        }
    }

    public class EntityPersonaList {
        @SerializedName("entity_persona_id")
        private Integer entityPersonaId;
        @SerializedName("entity_persona_name")
        private String entityPersonaName;
        @SerializedName("entity_id")
        private Integer entityId;
        @SerializedName("persona_id")
        private Integer personaId;
        @SerializedName("designation_id")
        private Integer designationId;
        @SerializedName("entity_name")
        private String entityName;
        @SerializedName("persona_name")
        private String personaName;
        @SerializedName("designation_name")
        private String designationName;
        @SerializedName("flag_requestor_persona")
        private Boolean flagRequestorPersona;
        @SerializedName("v_exp_years")
        private String vExpYears;
        @SerializedName("v_funds_raised")
        private Double vFundsRaised;
        @SerializedName("v_company_size")
        private Double vCompanySize;
        @SerializedName("v_company_rev")
        private Double vCompanyRev;
        @SerializedName("v_people_count")
        private Double vPeopleCount;
        @SerializedName("v_investor_type")
        private Double vInvestorType;
        @SerializedName("v_designation")
        private Double vDesignation;
        @SerializedName("deleted")
        private Boolean deleted;
        @SerializedName("rowcode")
        private String rowcode;

        public Integer getEntityPersonaId() {
            return entityPersonaId;
        }

        public void setEntityPersonaId(Integer entityPersonaId) {
            this.entityPersonaId = entityPersonaId;
        }

        public String getEntityPersonaName() {
            return entityPersonaName;
        }

        public void setEntityPersonaName(String entityPersonaName) {
            this.entityPersonaName = entityPersonaName;
        }

        public Integer getEntityId() {
            return entityId;
        }

        public void setEntityId(Integer entityId) {
            this.entityId = entityId;
        }

        public Integer getPersonaId() {
            return personaId;
        }

        public void setPersonaId(Integer personaId) {
            this.personaId = personaId;
        }

        public Integer getDesignationId() {
            return designationId;
        }

        public void setDesignationId(Integer designationId) {
            this.designationId = designationId;
        }

        public String getEntityName() {
            return entityName;
        }

        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }

        public String getPersonaName() {
            return personaName;
        }

        public void setPersonaName(String personaName) {
            this.personaName = personaName;
        }

        public String getDesignationName() {
            return designationName;
        }

        public void setDesignationName(String designationName) {
            this.designationName = designationName;
        }

        public Boolean getFlagRequestorPersona() {
            return flagRequestorPersona;
        }

        public void setFlagRequestorPersona(Boolean flagRequestorPersona) {
            this.flagRequestorPersona = flagRequestorPersona;
        }

        public String getvExpYears() {
            return vExpYears;
        }

        public void setvExpYears(String vExpYears) {
            this.vExpYears = vExpYears;
        }

        public Double getvFundsRaised() {
            return vFundsRaised;
        }

        public void setvFundsRaised(Double vFundsRaised) {
            this.vFundsRaised = vFundsRaised;
        }

        public Double getvCompanySize() {
            return vCompanySize;
        }

        public void setvCompanySize(Double vCompanySize) {
            this.vCompanySize = vCompanySize;
        }

        public Double getvCompanyRev() {
            return vCompanyRev;
        }

        public void setvCompanyRev(Double vCompanyRev) {
            this.vCompanyRev = vCompanyRev;
        }

        public Double getvPeopleCount() {
            return vPeopleCount;
        }

        public void setvPeopleCount(Double vPeopleCount) {
            this.vPeopleCount = vPeopleCount;
        }

        public Double getvInvestorType() {
            return vInvestorType;
        }

        public void setvInvestorType(Double vInvestorType) {
            this.vInvestorType = vInvestorType;
        }

        public Double getvDesignation() {
            return vDesignation;
        }

        public void setvDesignation(Double vDesignation) {
            this.vDesignation = vDesignation;
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
