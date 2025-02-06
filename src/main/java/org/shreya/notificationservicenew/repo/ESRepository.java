package org.shreya.notificationservicenew.repo;

import org.shreya.notificationservicenew.model.NotificationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Repository
public interface ESRepository extends ElasticsearchRepository<NotificationDocument, String> {
    List<NotificationDocument> findByCreatedAtBetween(Timestamp start, Timestamp end);
    List<NotificationDocument> findByMessageContaining(String message);
}
