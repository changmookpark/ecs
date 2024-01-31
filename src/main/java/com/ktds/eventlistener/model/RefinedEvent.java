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
public class RefinedEvent {
    
    @Id
    @Column(nullable=false)
    private String eventId;

    private String newEventId;

    @Column(nullable=false)
    private LocalDateTime eventDate;

    @Column(nullable=false)
    private LocalDateTime lastEventDate;

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

    private LocalDateTime procDate;

    public void updateNewEventId(String newEventId) {
        this.newEventId = newEventId;
    }

    public void updateLastEventDate(LocalDateTime lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

    public void updateEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public void updateProcFlag(String procFlag) {
        this.procFlag = procFlag;
    }

    public void updateProcDate(LocalDateTime procDate) {
        this.procDate = procDate;
    }
}
