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
import com.ktds.eventlistener.specification.ManagedEventSpecification;
import com.ktds.eventlistener.specification.RefinedEventSpecification;

import io.micrometer.common.util.StringUtils;

@Service
public class StagedEventService {
    
    @Autowired
    RefinedEventRepository refinedEventRepo;

    @Autowired
    ManagedEventRepository managedEventRepo;

    public Optional<RefinedEvent> findDupEvent(StagedEvent event) {

        Specification<RefinedEvent> spec = (root, query, CriteriaBuilder) -> null;

        if (event.getEventDate() != null)
            spec = spec.and(RefinedEventSpecification.equalsEventDate(event.getEventDate()));

        if (StringUtils.isNotEmpty(event.getHostName()))
            spec = spec.and(RefinedEventSpecification.equalsHostName(event.getHostName()));

        if (StringUtils.isNotEmpty(event.getIp()))
            spec = spec.and(RefinedEventSpecification.equalsIp(event.getIp()));

        if (StringUtils.isNotEmpty(event.getEventTitle()))
            spec = spec.and(RefinedEventSpecification.equalsEventTitle(event.getEventTitle()));

        if (StringUtils.isNotEmpty(event.getSeverity()))
            spec = spec.and(RefinedEventSpecification.equalsSeverity(event.getSeverity()));

        if (StringUtils.isNotEmpty(event.getEventType()))
            spec = spec.and(RefinedEventSpecification.equalsEventType(event.getEventType()));

        return refinedEventRepo.findOne(spec);
    }

    public Optional<RefinedEvent> findInProgressEvent(StagedEvent event) {

        Specification<ManagedEvent> managedSpec = (root, query, CriteriaBuilder) -> null;

        if (event.getEventDate() != null) {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            managedSpec = managedSpec.and(ManagedEventSpecification.greaterThanEventDate(yesterday));
        }

        if (StringUtils.isNotEmpty(event.getHostName()))
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsHostName(event.getHostName()));

        if (StringUtils.isNotEmpty(event.getIp()))
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsIp(event.getIp()));

        if (StringUtils.isNotEmpty(event.getSeverity()))
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsSeverity(event.getSeverity()));

        if (StringUtils.isNotEmpty(event.getEventCode()))
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

        if (StringUtils.isNotEmpty(event.getHostName()))
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsHostName(event.getHostName()));

        if (StringUtils.isNotEmpty(event.getIp()))
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsIp(event.getIp()));

        if (StringUtils.isNotEmpty(event.getSeverity()))
            managedSpec = managedSpec.and(ManagedEventSpecification.equalsSeverity(event.getSeverity()));

        if (StringUtils.isNotEmpty(event.getEventCode()))
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
}
