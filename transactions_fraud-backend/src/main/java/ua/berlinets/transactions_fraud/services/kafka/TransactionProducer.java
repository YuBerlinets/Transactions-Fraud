package ua.berlinets.transactions_fraud.services.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ua.berlinets.transactions_fraud.dto.transactions.Transaction;

@Service
@AllArgsConstructor
public class TransactionProducer {
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public void sendTransaction(Transaction transaction) {
        kafkaTemplate.send("transactions", transaction.getUserId(), transaction);
        System.out.println("Sent transaction: " + transaction);
    }
}

