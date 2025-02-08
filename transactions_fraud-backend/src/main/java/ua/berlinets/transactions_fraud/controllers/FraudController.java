package ua.berlinets.transactions_fraud.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;
import ua.berlinets.transactions_fraud.services.FraudAlertService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/fraud-alerts")
public class FraudController {

    private final FraudAlertService fraudAlertService;


    @GetMapping
    public ResponseEntity<?> getFraudAlerts(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(fraudAlertService.getAllFraudAlertsPagination(pageable));
    }
}
