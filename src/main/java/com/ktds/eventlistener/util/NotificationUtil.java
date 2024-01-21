package com.ktds.eventlistener.util;

import org.postgresql.PGNotification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class NotificationUtil {
    
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .registerModule(new JavaTimeModule());

    public static <T> T parseNotification(PGNotification nt, Class<T> classz) throws Exception {
        String jsonData = nt.getParameter();
        return objectMapper.readValue(jsonData, classz);
    }
}
