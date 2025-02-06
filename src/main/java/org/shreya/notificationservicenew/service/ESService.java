package org.shreya.notificationservicenew.service;

import org.shreya.notificationservicenew.model.Notification;
import org.shreya.notificationservicenew.model.NotificationDocument;
import org.shreya.notificationservicenew.repo.ESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class ESService {
    @Autowired
    private ESRepository esRepository;

    public ESService(ESRepository esRepository) {
        this.esRepository = esRepository;
    }

    public void saveLog(Notification notification) {
        NotificationDocument log=NotificationDocument.builder()
                .message(notification.getMessage())
                .createdAt(Timestamp.from(Instant.now()))
                .phoneNumber(notification.getPhoneNumber())
                .logLevel(notification.getStatus())
                .source("Database").build();
        esRepository.save(log);
    }

    public List<NotificationDocument> getLogsBetweenDates(String start, String end) {
        Instant instant1 = Instant.parse(start + "Z");
        Instant instant2 = Instant.parse(end + "Z");
        Timestamp timestamp1 = Timestamp.from(instant1);
        Timestamp timestamp2 = Timestamp.from(instant2);
        return esRepository.findByCreatedAtBetween(timestamp1, timestamp2);
    }

    public List<NotificationDocument> getLogsContainingMessage(String message) {
        return esRepository.findByMessageContaining(message);
    }
}
