package ua.berlinets.transactions_fraud.entities.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionData {
    private Instant timestamp;
    private String location;
}
