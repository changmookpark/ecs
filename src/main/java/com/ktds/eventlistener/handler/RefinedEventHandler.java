package com.ktds.eventlistener.handler;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ktds.eventlistener.model.RefinedEvent;
import com.ktds.eventlistener.service.RefinedEventService;

@Component
public class RefinedEventHandler implements EventHandler<RefinedEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(RefinedEventHandler.class);

    @Autowired
    RefinedEventService service;

    @Transactional
    public void processEvent(RefinedEvent event) {

    }
}
