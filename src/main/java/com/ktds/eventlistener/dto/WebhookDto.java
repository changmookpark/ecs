package com.ktds.eventlistener.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebhookDto {

    private HostDto host;

    private TriggerDto trigger;

    private String title;

    private final String to = "cloudwiz webhook";

    private String message;

    private EventDto event;

    private ItemDto item;
}
