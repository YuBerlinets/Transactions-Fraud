package ua.berlinets.transactions_fraud.services.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ua.berlinets.transactions_fraud.dto.transactions.Transaction;
import ua.berlinets.transactions_fraud.services.TransactionService;

@Service
@Slf4j
@AllArgsConstructor
@EnableKafkaStreams
@EnableKafka
public class TransactionConsumer {
    private final TransactionService transactionService;

    @KafkaListener(
            topics = "transactions",
            groupId = "transaction-group",
            containerFactory = "transactionContainerFactory"
    )
    public void consumeTransaction(@Payload Transaction transaction) {
        try {
            log.info("Received transaction from topic: {}", transaction);
            transactionService.recordTransaction(transaction);
        } catch (Exception e) {
            log.error("Error processing transaction: {}", transaction, e);
        }
    }
}