package ua.berlinets.transactions_fraud.entities.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactionState {
    private Instant maxTimestamp;
    private List<PreviousTransaction> transactions;
}
