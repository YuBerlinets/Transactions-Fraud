package ua.berlinets.transactions_fraud.dto.statistics;

import lombok.Data;
import ua.berlinets.transactions_fraud.dto.fraudAlert.FraudAlertListDTO;
import ua.berlinets.transactions_fraud.dto.transactions.Transaction;
import ua.berlinets.transactions_fraud.dto.transactions.TransactionListDTO;

import java.util.List;

@Data
public class StatisticsDTO {
    private List<TimeWindowStats> timeWindowStats;
    private List<TransactionListDTO> lastTransactions;
    private List<FraudAlertListDTO> lastFraudAlerts;
}
