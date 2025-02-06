package ua.berlinets.transactions_fraud.dto.transactions;

import lombok.Data;

import java.time.Instant;

@Data
public class TransactionListDTO {
    private String transactionId;
    private String userId;
    private double amount;
    private Instant timestamp;
}
