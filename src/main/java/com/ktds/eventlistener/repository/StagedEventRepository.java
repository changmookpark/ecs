package com.ktds.eventlistener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ktds.eventlistener.model.StagedEvent;

public interface StagedEventRepository extends JpaRepository<StagedEvent, String>, JpaSpecificationExecutor<StagedEvent> {
    
}
