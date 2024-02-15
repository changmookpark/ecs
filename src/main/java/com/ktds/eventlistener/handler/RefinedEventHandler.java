package com.ktds.eventlistener.handler;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ktds.eventlistener.dto.WebhookDto;
import com.ktds.eventlistener.model.ManagedEvent;
import com.ktds.eventlistener.model.RefinedEvent;
import com.ktds.eventlistener.service.RefinedEventService;

@Component
public class RefinedEventHandler implements EventHandler<RefinedEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger("refinedLog");

    @Autowired
    RefinedEventService service;

    @Transactional
    public void processEvent(RefinedEvent event) {

        logger.info(String.format("(%s) Starting to handle refined event data... [%d]", event.getEventId(), ("1".equals(event.getEventType()) ? 1 : 2)));

        try {

            if ("1".equals(event.getEventType())) {

                String newEventId = service.createManagedByRefined(event);
                event.updateNewEventId(newEventId);

                logger.info(String.format("(%s) New Event Id : %s", event.getEventId(), event.getNewEventId()));

                WebhookDto webhookDto = service.mapEventToWebhookDTO(event);
                service.sendWebhook(webhookDto);
                
            } else {

                Optional<ManagedEvent> optional = Optional.empty();
                
                optional = service.findManagedEvent(event);

                if (optional.isPresent()) {

                    ManagedEvent managedEvent = optional.get();

                    managedEvent.updateEventStatus("2");
                    service.updateManagedEvent(managedEvent);

                    event.updateNewEventId(managedEvent.getEventId());

                    WebhookDto webhookDto = service.mapEventToWebhookDTO(event);
                    service.sendWebhook(webhookDto);
                } else {
                    throw new Exception();
                }
            }
        } catch (Exception ex) {

            logger.info(String.format("(%s) %s", event.getEventId(), "No Pair Event"));
        } finally {

            logger.info(String.format("(%s) Finished handling refined event data...", event.getEventId()));

            event.updateProcFlag("S");
            event.updateProcDate(LocalDateTime.now());

            service.updateRefinedEvent(event);
        }
    }
}
