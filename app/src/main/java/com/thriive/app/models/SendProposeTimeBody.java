package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendProposeTimeBody {

    @SerializedName("meeting_code")
    private String meeting_code;

    @SerializedName("rowcode")
    private String rowcode;

    @SerializedName("flag_giver_prop_time")
    private boolean flag_giver_prop_time;

    @SerializedName("sel_meeting_slot_id")
    private int sel_meeting_slot_id;

    @SerializedName("slot_list")
    private List<CommonRequestTimeSlots.EntitySlotsListPOJO> slotsListPOJOS = null;

    public String getMeeting_code() {
        return meeting_code;
    }

    public void setMeeting_code(String meeting_code) {
        this.meeting_code = meeting_code;
    }

    public String getRowcode() {
        return rowcode;
    }

    public void setRowcode(String rowcode) {
        this.rowcode = rowcode;
    }

    public boolean isFlag_giver_prop_time() {
        return flag_giver_prop_time;
    }

    public void setFlag_giver_prop_time(boolean flag_giver_prop_time) {
        this.flag_giver_prop_time = flag_giver_prop_time;
    }

    public int getSel_meeting_slot_id() {
        return sel_meeting_slot_id;
    }

    public void setSel_meeting_slot_id(int sel_meeting_slot_id) {
        this.sel_meeting_slot_id = sel_meeting_slot_id;
    }

    public List<CommonRequestTimeSlots.EntitySlotsListPOJO> getSlotsListPOJOS() {
        return slotsListPOJOS;
    }

    public void setSlotsListPOJOS(List<CommonRequestTimeSlots.EntitySlotsListPOJO> slotsListPOJOS) {
        this.slotsListPOJOS = slotsListPOJOS;
    }
}
