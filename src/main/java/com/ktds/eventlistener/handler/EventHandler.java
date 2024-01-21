package com.ktds.eventlistener.handler;

public interface EventHandler<T> {
    
    public void processEvent(T event);
}
