package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

public class AttributeL1ListPOJO {

    @SerializedName("l1_attrib_id")
    private Integer l1_attrib_id;

    @SerializedName("l1_attrib_name")
    private String l1_attrib_name;

    @SerializedName("HexCode")
    private String HexCode;

    @SerializedName("deleted")
    private boolean deleted;

    @SerializedName("rowcode")
    private String rowcode;

    public Integer getL1_attrib__id() {
        return l1_attrib_id;
    }

    public void setL1_attrib__id(Integer l1_attrib__id) {
        this.l1_attrib_id = l1_attrib__id;
    }

    public String getL1_attrib_name() {
        return l1_attrib_name;
    }

    public void setL1_attrib_name(String l1_attrib_name) {
        this.l1_attrib_name = l1_attrib_name;
    }

    public String getHexCode() {
        return HexCode;
    }

    public void setHexCode(String hexCode) {
        HexCode = hexCode;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getRowcode() {
        return rowcode;
    }

    public void setRowcode(String rowcode) {
        this.rowcode = rowcode;
    }
}
