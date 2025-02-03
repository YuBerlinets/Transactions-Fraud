package ua.berlinets.transactions_fraud.entities.mongo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "fraud_alerts")
public class FraudAlert {
    @Id
    private String id;
    private String userId;
    private double amount;
    private Instant timestamp;
    private String location;
    private Set<String> locations;
}