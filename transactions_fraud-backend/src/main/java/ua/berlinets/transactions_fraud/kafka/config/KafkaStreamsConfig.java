package ua.berlinets.transactions_fraud.kafka.config;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
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
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.client.RestTemplate;
import ua.berlinets.transactions_fraud.dto.transactions.Transaction;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;
import ua.berlinets.transactions_fraud.entities.transactions.UserTransactionState;
import ua.berlinets.transactions_fraud.services.ExternalService;
import ua.berlinets.transactions_fraud.services.kafka.FraudDetectionProcessor;

import java.util.Map;

@EnableKafka
@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    //    @Bean
//    public StreamsConfig kafkaStreamsConfig() {
//        Properties props = new Properties();
//        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "transactions_fraud");
//        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:21092");
//        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
//        return new StreamsConfig(props);
//    }
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


