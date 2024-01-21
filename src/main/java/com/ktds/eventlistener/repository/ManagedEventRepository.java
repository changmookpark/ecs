package com.ktds.eventlistener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ktds.eventlistener.model.ManagedEvent;

public interface ManagedEventRepository extends JpaRepository<ManagedEvent, String>, JpaSpecificationExecutor<ManagedEvent> {
    
}
