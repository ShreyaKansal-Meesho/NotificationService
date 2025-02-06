package org.shreya.notificationservicenew.kafka;

import jakarta.validation.constraints.NotNull;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.shreya.notificationservicenew.service.KafkaService;
import org.shreya.notificationservicenew.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    private KafkaService kafkaService;

    public KafkaConsumer(KafkaService kafkaService) { this.kafkaService = kafkaService; }

    @KafkaListener(topics = Constants.KAFKA_NOTIFICATION_TOPIC, groupId = Constants.KAFKA_GROUP_ID)
    public void consume(@NotNull ConsumerRecord<String, String> record) {
        String request_id = record.value();
        kafkaService.processNotification(request_id);
    }
}
