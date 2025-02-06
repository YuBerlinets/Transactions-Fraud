package ua.berlinets.transactions_fraud.kafka.config;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.client.RestTemplate;
import ua.berlinets.transactions_fraud.dto.transactions.Transaction;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;
import ua.berlinets.transactions_fraud.entities.transactions.UserTransactionState;
import ua.berlinets.transactions_fraud.services.ExternalService;
import ua.berlinets.transactions_fraud.services.kafka.FraudDetectionProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfig {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Topology topology(StreamsBuilder streamsBuilder, ExternalService externalService, ModelMapper modelMapper) {
        StoreBuilder<KeyValueStore<String, UserTransactionState>> storeBuilder = Stores
                .keyValueStoreBuilder(
                        Stores.persistentKeyValueStore("fraud-store"),
                        Serdes.String(),
                        new JsonSerde<>(UserTransactionState.class)
                );

        streamsBuilder.addStateStore(storeBuilder);

        JsonSerde<FraudAlert> fraudAlertSerde = new JsonSerde<>(FraudAlert.class);
        fraudAlertSerde.serializer().configure(
                Map.of(JsonSerializer.TYPE_MAPPINGS, "fraudAlert:ua.berlinets.transactions_fraud.entities.mongo.FraudAlert"),
                false
        );
        fraudAlertSerde.deserializer().configure(
                Map.of(JsonDeserializer.TRUSTED_PACKAGES, "*",
                        JsonDeserializer.TYPE_MAPPINGS, "fraudAlert:ua.berlinets.transactions_fraud.entities.mongo.FraudAlert"),
                false
        );

        streamsBuilder.stream("transactions", Consumed.with(Serdes.String(), new JsonSerde<>(Transaction.class)))
                .process(() -> new FraudDetectionProcessor(modelMapper, externalService), "fraud-store")
                .to("fraud-alerts", Produced.with(Serdes.String(), fraudAlertSerde));

        return streamsBuilder.build();
    }
}


