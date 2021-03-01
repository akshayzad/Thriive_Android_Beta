package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.http.Field;

public class MeetingSlotBodyPOJO {

    @SerializedName("requestor_id")
    private int requestor_id;

    @SerializedName("reason_id")
    private int reason_id;

    @SerializedName("giver_persona_id")
    private String giver_persona_id;

    @SerializedName("sel_domain_id")
    private String sel_domain_id;

    @SerializedName("sel_sub_domain_id")
    private String sel_sub_domain_id;

    @SerializedName("sel_expertise_id")
    private String sel_expertise_id;


    @SerializedName("sel_country_id")
    private String sel_country_id;

    @SerializedName("l2_attrib_id")
    private String l2_attrib_id;


    @SerializedName("l1_attrib_id")
    private String l1_attrib_id;


    @SerializedName("req_text")
    private String req_text;


    @SerializedName("slot_list")
    private List<CommonRequestTimeSlots.EntitySlotsListPOJO> slotsListPOJOS = null;


    public List<CommonRequestTimeSlots.EntitySlotsListPOJO> getSlotsListPOJOS() {
        return slotsListPOJOS;
    }

    public void setSlotsListPOJOS(List<CommonRequestTimeSlots.EntitySlotsListPOJO> slotsListPOJOS) {
        this.slotsListPOJOS = slotsListPOJOS;
    }

    public int getRequestor_id() {
        return requestor_id;
    }

    public void setRequestor_id(int requestor_id) {
        this.requestor_id = requestor_id;
    }

    public int getReason_id() {
        return reason_id;
    }

    public void setReason_id(int reason_id) {
        this.reason_id = reason_id;
    }

    public String getGiver_persona_id() {
        return giver_persona_id;
    }

    public void setGiver_persona_id(String giver_persona_id) {
        this.giver_persona_id = giver_persona_id;
    }

    public String getSel_domain_id() {
        return sel_domain_id;
    }

    public void setSel_domain_id(String sel_domain_id) {
        this.sel_domain_id = sel_domain_id;
    }

    public String getSel_sub_domain_id() {
        return sel_sub_domain_id;
    }

    public void setSel_sub_domain_id(String sel_sub_domain_id) {
        this.sel_sub_domain_id = sel_sub_domain_id;
    }

    public String getSel_expertise_id() {
        return sel_expertise_id;
    }

    public void setSel_expertise_id(String sel_expertise_id) {
        this.sel_expertise_id = sel_expertise_id;
    }

    public String getSel_country_id() {
        return sel_country_id;
    }

    public void setSel_country_id(String sel_country_id) {
        this.sel_country_id = sel_country_id;
    }

    public String getL2_attrib_id() {
        return l2_attrib_id;
    }

    public void setL2_attrib_id(String l2_attrib_id) {
        this.l2_attrib_id = l2_attrib_id;
    }

    public String getL1_attrib_id() {
        return l1_attrib_id;
    }

    public void setL1_attrib_id(String l1_attrib_id) {
        this.l1_attrib_id = l1_attrib_id;
    }

    public String getReq_text() {
        return req_text;
    }

    public void setReq_text(String req_text) {
        this.req_text = req_text;
    }
}
