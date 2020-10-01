package com.thriive.app.models;

public class EventBusPOJO {
    int event;
    String meeting_id;

    public EventBusPOJO() {
    }

    public EventBusPOJO(int event, String meeting_id) {
        this.event = event;
        this.meeting_id = meeting_id;
    }

    public EventBusPOJO(int event) {
        this.event = event;
    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}
