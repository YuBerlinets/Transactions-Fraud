package ua.berlinets.transactions_fraud.services.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;
import ua.berlinets.transactions_fraud.repositories.FraudAlertRepository;

@Service
@Slf4j
@AllArgsConstructor
@EnableKafkaStreams
@EnableKafka
public class FraudAlertConsumer {
    private final FraudAlertRepository fraudAlertRepository;

    @KafkaListener(topics = "fraud-alerts", groupId = "fraud-group")
    public void consumeFraudAlert(@Payload FraudAlert alert) {
        try {
            log.info("Received fraud alert: {}", alert);

            fraudAlertRepository.save(alert);
        } catch (Exception e) {
            log.error("Error processing fraud alert: {}", alert, e);
        }
    }

}