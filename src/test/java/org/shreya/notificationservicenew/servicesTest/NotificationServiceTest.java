package org.shreya.notificationservicenew.servicesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shreya.notificationservicenew.model.Enum.Status;
import org.shreya.notificationservicenew.model.Notification;
import org.shreya.notificationservicenew.model.NotificationRequest;
import org.shreya.notificationservicenew.repo.NotificationRepository;
import org.shreya.notificationservicenew.service.NotificationService;
import org.shreya.notificationservicenew.util.Constants;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;
    private NotificationRequest request;

    @BeforeEach
    void setUp() {
        request = new NotificationRequest("9876543210", "Test message");

        notification = Notification.builder()
                .id("test-id")
                .phoneNumber(request.getPhoneNumber())
                .message(request.getMessage())
                .status(Status.pending)
                .build();
    }

    @Test
    void testCreateNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        Notification result = notificationService.createNotification(request);

        assertNotNull(result);
        assertEquals(request.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(request.getMessage(), result.getMessage());
        assertEquals(Status.pending, result.getStatus());

        verify(kafkaTemplate).send(eq(Constants.KAFKA_NOTIFICATION_TOPIC), eq(request.getPhoneNumber()), any(String.class));
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testUpdateNotification_Success() {
        String notificationId = notification.getId();

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        notificationService.updateNotification(notificationId, Status.sent, null, null);

        assertEquals(Status.sent, notification.getStatus());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testUpdateNotification_NotificationNotFound() {
        String invalidId = "invalid-id";

        when(notificationRepository.findById(invalidId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                notificationService.updateNotification(invalidId, Status.failed, "ERROR", "Failure message")
        );

        assertEquals("Notification not found with id: " + invalidId, exception.getMessage());
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void testGetNotificationById_Success() {
        String notificationId = notification.getId();

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        Notification result = notificationService.getNotificationById(notificationId);

        assertNotNull(result);
        assertEquals(notificationId, result.getId());
        verify(notificationRepository, times(1)).findById(notificationId);
    }

    @Test
    void testGetNotificationById_NotificationNotFound() {
        String invalidId = "invalid-id";

        when(notificationRepository.findById(invalidId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> notificationService.getNotificationById(invalidId));

        assertEquals("Notification not found with id: " + invalidId, exception.getMessage());
        verify(notificationRepository).findById(invalidId);
    }
}
