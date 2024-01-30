package com.ktds.eventlistener.handler;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ktds.eventlistener.exception.DupEventException;
import com.ktds.eventlistener.model.RefinedEvent;
import com.ktds.eventlistener.model.StagedEvent;
import com.ktds.eventlistener.service.StagedEventService;

@Component
public class StagedEventHandler implements EventHandler<StagedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StagedEventHandler.class);

    @Autowired
    StagedEventService service;
    
    @javax.transaction.Transactional
    public void processEvent(StagedEvent event) {
        
        logger.info(String.format("(%s) Starting to handle staged event data... [%d]", event.getEventId(), ("1".equals(event.getEventType()) ? 1 : 2)));
        Optional<RefinedEvent> optional = Optional.empty();

        try {

            logger.info(String.format("(%s) Check for duplicate event...", event.getEventId()));
            optional = service.findDupEvent(event);

            if (optional.isPresent()) {
                throw new DupEventException(DupEventException.DUP_EVENT);
            }

            if ("1".equals(event.getEventType())) {

                logger.info(String.format("(%s) Check for duplicate events in progress...", event.getEventId()));
                optional = service.findInProgressEvent(event);

                if (optional.isPresent()) {
                    throw new DupEventException(DupEventException.DUP_EVENT_IN_PROGRESS);
                }
            } else {
                
                optional = service.findProgressedEvent(event);

                if (optional.isPresent()) {
                    if ("2".equals(optional.get().getEventType())) {
                        throw new DupEventException(DupEventException.DUP_EVENT_PROGRESSED);
                    }
                } else {
                    throw new Exception();
                }
            }

            // === 이벤트 중복 검사 완료
            service.createRefinedByStaged(event);

            event.updatePassFlag("N");
        } catch (DupEventException ex) {
        
            RefinedEvent refinedEvent = optional.get();
            String passMessage = ex.getMessage();

            logger.info(String.format("(%s) %s", event.getEventId(), passMessage));

            event.updatePassFlag("Y");
            event.updatePassMessage(passMessage);

            refinedEvent.updateLastEventDate(event.getEventDate());
            refinedEvent.updateEventCount(refinedEvent.getEventCount() + 1);

            service.updateRefinedEvent(refinedEvent);
        } catch (Exception ex) {

            String passMessage = String.format("Request Error skip (%s)", ex.getClass().getSimpleName());

            logger.info(String.format("(%s) %s", event.getEventId(), passMessage));

            event.updatePassFlag("Y");
            event.updatePassMessage(passMessage);
        } finally {

            logger.info(String.format("(%s) Finished handling staged event data...", event.getEventId()));

            event.updateProcFlag("S");

            service.updateStagedEvent(event);
        }
    }
}
