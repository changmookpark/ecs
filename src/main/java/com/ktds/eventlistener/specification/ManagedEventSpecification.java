package com.ktds.eventlistener.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.ktds.eventlistener.model.ManagedEvent;

public class ManagedEventSpecification {

    public static Specification<ManagedEvent> greaterThanEventDate(LocalDateTime date) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), date);
    }
    
    public static Specification<ManagedEvent> equalsEventStatus(String eventStatus) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventStatus"), eventStatus);        
    }

    public static Specification<ManagedEvent> equalsHostName(String hostName) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("hostName"), hostName);        
    }

    public static Specification<ManagedEvent> equalsIp(String ip) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("ip"), ip);        
    }

    public static Specification<ManagedEvent> equalsSeverity(String severity) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("severity"), severity);        
    }

    public static Specification<ManagedEvent> equalsEventCode(String eventCode) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventCode"), eventCode);        
    }

    public static Specification<ManagedEvent> equalsTriggerId(String triggerId) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventTriggerId"), triggerId);        
    }
}
