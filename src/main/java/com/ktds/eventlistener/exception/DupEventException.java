package com.ktds.eventlistener.exception;

public class DupEventException extends Exception {

    public static final int DUP_EVENT = 1;
    public static final int DUP_EVENT_IN_PROGRESS = 2;
    public static final int DUP_EVENT_PROGRESSED = 3;

    private int code;

    public DupEventException(String message) {
        super(message);
    }

    public DupEventException(int code) {

        super(getMessage(code));
        this.code = code;
    }

    private static String getMessage(int code) {

        String message = "";

        switch(code) {
            case DUP_EVENT:
                message = "Dup Evnet skip";
                break;
            case DUP_EVENT_IN_PROGRESS:
                message = "Dup Evnet skip (In Progress)";
                break;
            case DUP_EVENT_PROGRESSED:
                message = "Dup Evnet skip (Progressed)";
                break;
        }

        return message;
    }
}
