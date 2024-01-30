package com.ktds.eventlistener.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    @Autowired
    StagedEventRepository stagedEventRepo;
    
    @Autowired
    RefinedEventRepository refinedEventRepo;

    @Autowired
    ManagedEventRepository managedEventRepo;

    public Optional<RefinedEvent> findDupEvent(StagedEvent event) {

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

        return refinedEventRepo.findOne(spec);
    }

    public Optional<RefinedEvent> findInProgressEvent(StagedEvent event) {

        Specification<ManagedEvent> managedSpec = (root, query, CriteriaBuilder) -> null;

        if (event.getEventDate() != null) {
            LocalDate yesterday = LocalDate.now().minusDays(1);
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

        Optional<ManagedEvent> managedEventOptional = managedEventRepo.findOne(managedSpec);
        Optional<RefinedEvent> refinedEventOptional  = Optional.empty();

        if (managedEventOptional.isPresent()) {

            ManagedEvent maangedEvent = managedEventOptional.get();
            Specification<RefinedEvent> refinedSpec = (root, query, CriteriaBuilder) -> null;

            refinedSpec = refinedSpec.and(RefinedEventSpecification.equalsNewEventId(maangedEvent.getEventId()));
            refinedSpec = refinedSpec.and(RefinedEventSpecification.equalsEventType("1"));
            
            refinedEventOptional = refinedEventRepo.findOne(refinedSpec);
        }

        return refinedEventOptional;
    }

    public Optional<RefinedEvent> findProgressedEvent(StagedEvent event) {

        Specification<ManagedEvent> managedSpec = (root, query, CriteriaBuilder) -> null;

        if (event.getEventDate() != null) {
            LocalDate yesterday = LocalDate.now().minusDays(1);
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

        Optional<ManagedEvent> managedEventOptional = managedEventRepo.findOne(managedSpec);
        Optional<RefinedEvent> refinedEventOptional  = Optional.empty();

        if (managedEventOptional.isPresent()) {

            ManagedEvent managedEvent = managedEventOptional.get();
            Specification<RefinedEvent> refinedSpec = (root, query, CriteriaBuilder) -> null;

            refinedSpec = refinedSpec.and(RefinedEventSpecification.equalsNewEventId(managedEvent.getEventId()));
            refinedSpec = refinedSpec.and(RefinedEventSpecification.equalsEventType(managedEvent.getEventStatus()));

            refinedEventOptional = refinedEventRepo.findOne(refinedSpec);
        }

        return refinedEventOptional;
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
