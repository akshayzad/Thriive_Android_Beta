package com.thriive.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonRequestTimeSlots {

    @SerializedName("IsOK")
    private Boolean isOK;
    @SerializedName("Message")
    private String message;
    @SerializedName("slot_list")
    private List<EntitySlotsListPOJO> slotsListPOJOS = null;

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

    public List<EntitySlotsListPOJO> getSlotsListPOJOS() {
        return slotsListPOJOS;
    }

    public void setSlotsListPOJOS(List<EntitySlotsListPOJO> slotsListPOJOS) {
        this.slotsListPOJOS = slotsListPOJOS;
    }

    public class EntitySlotsListPOJO {

        @SerializedName("from_hour")
        private int from_hour;

        @SerializedName("from_min")
        private int from_min;

        @SerializedName("to_hour")
        private int to_hour;

        @SerializedName("to_min")
        private int to_min;

        @SerializedName("for_date")
        private String for_date;

        @SerializedName("is_disabled")
        private boolean is_disabled;

        @SerializedName("helper_text")
        private String helper_text;

        @SerializedName("slot_from_date")
        private String slot_from_date;


        public String getSlot_from_date() {
            return slot_from_date;
        }

        public void setSlot_from_date(String slot_from_date) {
            this.slot_from_date = slot_from_date;
        }

        public boolean isIs_disabled() {
            return is_disabled;
        }

        public void setIs_disabled(boolean is_disabled) {
            this.is_disabled = is_disabled;
        }

        public String getHelper_text() {
            return helper_text;
        }

        public void setHelper_text(String helper_text) {
            this.helper_text = helper_text;
        }

        public int getFrom_hour() {
            return from_hour;
        }

        public void setFrom_hour(int from_hour) {
            this.from_hour = from_hour;
        }

        public int getFrom_min() {
            return from_min;
        }

        public void setFrom_min(int from_min) {
            this.from_min = from_min;
        }

        public int getTo_hour() {
            return to_hour;
        }

        public void setTo_hour(int to_hour) {
            this.to_hour = to_hour;
        }

        public int getTo_min() {
            return to_min;
        }

        public void setTo_min(int to_min) {
            this.to_min = to_min;
        }

        public String getFor_date() {
            return for_date;
        }

        public void setFor_date(String for_date) {
            this.for_date = for_date;
        }
    }

}
