package ua.berlinets.transactions_fraud.kafka.serdes.transaction;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;

public class FraudAlertSerde extends Serdes.WrapperSerde<FraudAlert> {
    public FraudAlertSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(FraudAlert.class));
    }
}
