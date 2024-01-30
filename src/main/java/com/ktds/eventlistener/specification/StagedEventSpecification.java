package com.ktds.eventlistener.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.ktds.eventlistener.model.StagedEvent;

public class StagedEventSpecification {

    public static Specification<StagedEvent> equalsEventDate(LocalDateTime eventDate) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventDate"), eventDate);
    }

    public static Specification<StagedEvent> equalsHostName(String hostName) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("hostName"), hostName);
    }

    public static Specification<StagedEvent> equalsIp(String ip) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("ip"), ip);
    }

    public static Specification<StagedEvent> equalsSeverity(String severity) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("severity"), severity);
    }

    public static Specification<StagedEvent> equalsEventCode(String eventCode) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventCode"), eventCode);
    }

    public static Specification<StagedEvent> equalsEventTitle(String eventTitle) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventTitle"), eventTitle);
    }

    public static Specification<StagedEvent> equalsEventType(String eventType) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("eventType"), eventType);
    }
}
