package ua.berlinets.transactions_fraud.entities.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreviousTransaction {
    private String userId;
    private double amount;
    private String type;
    private Instant timestamp;
    private String merchant;
    private String location;
}
