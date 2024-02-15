package com.ktds.eventlistener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HostDto {
    
    private String connectionInfo;

    private String id;

    private String visibleName;

    private String name;
}
