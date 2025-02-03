package ua.berlinets.transactions_fraud.kafka.serdes.transaction;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ua.berlinets.transactions_fraud.dto.transactions.Transaction;

public class TransactionSerde extends Serdes.WrapperSerde<Transaction> {
    public TransactionSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(Transaction.class));
    }
}



