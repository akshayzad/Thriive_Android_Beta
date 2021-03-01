package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

public class SelMeetingPOJO {

    @SerializedName("meeting_id")
    private int meeting_id;

    @SerializedName("meeting_code")
    private String meeting_code;

    @SerializedName("requestor_id")
    private int requestor_id;

    @SerializedName("giver_id")
    private int giver_id;

    @SerializedName("requestor_persona_id")
    private int requestor_persona_id;

    @SerializedName("giver_persona_id")
    private int giver_persona_id;

    @SerializedName("reason_id")
    private int reason_id;

    @SerializedName("l1_attrib_id")
    private int l1_attrib_id;

    @SerializedName("l2_attrib_id")
    private int l2_attrib_id;

    @SerializedName("domain_id")
    private int domain_id;

    @SerializedName("sub_domain_id")
    private int sub_domain_id;

    @SerializedName("expertise_id")
    private int expertise_id;

    @SerializedName("country_id")
    private int country_id;

    @SerializedName("req_text")
    private String req_text;

    @SerializedName("requestor_name")
    private String requestor_name;

    @SerializedName("giver_name")
    private String giver_name;

    @SerializedName("requestor_persona_name")
    private String requestor_persona_name;

    @SerializedName("giver_persona_name")
    private String giver_persona_name;

    @SerializedName("reason_name")
    private String reason_name;

    @SerializedName("l1_attrib_name")
    private String l1_attrib_name;

    @SerializedName("l2_attrib_name")
    private String l2_attrib_name;

    @SerializedName("domain_name")
    private String domain_name;

    @SerializedName("sub_domain_name")
    private String sub_domain_name;

    @SerializedName("expertise_name")
    private String expertise_name;

    @SerializedName("country_name")
    private String country_name;

    @SerializedName("request_date")
    private String request_date;

    @SerializedName("flag_matched")
    private boolean flag_matched;

    @SerializedName("flag_rejected")
    private boolean flag_rejected;

    @SerializedName("flag_accepted")
    private boolean flag_accepted;

    @SerializedName("flag_expired")
    private boolean flag_expired;

    @SerializedName("flag_giver_prop_time")
    private boolean flag_giver_prop_time;

    @SerializedName("flag_req_accept_prop_time")
    private boolean flag_req_accept_prop_time;

    @SerializedName("flag_req_prop_expired")
    private boolean flag_req_prop_expired;

    @SerializedName("flag_rescheduled")
    private boolean flag_rescheduled;

    @SerializedName("flag_cancelled")
    private boolean flag_cancelled;

    @SerializedName("flag_done")
    private boolean flag_done;

    @SerializedName("cancel_reason")
    private String cancel_reason;

    @SerializedName("date_matched")
    private String date_matched;

    public String getDate_matched() {
        return date_matched;
    }

    public void setDate_matched(String date_matched) {
        this.date_matched = date_matched;
    }

    public int getRequestor_id() {
        return requestor_id;
    }

    public void setRequestor_id(int requestor_id) {
        this.requestor_id = requestor_id;
    }

    public int getGiver_id() {
        return giver_id;
    }

    public void setGiver_id(int giver_id) {
        this.giver_id = giver_id;
    }

    public int getRequestor_persona_id() {
        return requestor_persona_id;
    }

    public void setRequestor_persona_id(int requestor_persona_id) {
        this.requestor_persona_id = requestor_persona_id;
    }

    public int getGiver_persona_id() {
        return giver_persona_id;
    }

    public void setGiver_persona_id(int giver_persona_id) {
        this.giver_persona_id = giver_persona_id;
    }

    public int getReason_id() {
        return reason_id;
    }

    public void setReason_id(int reason_id) {
        this.reason_id = reason_id;
    }

    public int getL1_attrib_id() {
        return l1_attrib_id;
    }

    public void setL1_attrib_id(int l1_attrib_id) {
        this.l1_attrib_id = l1_attrib_id;
    }

    public int getL2_attrib_id() {
        return l2_attrib_id;
    }

    public void setL2_attrib_id(int l2_attrib_id) {
        this.l2_attrib_id = l2_attrib_id;
    }

    public int getDomain_id() {
        return domain_id;
    }

    public void setDomain_id(int domain_id) {
        this.domain_id = domain_id;
    }

    public int getSub_domain_id() {
        return sub_domain_id;
    }

    public void setSub_domain_id(int sub_domain_id) {
        this.sub_domain_id = sub_domain_id;
    }

    public int getExpertise_id() {
        return expertise_id;
    }

    public void setExpertise_id(int expertise_id) {
        this.expertise_id = expertise_id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getRequestor_name() {
        return requestor_name;
    }

    public void setRequestor_name(String requestor_name) {
        this.requestor_name = requestor_name;
    }

    public String getGiver_name() {
        return giver_name;
    }

    public void setGiver_name(String giver_name) {
        this.giver_name = giver_name;
    }

    public String getRequestor_persona_name() {
        return requestor_persona_name;
    }

    public void setRequestor_persona_name(String requestor_persona_name) {
        this.requestor_persona_name = requestor_persona_name;
    }

    public String getGiver_persona_name() {
        return giver_persona_name;
    }

    public void setGiver_persona_name(String giver_persona_name) {
        this.giver_persona_name = giver_persona_name;
    }

    public String getReason_name() {
        return reason_name;
    }

    public void setReason_name(String reason_name) {
        this.reason_name = reason_name;
    }

    public String getL1_attrib_name() {
        return l1_attrib_name;
    }

    public void setL1_attrib_name(String l1_attrib_name) {
        this.l1_attrib_name = l1_attrib_name;
    }

    public String getL2_attrib_name() {
        return l2_attrib_name;
    }

    public void setL2_attrib_name(String l2_attrib_name) {
        this.l2_attrib_name = l2_attrib_name;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public String getSub_domain_name() {
        return sub_domain_name;
    }

    public void setSub_domain_name(String sub_domain_name) {
        this.sub_domain_name = sub_domain_name;
    }

    public String getExpertise_name() {
        return expertise_name;
    }

    public void setExpertise_name(String expertise_name) {
        this.expertise_name = expertise_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    public boolean isFlag_matched() {
        return flag_matched;
    }

    public void setFlag_matched(boolean flag_matched) {
        this.flag_matched = flag_matched;
    }

    public boolean isFlag_rejected() {
        return flag_rejected;
    }

    public void setFlag_rejected(boolean flag_rejected) {
        this.flag_rejected = flag_rejected;
    }

    public boolean isFlag_accepted() {
        return flag_accepted;
    }

    public void setFlag_accepted(boolean flag_accepted) {
        this.flag_accepted = flag_accepted;
    }

    public boolean isFlag_expired() {
        return flag_expired;
    }

    public void setFlag_expired(boolean flag_expired) {
        this.flag_expired = flag_expired;
    }

    public boolean isFlag_giver_prop_time() {
        return flag_giver_prop_time;
    }

    public void setFlag_giver_prop_time(boolean flag_giver_prop_time) {
        this.flag_giver_prop_time = flag_giver_prop_time;
    }

    public boolean isFlag_req_accept_prop_time() {
        return flag_req_accept_prop_time;
    }

    public void setFlag_req_accept_prop_time(boolean flag_req_accept_prop_time) {
        this.flag_req_accept_prop_time = flag_req_accept_prop_time;
    }

    public boolean isFlag_req_prop_expired() {
        return flag_req_prop_expired;
    }

    public void setFlag_req_prop_expired(boolean flag_req_prop_expired) {
        this.flag_req_prop_expired = flag_req_prop_expired;
    }

    public boolean isFlag_rescheduled() {
        return flag_rescheduled;
    }

    public void setFlag_rescheduled(boolean flag_rescheduled) {
        this.flag_rescheduled = flag_rescheduled;
    }

    public boolean isFlag_cancelled() {
        return flag_cancelled;
    }

    public void setFlag_cancelled(boolean flag_cancelled) {
        this.flag_cancelled = flag_cancelled;
    }

    public boolean isFlag_done() {
        return flag_done;
    }

    public void setFlag_done(boolean flag_done) {
        this.flag_done = flag_done;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getMeeting_code() {
        return meeting_code;
    }

    public void setMeeting_code(String meeting_code) {
        this.meeting_code = meeting_code;
    }

    public String getReq_text() {
        return req_text;
    }

    public void setReq_text(String req_text) {
        this.req_text = req_text;
    }
}
