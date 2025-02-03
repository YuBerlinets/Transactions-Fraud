package ua.berlinets.transactions_fraud.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;

import java.util.List;

@Repository
public interface FraudAlertRepository extends MongoRepository<FraudAlert, String> {
    List<FraudAlert> findByUserId(String userId);
}