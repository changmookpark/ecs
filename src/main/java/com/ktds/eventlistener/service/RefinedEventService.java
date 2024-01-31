package com.ktds.eventlistener.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ktds.eventlistener.model.ManagedEvent;
import com.ktds.eventlistener.model.RefinedEvent;
import com.ktds.eventlistener.repository.ManagedEventRepository;
import com.ktds.eventlistener.repository.RefinedEventRepository;
import com.ktds.eventlistener.specification.ManagedEventSpecification;

@Service
public class RefinedEventService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");

    @Autowired
    RefinedEventRepository refinedEventRepo;

    @Autowired
    ManagedEventRepository managedEventRepo;

    public Optional<ManagedEvent> findManagedEvent(RefinedEvent event) {

        Specification<ManagedEvent> spec = (root, query, CriteriaBuilder) -> null;

        if (!event.getHostName().isEmpty())
            spec = spec.and(ManagedEventSpecification.equalsHostName(event.getHostName()));

        if (!event.getIp().isEmpty())
            spec = spec.and(ManagedEventSpecification.equalsIp(event.getIp()));

        if (!event.getSeverity().isEmpty())
            spec = spec.and(ManagedEventSpecification.equalsSeverity(event.getSeverity()));

        if (!event.getEventCode().isEmpty())
            spec = spec.and(ManagedEventSpecification.equalsEventCode(event.getEventCode()));

        spec = spec.and(ManagedEventSpecification.equalsEventStatus("1"));

        return managedEventRepo.findOne(spec);
    }

    public void updateRefinedEvent(RefinedEvent event) {
        
        refinedEventRepo.save(event);
    }

    public void updateManagedEvent(ManagedEvent event) {

        managedEventRepo.save(event);
    }
    
    public String createManagedByRefined(RefinedEvent event) {

        int seq = refinedEventRepo.getEventSequence();
        LocalDate today = LocalDate.now();

        String newEventId = String.format("EVM%6s%09d", today.format(formatter), seq);

        ManagedEvent managedEvent = new ManagedEvent(
            newEventId, 
            event.getEventDate(), 
            "1", 
            event.getHostName(), 
            event.getIp(), 
            event.getSeverity(), 
            event.getEventCode(), 
            event.getEventTitle(), 
            event.getEventMessage(), 
            event.getTriggerId(), 
            event.getMonitorTool(), 
            null, 
            null, 
            null, 
            null, 
            null, 
            null);

        managedEventRepo.save(managedEvent);

        return newEventId;
    }
}
