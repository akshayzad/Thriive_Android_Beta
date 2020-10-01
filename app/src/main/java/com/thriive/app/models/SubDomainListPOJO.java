package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class
SubDomainListPOJO {
    @SerializedName("sub_domain_id")
    private Integer subDomainId;
    @SerializedName("sub_domain_name")
    private String subDomainName;
    @SerializedName("domain_id")
    private Integer domainId;

    private boolean isSelect = false;

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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
