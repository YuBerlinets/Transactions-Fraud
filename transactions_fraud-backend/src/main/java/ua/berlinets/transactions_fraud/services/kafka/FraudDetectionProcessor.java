package ua.berlinets.transactions_fraud.services.kafka;

import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.berlinets.transactions_fraud.dto.transactions.PreviousTransactionRequestDTO;
import ua.berlinets.transactions_fraud.dto.transactions.Transaction;
import ua.berlinets.transactions_fraud.dto.transactions.TransactionDecision;
import ua.berlinets.transactions_fraud.dto.transactions.TransactionRequestDTO;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;
import ua.berlinets.transactions_fraud.entities.transactions.PreviousTransaction;
import ua.berlinets.transactions_fraud.entities.transactions.TransactionData;
import ua.berlinets.transactions_fraud.entities.transactions.UserTransactionState;
import ua.berlinets.transactions_fraud.services.ExternalService;

import java.time.Duration;
import java.time.Instant;
import java.util.*;


import java.util.stream.Collectors;

@Service
public class FraudDetectionProcessor implements Processor<String, Transaction, String, FraudAlert> {
    private ProcessorContext<String, FraudAlert> context;
    private KeyValueStore<String, UserTransactionState> stateStore;

    private final ModelMapper modelMapper;
    private final ExternalService externalService;

    private static final String FRAUD_STORE = "fraud-store";

    public FraudDetectionProcessor(ModelMapper modelMapper, ExternalService externalService) {
        this.modelMapper = modelMapper;
        this.externalService = externalService;
    }

    @Override
    public void init(ProcessorContext<String, FraudAlert> context) {
        this.context = context;
        this.stateStore = context.getStateStore(FRAUD_STORE);
    }

    @Override
    public void process(Record<String, Transaction> record) {
        String userId = record.key();
        Transaction transaction = record.value();
        Instant timestamp = transaction.getTimestamp();

        UserTransactionState state = stateStore.get(userId);
        if (state == null) {
            state = new UserTransactionState(timestamp, new ArrayList<>());
        } else {
            state.setMaxTimestamp(state.getMaxTimestamp().isAfter(timestamp) ? state.getMaxTimestamp() : timestamp);
        }

        PreviousTransaction newTransaction = new PreviousTransaction(
                transaction.getUserId(), transaction.getAmount(),
                transaction.getType(), transaction.getTimestamp(),
                transaction.getMerchant(), transaction.getLocation()
        );
        state.getTransactions().add(newTransaction);

        Instant cutoff = state.getMaxTimestamp().minus(Duration.ofMinutes(10));
        state.setTransactions(state.getTransactions()
                .stream()
                .filter(t -> !t.getTimestamp().isBefore(cutoff))
                .collect(Collectors.toList()));

        TransactionRequestDTO request = modelMapper.map(transaction, TransactionRequestDTO.class);
        request.setTimestamp(transaction.getTimestamp().toString());

        List<PreviousTransactionRequestDTO> previousTransactions = state.getTransactions()
                .stream()
                .limit(3)
                .map(prev -> {
                    PreviousTransactionRequestDTO dto = modelMapper.map(prev, PreviousTransactionRequestDTO.class);
                    dto.setTimestamp(prev.getTimestamp().toString());
                    return dto;
                })
                .collect(Collectors.toList());

        request.setPreviousTransactions(previousTransactions);

        System.out.println("Sending request: " + request);
        TransactionDecision result = externalService.sendTransactionToAnalysis(request);
        System.out.println("Received result: " + result);

        if (result != null && result.isFraud()) {
            FraudAlert alert = new FraudAlert(
                    UUID.randomUUID().toString(),
                    userId,
                    transaction.getTransactionId(),
                    transaction.getAmount(),
                    transaction.getTimestamp(),
                    transaction.getLocation()
            );
            transaction.setFraud(true);
            context.forward(new Record<>(userId, alert, System.currentTimeMillis()));
            stateStore.put(userId, null);
        } else {
            stateStore.put(userId, state);
        }
    }

    @Override
    public void close() {

    }
}
