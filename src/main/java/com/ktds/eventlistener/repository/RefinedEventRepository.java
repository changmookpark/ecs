package com.ktds.eventlistener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ktds.eventlistener.model.RefinedEvent;

public interface RefinedEventRepository extends JpaRepository<RefinedEvent, String>, JpaSpecificationExecutor<RefinedEvent> {
    
    @Query(value = "SELECT event.get_event_seq()", nativeQuery = true)
    int getEventSequence();
}
