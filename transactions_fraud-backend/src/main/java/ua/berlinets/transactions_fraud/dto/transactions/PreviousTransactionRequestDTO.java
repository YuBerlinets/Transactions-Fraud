package ua.berlinets.transactions_fraud.dto.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreviousTransactionRequestDTO {
    private String userId;
    private double amount;
    private String type;
    private String timestamp;
    private String merchant;
    private String location;
}
