package org.shreya.notificationservicenew.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.shreya.notificationservicenew.model.Enum.Status;
import org.springframework.data.elasticsearch.annotations.Document;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@Document(indexName = "logs")
@Builder
public class NotificationDocument {
    @Id
    private String id;
    private Timestamp createdAt;
    private String message;
    private String phoneNumber;
    private Status logLevel;
    private String source;

    @PrePersist
    protected void onCreate() {
        id = UUID.randomUUID().toString();
    }
}







