package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaDataPOJO {
    @SerializedName("IsOK")
    @Expose
    private Boolean isOK;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("mr_params")
    @Expose

    private MrParams mrParams;



    public class MrParams {

        @SerializedName("requestor_id")
        @Expose
        private Integer requestorId;
        @SerializedName("requestor_name")
        @Expose
        private String requestorName;
        @SerializedName("requestor_persona_id")
        @Expose
        private Integer requestorPersonaId;
        @SerializedName("requestor_persona_name")
        @Expose
        private String requestorPersonaName;
        @SerializedName("reason_list")
        @Expose
        private Object reasonList;
        @SerializedName("reason_name")
        @Expose
        private Object reasonName;
        @SerializedName("persona_list")
        @Expose
        private Object personaList;
        @SerializedName("giver_persona_name")
        @Expose
        private Object giverPersonaName;
        @SerializedName("domain_list")
        @Expose
        private Object domainList;
        @SerializedName("country_list")
        @Expose
        private Object countryList;
        @SerializedName("flag_domain")
        @Expose
        private Boolean flagDomain;
        @SerializedName("flag_keywords")
        @Expose
        private Boolean flagKeywords;
        @SerializedName("keywords_list")
        @Expose
        private Object keywordsList;
        @SerializedName("expertise_list")
        @Expose
        private Object expertiseList;
        @SerializedName("flag_expertise")
        @Expose
        private Boolean flagExpertise;
        @SerializedName("giver_persona_id")
        @Expose
        private Integer giverPersonaId;
        @SerializedName("reason_id")
        @Expose
        private Integer reasonId;
        @SerializedName("sel_domain_id")
        @Expose
        private Integer selDomainId;
        @SerializedName("sel_sub_domain_id")
        @Expose
        private Integer selSubDomainId;
        @SerializedName("sel_expertise_id")
        @Expose
        private Integer selExpertiseId;
        @SerializedName("sel_country_id")
        @Expose
        private Integer selCountryId;
    }

}
