package ua.berlinets.transactions_fraud.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;
import ua.berlinets.transactions_fraud.repositories.FraudAlertRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class FraudAlertService {
    private final FraudAlertRepository fraudAlertRepository;

    public List<FraudAlert> getAllFraudAlerts() {
        return fraudAlertRepository.findAll();
    }
}
