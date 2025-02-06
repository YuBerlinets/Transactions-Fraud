package ua.berlinets.transactions_fraud.controllers;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import ua.berlinets.transactions_fraud.dto.statistics.StatisticsDTO;
import ua.berlinets.transactions_fraud.dto.statistics.TimeWindowStats;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;
import ua.berlinets.transactions_fraud.entities.transactions.TransactionData;
import ua.berlinets.transactions_fraud.services.TransactionService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final TransactionService statisticsService;

    @Scheduled(fixedRate = 5000)
    public void sendStatisticsUpdates() {
        StatisticsDTO stats = statisticsService.getStatistics();
        messagingTemplate.convertAndSend("/topic/statistics", stats);
    }
}
