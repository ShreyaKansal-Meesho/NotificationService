package org.shreya.notificationservicenew.servicesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shreya.notificationservicenew.model.Enum.Status;
import org.shreya.notificationservicenew.model.Notification;
import org.shreya.notificationservicenew.repo.NotificationRepository;
import org.shreya.notificationservicenew.service.BlacklistService;
import org.shreya.notificationservicenew.service.ESService;
import org.shreya.notificationservicenew.service.KafkaService;
import org.shreya.notificationservicenew.service.NotificationService;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private BlacklistService blacklistService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ESService esService;

    @InjectMocks
    private KafkaService kafkaService;

    private Notification testNotification;
    private Notification testNotification2;

    @BeforeEach
    void setUp() {
        kafkaService = new KafkaService(notificationService, blacklistService, notificationRepository, esService);

        testNotification = Notification.builder()
                .id("test-id")
                .phoneNumber("+919876543210")
                .message("Test message")
                .status(Status.pending)
                .build();
    }

    @Test
    void testProcessNotification_SuccessfulSend() {
        String requestId = "test-id";
        when(notificationRepository.findById(requestId)).thenReturn(Optional.of(testNotification));
        when(blacklistService.isBlacklisted(testNotification.getPhoneNumber())).thenReturn(false);

        kafkaService.processNotification(requestId);
        verify(notificationService).updateNotification(requestId, Status.sent, null, null);
        verify(esService).saveLog(testNotification);
    }

    @Test
    void testProcessNotification_BlacklistedNumber() {
        String requestId = "test-id";

        when(notificationRepository.findById(requestId)).thenReturn(Optional.of(testNotification));
        when(blacklistService.isBlacklisted(testNotification.getPhoneNumber())).thenReturn(true);

        kafkaService.processNotification(requestId);
        verify(notificationService).updateNotification(requestId, Status.failed, "ERROR_BLACKLISTED", "Phone number is blacklisted!");
        verify(esService).saveLog(testNotification);
    }

    @Test
    void testProcessNotification_NotificationNotFound() {
        String requestId = "invalid-id";
        when(notificationRepository.findById(requestId)).thenReturn(Optional.empty());

        kafkaService.processNotification(requestId);
        verify(notificationService, never()).updateNotification(any(), any(), any(), any());
        verify(esService, never()).saveLog(any());
    }
}
