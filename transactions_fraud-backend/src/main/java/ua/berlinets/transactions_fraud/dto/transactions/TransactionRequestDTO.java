package ua.berlinets.transactions_fraud.dto.transactions;

import lombok.Data;
import ua.berlinets.transactions_fraud.entities.transactions.PreviousTransaction;

import java.time.Instant;
import java.util.List;

@Data
public class TransactionRequestDTO {
    private String transactionId;
    private String userId;
    private double amount;
    private String timestamp;
    private String merchant;
    private String type;
    private String location;
    private List<PreviousTransactionRequestDTO> previousTransactions;
}
