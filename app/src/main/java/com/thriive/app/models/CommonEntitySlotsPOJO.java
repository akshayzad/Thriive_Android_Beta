package com.thriive.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonEntitySlotsPOJO {
    @SerializedName("IsOK")
    @Expose
    private Boolean isOK;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("entity_slot_list")
    @Expose
    private List<EntitySlotList> entitySlotList = null;

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

    public List<EntitySlotList> getEntitySlotList() {
        return entitySlotList;
    }

    public void setEntitySlotList(List<EntitySlotList> entitySlotList) {
        this.entitySlotList = entitySlotList;
    }

    public class EntitySlotList {

        @SerializedName("slot_date")
        @Expose
        private String slotDate;
        @SerializedName("plan_start_time")
        @Expose
        private String planStartTime;
        @SerializedName("plan_end_time")
        @Expose
        private String planEndTime;
        @SerializedName("entity_slot_id")
        @Expose
        private Integer entitySlotId;
        @SerializedName("entity_slot_name")
        @Expose
        private String entitySlotName;
        @SerializedName("entity_id")
        @Expose
        private Integer entityId;
        @SerializedName("entity_name")
        @Expose
        private String entityName;
        @SerializedName("day_of_week")
        @Expose
        private Integer dayOfWeek;
        @SerializedName("day_name")
        @Expose
        private String dayName;
        @SerializedName("from_hour")
        @Expose
        private Integer fromHour;
        @SerializedName("from_min")
        @Expose
        private Integer fromMin;
        @SerializedName("to_hour")
        @Expose
        private Integer toHour;
        @SerializedName("to_min")
        @Expose
        private Integer toMin;
        @SerializedName("time_zone_diff")
        @Expose
        private Double timeZoneDiff;
        @SerializedName("flag_custom")
        @Expose
        private Boolean flagCustom;
        @SerializedName("deleted")
        @Expose
        private Boolean deleted;
        @SerializedName("rowcode")
        @Expose
        private String rowcode;

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

        public String getSlotDate() {
            return slotDate;
        }

        public void setSlotDate(String slotDate) {
            this.slotDate = slotDate;
        }

        public Integer getEntitySlotId() {
            return entitySlotId;
        }

        public void setEntitySlotId(Integer entitySlotId) {
            this.entitySlotId = entitySlotId;
        }

        public String getEntitySlotName() {
            return entitySlotName;
        }

        public void setEntitySlotName(String entitySlotName) {
            this.entitySlotName = entitySlotName;
        }

        public Integer getEntityId() {
            return entityId;
        }

        public void setEntityId(Integer entityId) {
            this.entityId = entityId;
        }

        public String getEntityName() {
            return entityName;
        }

        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }

        public Integer getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(Integer dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getDayName() {
            return dayName;
        }

        public void setDayName(String dayName) {
            this.dayName = dayName;
        }

        public Integer getFromHour() {
            return fromHour;
        }

        public void setFromHour(Integer fromHour) {
            this.fromHour = fromHour;
        }

        public Integer getFromMin() {
            return fromMin;
        }

        public void setFromMin(Integer fromMin) {
            this.fromMin = fromMin;
        }

        public Integer getToHour() {
            return toHour;
        }

        public void setToHour(Integer toHour) {
            this.toHour = toHour;
        }

        public Integer getToMin() {
            return toMin;
        }

        public void setToMin(Integer toMin) {
            this.toMin = toMin;
        }

        public Double getTimeZoneDiff() {
            return timeZoneDiff;
        }

        public void setTimeZoneDiff(Double timeZoneDiff) {
            this.timeZoneDiff = timeZoneDiff;
        }

        public Boolean getFlagCustom() {
            return flagCustom;
        }

        public void setFlagCustom(Boolean flagCustom) {
            this.flagCustom = flagCustom;
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
    }
}
