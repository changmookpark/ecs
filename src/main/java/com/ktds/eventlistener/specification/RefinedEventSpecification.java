package com.ktds.eventlistener.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.ktds.eventlistener.model.ManagedEvent;
import com.ktds.eventlistener.model.RefinedEvent;

public class RefinedEventSpecification {

    public static Specification<RefinedEvent> equalsNewEventId(String newEventId) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("newEventId"), newEventId);
    }
    
    public static Specification<RefinedEvent> equalsEventDate(LocalDateTime eventDate) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventDate"), eventDate);
    }

    public static Specification<RefinedEvent> equalsHostName(String hostName) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("hostName"), hostName);
    }

    public static Specification<RefinedEvent> equalsIp(String ip) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("ip"), ip);
    }

    public static Specification<RefinedEvent> equalsSeverity(String severity) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("severity"), severity);
    }

    public static Specification<RefinedEvent> equalsEventCode(String eventCode) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventCode"), eventCode);
    }

    public static Specification<RefinedEvent> equalsEventTitle(String eventTitle) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventTitle"), eventTitle);
    }

    public static Specification<RefinedEvent> equalsEventType(String eventType) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventType"), eventType);
    }

    public static Specification<ManagedEvent> equalsTriggerId(String triggerId) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventTriggerId"), triggerId);        
    }
}
