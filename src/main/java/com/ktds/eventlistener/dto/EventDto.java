package com.ktds.eventlistener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventDto {
    
    private String id;

    private String status;

    private String severity;

    private String date;
}
