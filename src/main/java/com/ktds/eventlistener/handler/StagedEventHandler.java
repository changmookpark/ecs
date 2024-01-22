package com.ktds.eventlistener.handler;

import org.springframework.stereotype.Component;

import com.ktds.eventlistener.model.StagedEvent;

@Component
public class StagedEventHandler implements EventHandler<StagedEvent> {
    
    public void processEvent(StagedEvent event) {
        
    }
}
