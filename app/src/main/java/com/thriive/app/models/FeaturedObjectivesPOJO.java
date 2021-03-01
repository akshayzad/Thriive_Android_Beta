package com.thriive.app.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeaturedObjectivesPOJO {

    @SerializedName("IsOK")
    @Expose
    private Boolean isOK;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("obj_list")
    @Expose
    private List<ObjList> objList = null;

    public Boolean getIsOK() {
        return isOK;
    }

    public void setIsOK(Boolean isOK) {
        this.isOK = isOK;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ObjList> getObjList() {
        return objList;
    }

    public void setObjList(List<ObjList> objList) {
        this.objList = objList;
    }



    public class ObjList {

        @SerializedName("image_index")
        @Expose
        private Integer imageIndex;
        @SerializedName("feature_text")
        @Expose
        private String featureText;
        @SerializedName("reason_id")
        @Expose
        private Integer reasonId;
        @SerializedName("reason_name")
        @Expose
        private String reasonName;
        @SerializedName("l1_attrib_id")
        @Expose
        private Integer l1AttribId;
        @SerializedName("l1_attrib_name")
        @Expose
        private String l1AttribName;
        @SerializedName("giver_persona_id")
        @Expose
        private Integer giverPersonaId;
        @SerializedName("giver_persona_name")
        @Expose
        private String giverPersonaName;
        @SerializedName("domain_id")
        @Expose
        private Integer domainId;
        @SerializedName("domain_name")
        @Expose
        private String domainName;
        @SerializedName("sub_domain_id")
        @Expose
        private Integer subDomainId;
        @SerializedName("sub_domain_name")
        @Expose
        private String subDomainName;
        @SerializedName("expertise_id")
        @Expose
        private Integer expertiseId;
        @SerializedName("expertise_name")
        @Expose
        private String expertiseName;

        public Integer getImageIndex() {
            return imageIndex;
        }

        public void setImageIndex(Integer imageIndex) {
            this.imageIndex = imageIndex;
        }

        public String getFeatureText() {
            return featureText;
        }

        public void setFeatureText(String featureText) {
            this.featureText = featureText;
        }

        public Integer getReasonId() {
            return reasonId;
        }

        public void setReasonId(Integer reasonId) {
            this.reasonId = reasonId;
        }

        public String getReasonName() {
            return reasonName;
        }

        public void setReasonName(String reasonName) {
            this.reasonName = reasonName;
        }

        public Integer getL1AttribId() {
            return l1AttribId;
        }

        public void setL1AttribId(Integer l1AttribId) {
            this.l1AttribId = l1AttribId;
        }

        public String getL1AttribName() {
            return l1AttribName;
        }

        public void setL1AttribName(String l1AttribName) {
            this.l1AttribName = l1AttribName;
        }

        public Integer getGiverPersonaId() {
            return giverPersonaId;
        }

        public void setGiverPersonaId(Integer giverPersonaId) {
            this.giverPersonaId = giverPersonaId;
        }

        public String getGiverPersonaName() {
            return giverPersonaName;
        }

        public void setGiverPersonaName(String giverPersonaName) {
            this.giverPersonaName = giverPersonaName;
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

    }

}