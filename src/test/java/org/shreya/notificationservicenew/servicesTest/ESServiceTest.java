package org.shreya.notificationservicenew.servicesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shreya.notificationservicenew.model.Enum.Status;
import org.shreya.notificationservicenew.model.Notification;
import org.shreya.notificationservicenew.model.NotificationDocument;
import org.shreya.notificationservicenew.repo.ESRepository;
import org.shreya.notificationservicenew.service.ESService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ESServiceTest {

    @Mock
    private ESRepository esRepository;

    @InjectMocks
    private ESService esService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testSaveLog() {
        Notification notification = new Notification();
        notification.setMessage("Test Message");
        notification.setPhoneNumber("9876543210");
        notification.setStatus(Status.sent);

        NotificationDocument log = NotificationDocument.builder()
                .message(notification.getMessage())
                .createdAt(Timestamp.from(Instant.now()))
                .phoneNumber(notification.getPhoneNumber())
                .logLevel(notification.getStatus())
                .source("Database").build();

        when(esRepository.save(any(NotificationDocument.class))).thenReturn(log);
        esService.saveLog(notification);
        verify(esRepository).save(any(NotificationDocument.class));
    }

    @Test
    void testGetLogsBetweenDates() {
        String start = "2024-02-01T00:00:00";
        String end = "2024-02-02T00:00:00";

        Instant instant1 = Instant.parse(start+'Z');
        Instant instant2 = Instant.parse(end+'Z');
        Timestamp timestamp1 = Timestamp.from(instant1);
        Timestamp timestamp2 = Timestamp.from(instant2);

        List<NotificationDocument> expectedLogs = List.of(
                new NotificationDocument("1", Timestamp.from(Instant.now()), "Test Message 1", "9876543210", Status.pending, "Database"),
                new NotificationDocument("2", Timestamp.from(Instant.now()), "Test Message 2", "9123456789",Status.pending, "Database")
        );

        when(esRepository.findByCreatedAtBetween(timestamp1, timestamp2)).thenReturn(expectedLogs);

        List<NotificationDocument> actualLogs = esService.getLogsBetweenDates(start, end);

        assertEquals(2, actualLogs.size());
        assertEquals("9876543210", actualLogs.get(0).getPhoneNumber());
        assertEquals("Test Message 1", actualLogs.get(0).getMessage());

        verify(esRepository).findByCreatedAtBetween(timestamp1, timestamp2);
    }

    @Test
    void testGetLogsContainingMessage() {
        String message = "Test";

        List<NotificationDocument> expectedLogs = List.of(
                new NotificationDocument("1", Timestamp.from(Instant.now()), "Test Message 1", "9876543210", Status.pending, "Database"),
                new NotificationDocument("2", Timestamp.from(Instant.now()), "Test Message 2", "9123456789",Status.pending, "Database")
        );

        when(esRepository.findByMessageContaining(message)).thenReturn(expectedLogs);

        List<NotificationDocument> actualLogs = esService.getLogsContainingMessage(message);

        assertEquals(2, actualLogs.size());
        assertTrue(actualLogs.get(0).getMessage().contains("Test"));
        assertTrue(actualLogs.get(1).getMessage().contains("Test"));

        verify(esRepository).findByMessageContaining(message);
    }
}
