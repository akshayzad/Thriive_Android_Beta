package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

public class MeetingDetailPOJO {

    @SerializedName("meeting_slot_id")
    private int meeting_slot_id;

    @SerializedName("meeting_id")
    private int meeting_id;

    @SerializedName("proposer_id")
    private int proposer_id;

    @SerializedName("proposer_name")
    private String proposer_name;

    @SerializedName("requestor_id")
    private int requestor_id;

    @SerializedName("requestor_name")
    private String requestor_name;

    @SerializedName("giver_id")
    private int giver_id;

    @SerializedName("giver_name")
    private String giver_name;

    @SerializedName("flag_requestor")
    private boolean flag_requestor;

    @SerializedName("flag_entry_by_support")
    private boolean flag_entry_by_support;

    @SerializedName("slot_from_date")
    private String slot_from_date;

    @SerializedName("slot_to_date")
    private String slot_to_date;

    @SerializedName("flag_slot_accepted")
    private boolean flag_slot_accepted;

    @SerializedName("flag_slot_rejected")
    private boolean flag_slot_rejected;

    @SerializedName("slot_day")
    private int slot_day;

    @SerializedName("flag_cancelled")
    private String flag_cancelled;

    @SerializedName("flag_rescheduled")
    private boolean flag_rescheduled;

    @SerializedName("deleted")
    private boolean deleted;

    @SerializedName("rowcode")
    private String rowcode;


    public int getMeeting_slot_id() {
        return meeting_slot_id;
    }

    public void setMeeting_slot_id(int meeting_slot_id) {
        this.meeting_slot_id = meeting_slot_id;
    }

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    public int getProposer_id() {
        return proposer_id;
    }

    public void setProposer_id(int proposer_id) {
        this.proposer_id = proposer_id;
    }

    public String getProposer_name() {
        return proposer_name;
    }

    public void setProposer_name(String proposer_name) {
        this.proposer_name = proposer_name;
    }

    public int getRequestor_id() {
        return requestor_id;
    }

    public void setRequestor_id(int requestor_id) {
        this.requestor_id = requestor_id;
    }

    public String getRequestor_name() {
        return requestor_name;
    }

    public void setRequestor_name(String requestor_name) {
        this.requestor_name = requestor_name;
    }

    public int getGiver_id() {
        return giver_id;
    }

    public void setGiver_id(int giver_id) {
        this.giver_id = giver_id;
    }

    public String getGiver_name() {
        return giver_name;
    }

    public void setGiver_name(String giver_name) {
        this.giver_name = giver_name;
    }

    public boolean isFlag_requestor() {
        return flag_requestor;
    }

    public void setFlag_requestor(boolean flag_requestor) {
        this.flag_requestor = flag_requestor;
    }

    public boolean isFlag_entry_by_support() {
        return flag_entry_by_support;
    }

    public void setFlag_entry_by_support(boolean flag_entry_by_support) {
        this.flag_entry_by_support = flag_entry_by_support;
    }

    public String getSlot_from_date() {
        return slot_from_date;
    }

    public void setSlot_from_date(String slot_from_date) {
        this.slot_from_date = slot_from_date;
    }

    public String getSlot_to_date() {
        return slot_to_date;
    }

    public void setSlot_to_date(String slot_to_date) {
        this.slot_to_date = slot_to_date;
    }

    public boolean isFlag_slot_accepted() {
        return flag_slot_accepted;
    }

    public void setFlag_slot_accepted(boolean flag_slot_accepted) {
        this.flag_slot_accepted = flag_slot_accepted;
    }

    public boolean isFlag_slot_rejected() {
        return flag_slot_rejected;
    }

    public void setFlag_slot_rejected(boolean flag_slot_rejected) {
        this.flag_slot_rejected = flag_slot_rejected;
    }

    public int getSlot_day() {
        return slot_day;
    }

    public void setSlot_day(int slot_day) {
        this.slot_day = slot_day;
    }

    public String getFlag_cancelled() {
        return flag_cancelled;
    }

    public void setFlag_cancelled(String flag_cancelled) {
        this.flag_cancelled = flag_cancelled;
    }

    public boolean isFlag_rescheduled() {
        return flag_rescheduled;
    }

    public void setFlag_rescheduled(boolean flag_rescheduled) {
        this.flag_rescheduled = flag_rescheduled;
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
