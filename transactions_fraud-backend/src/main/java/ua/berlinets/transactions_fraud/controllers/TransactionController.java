package ua.berlinets.transactions_fraud.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.berlinets.transactions_fraud.dto.transactions.Transaction;
import ua.berlinets.transactions_fraud.services.ExternalService;
import ua.berlinets.transactions_fraud.services.TransactionService;
import ua.berlinets.transactions_fraud.services.kafka.TransactionProducer;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionProducer transactionProducer;
    private final TransactionService transactionService;
    private final ExternalService externalService;

    @PostMapping("/send")
    public ResponseEntity<?> sendTransaction(@RequestBody Transaction transaction) {
        transactionProducer.sendTransaction(transaction);
        return ResponseEntity.ok("Transaction sent!");
    }

    @PostMapping("/send-list")
    public ResponseEntity<?> sendTransactionList(@RequestBody Transaction[] transactions) {
        for (Transaction transaction : transactions) {
            transactionProducer.sendTransaction(transaction);
        }
        return ResponseEntity.ok("Transactions sent!");
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics() {
        return ResponseEntity.ok(transactionService.getStatistics());
    }
}

