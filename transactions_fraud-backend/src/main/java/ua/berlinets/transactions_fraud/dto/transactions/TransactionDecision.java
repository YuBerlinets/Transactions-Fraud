package ua.berlinets.transactions_fraud.dto.transactions;

import lombok.Data;

@Data
public class TransactionDecision {
    private String transactionId;
    private boolean isFraud;
    private String reason;
}
