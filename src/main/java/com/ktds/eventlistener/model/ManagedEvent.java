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
public class ManagedEvent {

    @Id
    @Column(nullable=false)
    private String eventId;

    @Column(nullable=false)
    private LocalDate eventDate;

    @Column(nullable=false)
    private String eventStatus;

    private String hostName;

    private String ip;

    private String severity;

    private String eventCode;

    private String eventMessage;

    private String eventInfo;

    private String triggerId;

    @Column(nullable=false)
    private String monitorTool;

    private String diposer;

    private String diposerId;

    private LocalDate diposalDate;

    private String acceptor;

    private String acceptorId;

    private LocalDate acceptDate;
}
