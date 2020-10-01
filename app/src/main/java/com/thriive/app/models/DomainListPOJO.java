package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DomainListPOJO {

    @SerializedName("domain_id")
    private Integer domainId;
    @SerializedName("domain_name")
    private String domainName;
    @SerializedName("sub_domain_list")
    private List<SubDomainListPOJO> subDomainList = null;

    private boolean isSelect = false;

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

    public List<SubDomainListPOJO> getSubDomainList() {
        return subDomainList;
    }

    public void setSubDomainList(List<SubDomainListPOJO> subDomainList) {
        this.subDomainList = subDomainList;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
