package ua.berlinets.transactions_fraud.services;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.berlinets.transactions_fraud.dto.transactions.TransactionDecision;
import ua.berlinets.transactions_fraud.dto.transactions.TransactionRequestDTO;

@Service
public class ExternalService {
    private final String ML_MODEL_URL;
    private final RestTemplate restTemplate;

    public ExternalService(RestTemplate restTemplate, Dotenv dotenv) {
        this.ML_MODEL_URL = dotenv.get("ML_MODEL_URL");
        this.restTemplate = restTemplate;
    }

    public TransactionDecision sendTransactionToAnalysis(TransactionRequestDTO request) {
        return restTemplate.postForObject(ML_MODEL_URL, request, TransactionDecision.class);
    }
}
