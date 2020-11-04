package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonStartMeetingPOJO {

    @SerializedName("IsOK")
    @Expose
    private Boolean isOK;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("meeting_data")
    @Expose
    private MeetingDataPOJO meetingData;

    public Boolean getOK() {
        return isOK;
    }

    public void setOK(Boolean OK) {
        isOK = OK;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MeetingDataPOJO getMeetingData() {
        return meetingData;
    }

    public void setMeetingData(MeetingDataPOJO meetingData) {
        this.meetingData = meetingData;
    }

    public class MeetingDataPOJO {

        @SerializedName("meeting_id")
        @Expose
        private Integer meetingId;
        @SerializedName("meeting_code")
        @Expose
        private String meetingCode;
        @SerializedName("meeting_request_id")
        @Expose
        private Integer meetingRequestId;
        @SerializedName("giver_id")
        @Expose
        private Integer giverId;
        @SerializedName("giver_name")
        @Expose
        private String giverName;
        @SerializedName("giver_persona_id")
        @Expose
        private Integer giverPersonaId;
        @SerializedName("giver_persona_name")
        @Expose
        private String giverPersonaName;
        @SerializedName("date_match")
        @Expose
        private String dateMatch;
        @SerializedName("flag_accepted")
        @Expose
        private Boolean flagAccepted;
        @SerializedName("date_accepted")
        @Expose
        private String dateAccepted;
        @SerializedName("flag_rejected")
        @Expose
        private Boolean flagRejected;
        @SerializedName("date_rejected")
        @Expose
        private String dateRejected;
        @SerializedName("flag_expired")
        @Expose
        private Boolean flagExpired;
        @SerializedName("flag_cancelled")
        @Expose
        private Boolean flagCancelled;
        @SerializedName("date_cancelled")
        @Expose
        private String dateCancelled;
        @SerializedName("flag_rescheduled")
        @Expose
        private Boolean flagRescheduled;
        @SerializedName("date_when_rescheduled")
        @Expose
        private String dateWhenRescheduled;
        @SerializedName("plan_start_time")
        @Expose
        private String planStartTime;
        @SerializedName("plan_end_time")
        @Expose
        private String planEndTime;
        @SerializedName("actual_start_time")
        @Expose
        private String actualStartTime;
        @SerializedName("acutal_end_time")
        @Expose
        private String acutalEndTime;
        @SerializedName("meeting_channel")
        @Expose
        private String meetingChannel;
        @SerializedName("cancel_reason")
        @Expose
        private String cancelReason;
        @SerializedName("deleted")
        @Expose
        private Boolean deleted;
        @SerializedName("rowcode")
        @Expose
        private String rowcode;
        @SerializedName("flag_done")
        @Expose
        private Boolean flagDone;
        @SerializedName("meeting_token")
        @Expose
        private String meetingToken;

        @SerializedName("requestor_id")
        private Integer requestorId;
        @SerializedName("requestor_name")
        private String requestorName;

        public Integer getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(Integer meetingId) {
            this.meetingId = meetingId;
        }

        public String getMeetingCode() {
            return meetingCode;
        }

        public void setMeetingCode(String meetingCode) {
            this.meetingCode = meetingCode;
        }

        public Integer getMeetingRequestId() {
            return meetingRequestId;
        }

        public void setMeetingRequestId(Integer meetingRequestId) {
            this.meetingRequestId = meetingRequestId;
        }

        public Integer getGiverId() {
            return giverId;
        }

        public void setGiverId(Integer giverId) {
            this.giverId = giverId;
        }

        public String getGiverName() {
            return giverName;
        }

        public void setGiverName(String giverName) {
            this.giverName = giverName;
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

        public String getDateMatch() {
            return dateMatch;
        }

        public void setDateMatch(String dateMatch) {
            this.dateMatch = dateMatch;
        }

        public Boolean getFlagAccepted() {
            return flagAccepted;
        }

        public void setFlagAccepted(Boolean flagAccepted) {
            this.flagAccepted = flagAccepted;
        }

        public String getDateAccepted() {
            return dateAccepted;
        }

        public void setDateAccepted(String dateAccepted) {
            this.dateAccepted = dateAccepted;
        }

        public Boolean getFlagRejected() {
            return flagRejected;
        }

        public void setFlagRejected(Boolean flagRejected) {
            this.flagRejected = flagRejected;
        }

        public String getDateRejected() {
            return dateRejected;
        }

        public void setDateRejected(String dateRejected) {
            this.dateRejected = dateRejected;
        }

        public Boolean getFlagExpired() {
            return flagExpired;
        }

        public void setFlagExpired(Boolean flagExpired) {
            this.flagExpired = flagExpired;
        }

        public Boolean getFlagCancelled() {
            return flagCancelled;
        }

        public void setFlagCancelled(Boolean flagCancelled) {
            this.flagCancelled = flagCancelled;
        }

        public String getDateCancelled() {
            return dateCancelled;
        }

        public void setDateCancelled(String dateCancelled) {
            this.dateCancelled = dateCancelled;
        }

        public Boolean getFlagRescheduled() {
            return flagRescheduled;
        }

        public void setFlagRescheduled(Boolean flagRescheduled) {
            this.flagRescheduled = flagRescheduled;
        }

        public String getDateWhenRescheduled() {
            return dateWhenRescheduled;
        }

        public void setDateWhenRescheduled(String dateWhenRescheduled) {
            this.dateWhenRescheduled = dateWhenRescheduled;
        }

        public String getPlanStartTime() {
            return planStartTime;
        }

        public void setPlanStartTime(String planStartTime) {
            this.planStartTime = planStartTime;
        }

        public String getPlanEndTime() {
            return planEndTime;
        }

        public void setPlanEndTime(String planEndTime) {
            this.planEndTime = planEndTime;
        }

        public String getActualStartTime() {
            return actualStartTime;
        }

        public void setActualStartTime(String actualStartTime) {
            this.actualStartTime = actualStartTime;
        }

        public String getAcutalEndTime() {
            return acutalEndTime;
        }

        public void setAcutalEndTime(String acutalEndTime) {
            this.acutalEndTime = acutalEndTime;
        }

        public String getMeetingChannel() {
            return meetingChannel;
        }

        public void setMeetingChannel(String meetingChannel) {
            this.meetingChannel = meetingChannel;
        }

        public String getCancelReason() {
            return cancelReason;
        }

        public void setCancelReason(String cancelReason) {
            this.cancelReason = cancelReason;
        }

        public Boolean getDeleted() {
            return deleted;
        }

        public void setDeleted(Boolean deleted) {
            this.deleted = deleted;
        }

        public String getRowcode() {
            return rowcode;
        }

        public void setRowcode(String rowcode) {
            this.rowcode = rowcode;
        }

        public Boolean getFlagDone() {
            return flagDone;
        }

        public void setFlagDone(Boolean flagDone) {
            this.flagDone = flagDone;
        }

        public String getMeetingToken() {
            return meetingToken;
        }

        public void setMeetingToken(String meetingToken) {
            this.meetingToken = meetingToken;
        }

        public Integer getRequestorId() {
            return requestorId;
        }

        public void setRequestorId(Integer requestorId) {
            this.requestorId = requestorId;
        }

        public String getRequestorName() {
            return requestorName;
        }

        public void setRequestorName(String requestorName) {
            this.requestorName = requestorName;
        }
    }
}
