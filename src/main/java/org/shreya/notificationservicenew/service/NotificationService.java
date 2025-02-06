package org.shreya.notificationservicenew.service;

import jakarta.transaction.Transactional;
import org.shreya.notificationservicenew.model.Enum.Status;
import org.shreya.notificationservicenew.model.Notification;
import org.shreya.notificationservicenew.model.NotificationRequest;
import org.shreya.notificationservicenew.repo.NotificationRepository;
import org.shreya.notificationservicenew.util.Constants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public NotificationService(NotificationRepository notificationRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.notificationRepository = notificationRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Notification createNotification(NotificationRequest request) {
        String ID = UUID.randomUUID().toString();

        Notification notification = Notification.builder()
                .id(ID)
                .phoneNumber(request.getPhoneNumber())
                .message(request.getMessage())
                .status(Status.pending)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        kafkaTemplate.send(Constants.KAFKA_NOTIFICATION_TOPIC, request.getPhoneNumber(), ID);

        return savedNotification;
    }

    @Transactional
    public Notification updateNotification(String id, Status status, String failureCode, String failureComments) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + id));

        notification.setStatus(status);
        notification.setFailure_code(failureCode);
        notification.setFailure_comments(failureComments);
        notification.setUpdated_at(LocalDateTime.now());

        notificationRepository.save(notification);
        return notification;
    }

    public Notification getNotificationById(String id) {
        System.out.println(id);
        return notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + id));
    }
}
