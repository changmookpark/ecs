package com.ktds.eventlistener.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ktds.eventlistener.common.EventSeverity;
import com.ktds.eventlistener.common.EventType;
import com.ktds.eventlistener.dto.EventDto;
import com.ktds.eventlistener.dto.HostDto;
import com.ktds.eventlistener.dto.ItemDto;
import com.ktds.eventlistener.dto.TriggerDto;
import com.ktds.eventlistener.dto.WebhookDto;
import com.ktds.eventlistener.model.ManagedEvent;
import com.ktds.eventlistener.model.RefinedEvent;
import com.ktds.eventlistener.repository.ManagedEventRepository;
import com.ktds.eventlistener.repository.RefinedEventRepository;
import com.ktds.eventlistener.specification.ManagedEventSpecification;

@Service
public class RefinedEventService {

    private static final Logger logger = LoggerFactory.getLogger(RefinedEventService.class);

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");

    private WebClient apiClient;

    @Autowired
    RefinedEventRepository refinedEventRepo;

    @Autowired
    ManagedEventRepository managedEventRepo;

    public RefinedEventService(@Value("${cloudwiz.api.url}") String apiUrl) {

        apiClient = WebClient.builder()
            .baseUrl(apiUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public Optional<ManagedEvent> findManagedEvent(RefinedEvent event) {

        Specification<ManagedEvent> spec = (root, query, CriteriaBuilder) -> null;

        if (!event.getHostName().isEmpty())
            spec = spec.and(ManagedEventSpecification.equalsHostName(event.getHostName()));

        if (!event.getIp().isEmpty())
            spec = spec.and(ManagedEventSpecification.equalsIp(event.getIp()));

        if (!event.getSeverity().isEmpty())
            spec = spec.and(ManagedEventSpecification.equalsSeverity(event.getSeverity()));

        if (!event.getEventCode().isEmpty())
            spec = spec.and(ManagedEventSpecification.equalsEventCode(event.getEventCode()));

        spec = spec.and(ManagedEventSpecification.equalsEventStatus("1"));

        return managedEventRepo.findOne(spec);
    }

    public void updateRefinedEvent(RefinedEvent event) {
        
        refinedEventRepo.save(event);
    }

    public void updateManagedEvent(ManagedEvent event) {

        managedEventRepo.save(event);
    }
    
    public String createManagedByRefined(RefinedEvent event) {

        int seq = refinedEventRepo.getEventSequence();
        LocalDate today = LocalDate.now();

        String newEventId = String.format("EVM%6s%09d", today.format(formatter), seq);

        ManagedEvent managedEvent = new ManagedEvent(
            newEventId, 
            event.getEventDate(), 
            "1", 
            event.getHostName(), 
            event.getIp(), 
            event.getSeverity(), 
            event.getEventCode(), 
            event.getEventTitle(), 
            event.getEventMessage(), 
            event.getTriggerId(), 
            event.getMonitorTool(), 
            null, 
            null, 
            null, 
            null, 
            null, 
            null);

        managedEventRepo.save(managedEvent);

        return newEventId;
    }

    public WebhookDto mapEventToWebhookDTO(RefinedEvent event) {

        return new WebhookDto(
            new HostDto(
                event.getIp(),
                "",
                "",
                ""
            ),
            new TriggerDto("", "", ""),
            event.getEventTitle(),
            event.getEventMessage(),
            new EventDto(
                event.getNewEventId(),
                EventType.fromCode(event.getEventType()).toString(),
                EventSeverity.fromCode(event.getSeverity()).toString(),
                event.getEventDate().toString()
            ),
            new ItemDto("", "", "")
        );
    }

    public String sendWebhook(WebhookDto webhookDto) {

        String response = null;

        try {

            logger.info("Send Webhook : " + objectMapper.writeValueAsString(webhookDto));

            response = apiClient.post()
                .bodyValue(webhookDto)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            logger.info(response);

        } catch (Exception ex) {

            logger.info(ex.toString());
        }

        return response;
    }
}
