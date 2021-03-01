package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

public class AttributeL2ListPOJO {

    @SerializedName("l2_attrib_id")
    private Integer l2_attrib_id;

    @SerializedName("l2_attrib_name")
    private String l2_attrib_name;

    @SerializedName("deleted")
    private boolean deleted;

    @SerializedName("rowcode")
    private String rowcode;

    public Integer getL2_attrib_id() {
        return l2_attrib_id;
    }

    public void setL2_attrib_id(Integer l2_attrib_id) {
        this.l2_attrib_id = l2_attrib_id;
    }

    public String getL2_attrib_name() {
        return l2_attrib_name;
    }

    public void setL2_attrib_name(String l2_attrib_name) {
        this.l2_attrib_name = l2_attrib_name;
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
