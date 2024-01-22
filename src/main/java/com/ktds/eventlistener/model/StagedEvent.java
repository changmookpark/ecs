package com.ktds.eventlistener.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(schema="event")
public class StagedEvent {
    
    @Id
    @Column(nullable=false)
    private String eventId;

    @Column(nullable=false)
    private LocalDate eventDate;

    private String hostName;

    private String ip;

    private String severity;

    private String eventCode;

    private String eventTitle;

    private String eventMessage;

    @Column(nullable=false)
    private String eventType;

    private String triggerId;

    @Column(nullable=false)
    private String monitorTool;

    private String procFlag;

    private String passFlag;

    private String passMessage;
}
