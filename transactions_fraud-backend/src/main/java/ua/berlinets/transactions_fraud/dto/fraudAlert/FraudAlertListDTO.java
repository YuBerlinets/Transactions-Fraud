package ua.berlinets.transactions_fraud.dto.fraudAlert;

import lombok.Data;

import java.time.Instant;

@Data
public class FraudAlertListDTO {
    private String id;
    private String userId;
    private String transactionId;
    private Instant timestamp;
}
