package ua.berlinets.transactions_fraud.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;
import ua.berlinets.transactions_fraud.services.FraudAlertService;

import java.util.List;

@RestController
@RequestMapping("/api/fraud")
@AllArgsConstructor
public class FraudController {

    private final FraudAlertService fraudAlertService;

    @GetMapping("/alerts")
    public List<FraudAlert> getFraudAlerts() {
        return fraudAlertService.getAllFraudAlerts();
    }
}
