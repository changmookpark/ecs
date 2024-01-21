package com.ktds.eventlistener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ktds.eventlistener.model.RefinedEvent;

public interface RefinedEventRepository extends JpaRepository<RefinedEvent, String>, JpaSpecificationExecutor<RefinedEvent> {
    
}
