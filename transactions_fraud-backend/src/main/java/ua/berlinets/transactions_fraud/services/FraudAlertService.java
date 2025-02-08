package ua.berlinets.transactions_fraud.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.berlinets.transactions_fraud.dto.response.PaginationResponse;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;
import ua.berlinets.transactions_fraud.repositories.FraudAlertRepository;


import java.util.List;

@Service
@AllArgsConstructor
public class FraudAlertService {
    private final FraudAlertRepository fraudAlertRepository;
    private final ModelMapper modelMapper;

    public List<FraudAlert> getAllFraudAlerts() {
        return fraudAlertRepository.findAll();
    }

    public PaginationResponse<FraudAlert> getAllFraudAlertsPagination(Pageable pageable) {
        PaginationResponse<FraudAlert> response = new PaginationResponse<>();
        Page<FraudAlert> fraudAlertsPage = fraudAlertRepository.findAll(pageable);
        response.setTotalPages(fraudAlertsPage.getTotalPages());
        response.setTotalElements(fraudAlertsPage.getTotalElements());
        response.setContent(fraudAlertsPage.getContent());
        return response;
    }

    public void saveFraudAlert(FraudAlert fraudAlert) {
        fraudAlertRepository.save(fraudAlert);
    }
}
