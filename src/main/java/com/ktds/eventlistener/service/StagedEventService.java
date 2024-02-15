package com.ktds.eventlistener.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ktds.eventlistener.model.ManagedEvent;
import com.ktds.eventlistener.model.RefinedEvent;
import com.ktds.eventlistener.model.StagedEvent;
import com.ktds.eventlistener.repository.ManagedEventRepository;
import com.ktds.eventlistener.repository.RefinedEventRepository;
import com.ktds.eventlistener.repository.StagedEventRepository;
import com.ktds.eventlistener.specification.ManagedEventSpecification;
import com.ktds.eventlistener.specification.RefinedEventSpecification;

@Service
public class StagedEventService {

    private static final Logger logger = LoggerFactory.getLogger("stagedLog");

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    StagedEventRepository stagedEventRepo;
    
    @Autowired
    RefinedEventRepository refinedEventRepo;

    @Autowired
    ManagedEventRepository managedEventRepo;

    public List<RefinedEvent> findDupEvent(StagedEvent event) throws Exception {

        Specification<RefinedEvent> spec = (root, query, CriteriaBuilder) -> null;

        if (event.getEventDate() != null)
            spec = spec.and(RefinedEventSpecification.equalsEventDate(event.getEventDate()));

        if (!event.getHostName().isEmpty())
            spec = spec.and(RefinedEventSpecification.equalsHostName(event.getHostName()));

        if (!event.getIp().isEmpty())
            spec = spec.and(RefinedEventSpecification.equalsIp(event.getIp()));

        if (!event.getEventTitle().isEmpty())
            spec = spec.and(RefinedEventSpecification.equalsEventTitle(event.getEventTitle()));

        if (!event.getSeverity().isEmpty())
            spec = spec.and(RefinedEventSpecification.equalsSeverity(event.getSeverity()));

        if (!event.getEventType().isEmpty())
            spec = spec.and(RefinedEventSpecification.equalsEventType(event.getEventType()));

        List<RefinedEvent> refinedEvents = refinedEventRepo.findAll(spec);

        logger.info(String.format("(%s) Duplicate event : %s", event.getEventId(), objectMapper.writeValueAsString(refinedEvents)));

        return refinedEvents;
    }

    public List<RefinedEvent> findInProgressEvent(StagedEvent event) throws Exception {

        Specification<ManagedEvent> managedSpec = (root, query, CriteriaBuilder) -> null;

        if (event.getEventDate() != null) {
            LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
            managedSpec = managedSpec.and(ManagedEventSpecification.greaterThanEventDate(yesterday));
        }

        if (!event.getHostName().isEmpty())
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsHostName(event.getHostName()));

        if (!event.getIp().isEmpty())
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsIp(event.getIp()));

        if (!event.getSeverity().isEmpty())
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsSeverity(event.getSeverity()));

        if (!event.getEventCode().isEmpty())
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsEventCode(event.getEventCode()));

        managedSpec = managedSpec.and(ManagedEventSpecification.equalsEventStatus("1"));

        List<ManagedEvent> managedEvents = managedEventRepo.findAll(managedSpec);
        List<RefinedEvent> refinedEvents = new ArrayList<>();

        logger.info(String.format("(%s) In progress event (managed) : %s", event.getEventId(), objectMapper.writeValueAsString(managedEvents.isEmpty() ? "" : managedEvents)));

        if (!managedEvents.isEmpty()) {

            ManagedEvent maangedEvent = managedEvents.get(0);
            Specification<RefinedEvent> refinedSpec = (root, query, CriteriaBuilder) -> null;

            refinedSpec = refinedSpec.and(RefinedEventSpecification.equalsNewEventId(maangedEvent.getEventId()));
            refinedSpec = refinedSpec.and(RefinedEventSpecification.equalsEventType("1"));

            refinedEvents = refinedEventRepo.findAll(refinedSpec);

            logger.info(String.format("(%s) In progress event (refined) : %s", event.getEventId(), objectMapper.writeValueAsString(refinedEvents)));
        }

        return refinedEvents;
    }

    public List<RefinedEvent> findProgressedEvent(StagedEvent event) throws Exception {

        Specification<ManagedEvent> managedSpec = (root, query, CriteriaBuilder) -> null;

        if (event.getEventDate() != null) {
            LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
            managedSpec = managedSpec.and(ManagedEventSpecification.greaterThanEventDate(yesterday));
        }

        if (!event.getHostName().isEmpty())
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsHostName(event.getHostName()));

        if (!event.getIp().isEmpty())
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsIp(event.getIp()));

        if (!event.getSeverity().isEmpty())
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsSeverity(event.getSeverity()));

        if (!event.getEventCode().isEmpty())
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsEventCode(event.getEventCode()));

        List<ManagedEvent> managedEvents = managedEventRepo.findAll(managedSpec);
        List<RefinedEvent> refinedEvents = new ArrayList<>();

        logger.info(String.format("(%s) Progressed event (managed) : %s", event.getEventId(), objectMapper.writeValueAsString(managedEvents)));

        if (!managedEvents.isEmpty()) {

            ManagedEvent managedEvent = managedEvents.get(0);
            Specification<RefinedEvent> refinedSpec = (root, query, CriteriaBuilder) -> null;

            refinedSpec = refinedSpec.and(RefinedEventSpecification.equalsNewEventId(managedEvent.getEventId()));
            refinedSpec = refinedSpec.and(RefinedEventSpecification.equalsEventType(managedEvent.getEventStatus()));

            refinedEvents = refinedEventRepo.findAll(refinedSpec);

            logger.info(String.format("(%s) Progressed event (refined) : %s", event.getEventId(), objectMapper.writeValueAsString(refinedEvents)));
        }

        return refinedEvents;
    }

    public void updateStagedEvent(StagedEvent event) {

        stagedEventRepo.save(event);
    }

    public void updateRefinedEvent(RefinedEvent event) {
        
        refinedEventRepo.save(event);
    }

    public void createRefinedByStaged(StagedEvent event) {

        RefinedEvent refinedEvent = new RefinedEvent(
                event.getEventId(),
                null,
                event.getEventDate(),
                event.getEventDate(),
                event.getHostName(),
                event.getIp(),
                event.getSeverity(),
                event.getEventCode(),
                event.getEventTitle(),
                event.getEventMessage(),
                event.getEventType(),
                event.getTriggerId(),
                event.getMonitorTool(),
                1,
                "W",
                null);

        refinedEventRepo.save(refinedEvent);
    }
}
