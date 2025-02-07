package ua.berlinets.transactions_fraud.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.berlinets.transactions_fraud.dto.fraudAlert.FraudAlertListDTO;
import ua.berlinets.transactions_fraud.dto.statistics.StatisticsDTO;
import ua.berlinets.transactions_fraud.dto.statistics.TimeWindowStats;
import ua.berlinets.transactions_fraud.dto.transactions.Transaction;
import ua.berlinets.transactions_fraud.dto.transactions.TransactionListDTO;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {
    private final ConcurrentHashMap<Long, TimeWindowStats> statistics = new ConcurrentHashMap<>();
    private final Deque<Transaction> lastTransactions = new LinkedList<>();
    private final Deque<FraudAlert> lastFraudAlerts = new LinkedList<>();
    private final ModelMapper modelMapper;
    private static final int MAX_HISTORY = 15;
    private static final long WINDOW_SIZE_MS = 3 * 60 * 1000;

    public void recordTransaction(Transaction transaction) {
        long windowStart = getWindowStart(transaction.getTimestamp().toEpochMilli());
        statistics.compute(windowStart, (key, stats) -> {
            if (stats == null) {
                stats = new TimeWindowStats(windowStart);
            }
            stats.incrementTotal();
            stats.incrementApproved();
            return stats;
        });
        synchronized (lastTransactions) {
            lastTransactions.addFirst(transaction);
            if (lastTransactions.size() > MAX_HISTORY) {
                lastTransactions.removeLast();
            }
        }
    }

    public void recordFraudAlert(FraudAlert fraudAlert) {
        long windowStart = getWindowStart(fraudAlert.getTimestamp().toEpochMilli());
        statistics.compute(windowStart, (key, stats) -> {
            if (stats == null) {
                stats = new TimeWindowStats(windowStart);
            }
//            stats.incrementTotal();
            stats.incrementBlocked();
            stats.decrementApproved();
            return stats;
        });
        synchronized (lastFraudAlerts) {
            lastFraudAlerts.addFirst(fraudAlert);
            if (lastFraudAlerts.size() > MAX_HISTORY) {
                lastFraudAlerts.removeLast();
            }
        }
    }

    private long getWindowStart(long timestamp) {
        return (timestamp / WINDOW_SIZE_MS) * WINDOW_SIZE_MS;
    }

    public List<TimeWindowStats> getWindowStats() {
        List<TimeWindowStats> result = new ArrayList<>();
        long currentWindow = getWindowStart(System.currentTimeMillis());

        statistics.forEach((windowStart, stats) -> {
            if (currentWindow - windowStart <= 24 * 60 * 60 * 1000) {
                result.add(stats.copy());
            }
        });

        result.sort(Comparator.comparingLong(TimeWindowStats::getWindowStart));
        return result;
    }

    public StatisticsDTO getStatistics() {
        StatisticsDTO dto = new StatisticsDTO();
        dto.setTimeWindowStats(getWindowStats());
        dto.setLastTransactions(mapTransactions(new ArrayList<>(lastTransactions)));
        dto.setLastFraudAlerts(mapFraudAlerts(new ArrayList<>(lastFraudAlerts)));
        return dto;
    }

    private List<FraudAlertListDTO> mapFraudAlerts(List<FraudAlert> fraudAlerts) {
        List<FraudAlertListDTO> result = new ArrayList<>();
        for (FraudAlert alert : fraudAlerts) {
            result.add(modelMapper.map(alert, FraudAlertListDTO.class));
        }
        return result;
    }

    public List<TransactionListDTO> mapTransactions(List<Transaction> transactions) {
        List<TransactionListDTO> result = new ArrayList<>();
        for (Transaction transaction : transactions)
            result.add(modelMapper.map(transaction, TransactionListDTO.class));

        return result;
    }
}
