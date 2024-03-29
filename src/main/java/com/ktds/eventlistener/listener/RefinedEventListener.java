package com.ktds.eventlistener.listener;

import java.sql.Connection;
import java.sql.Statement;

import javax.annotation.PostConstruct;

import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ktds.eventlistener.handler.RefinedEventHandler;
import com.ktds.eventlistener.model.RefinedEvent;
import com.ktds.eventlistener.util.NotificationUtil;

@Component
public class RefinedEventListener implements EventListener {

    private static final Logger logger = LoggerFactory.getLogger("refinedLog");

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Value("${pg.channel.refined}")
    private String CHANNEL_REFINED;

    @Autowired
    private JdbcTemplate tpl;

    @Autowired
    private RefinedEventHandler handler;

    // @PostConstruct
    @Async
    public void listeningEvent() {

        tpl.execute((Connection con) -> {
            try (Statement stmt = con.createStatement()) {

                stmt.execute("LISTEN " + CHANNEL_REFINED);
                PGConnection pgcon = con.unwrap(PGConnection.class);
                
                logger.info("Listening refined event...");

                while(!Thread.currentThread().isInterrupted()) {

                    PGNotification[] nts = pgcon.getNotifications(10000);

                    if(nts == null || nts.length == 0) { continue; }

                    for(PGNotification nt : nts) {
                        
                        try {

                            RefinedEvent event = NotificationUtil.parseNotification(nt, RefinedEvent.class);
                            logger.info("Listen refined event notification : " + objectMapper.writeValueAsString(event));
                            handler.processEvent(event);
                        } catch (Exception ex) {
                            
                            logger.error(ex.toString());
                        }
                    }
                }
            }

            return 0;
        });
    }
}
