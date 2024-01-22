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
public class RefinedEvent {
    
    @Id
    @Column(nullable=false)
    private String eventId;

    private String newEventId;

    @Column(nullable=false)
    private LocalDate eventDate;

    @Column(nullable=false)
    private LocalDate lastEventDate;

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

    private int eventCount;

    private String procFlag;

    private LocalDate procDate;

    public void updateLastEventDate(LocalDate lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

    public void updateEventCount(int eventCount) {
        this.eventCount = eventCount;
    }
}
