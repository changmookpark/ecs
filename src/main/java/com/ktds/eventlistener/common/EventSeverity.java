package com.ktds.eventlistener.common;

public enum EventSeverity {

    Disaster("Fatal"),
    High("Critical");

    private final String code;

    private EventSeverity(String code) {

        this.code = code;
    }

    public String getCode() {

        return this.code;
    }

    public static EventSeverity fromCode(String code) {

        for (EventSeverity eventSeverity : EventSeverity.values()) {

            if (eventSeverity.getCode().equals(code)) {
                return eventSeverity;
            }
        }

        return null;
    }
}