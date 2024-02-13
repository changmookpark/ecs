package com.ktds.eventlistener.common;

public enum EventType {

    PROBLEM("1"),
    RESOLVED("2");

    private final String code;

    private EventType(String code) {

        this.code = code;
    }

    public String getCode() {

        return this.code;
    }

    public static EventType fromCode(String code) {

        for (EventType eventType : EventType.values()) {

            if (eventType.getCode().equals(code)) {
                return eventType;
            }
        }

        return null;
    }
}