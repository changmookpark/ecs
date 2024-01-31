package com.ktds.eventlistener.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private LocalDateTime eventDate;

    @Column(nullable=false)
    private String eventStatus;

    private String hostName;

    private String ip;

    private String severity;

    private String eventCode;

    private String eventTitle;

    private String eventMessage;

    private String triggerId;

    @Column(nullable=false)
    private String monitorTool;

    private String diposer;

    private String diposerId;

    private LocalDateTime diposalDate;

    private String acceptor;

    private String acceptorId;

    private LocalDateTime acceptDate;
}
