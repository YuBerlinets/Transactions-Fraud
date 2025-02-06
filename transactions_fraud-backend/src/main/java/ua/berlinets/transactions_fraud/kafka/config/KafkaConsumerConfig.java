package ua.berlinets.transactions_fraud.kafka.config;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ua.berlinets.transactions_fraud.dto.transactions.Transaction;
import ua.berlinets.transactions_fraud.entities.mongo.FraudAlert;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.StringDeserializer;


@Configuration
@AllArgsConstructor
public class KafkaConsumerConfig {

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, FraudAlert> fraudAlertConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                fraudAlertDeserializer()
        );
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, FraudAlert>>
    fraudAlertContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FraudAlert> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(fraudAlertConsumerFactory());
        return factory;
    }

    @Bean
    public JsonDeserializer<FraudAlert> fraudAlertDeserializer() {
        JsonDeserializer<FraudAlert> deserializer = new JsonDeserializer<>(FraudAlert.class);
        configureDeserializer(deserializer);
        return deserializer;
    }


    @Bean
    public ConsumerFactory<String, Transaction> transactionConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                transactionDeserializer()
        );
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Transaction>>
    transactionContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Transaction> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionConsumerFactory());
        return factory;
    }

    @Bean
    public JsonDeserializer<Transaction> transactionDeserializer() {
        JsonDeserializer<Transaction> deserializer = new JsonDeserializer<>(Transaction.class);
        configureDeserializer(deserializer);
        return deserializer;
    }

    private void configureDeserializer(JsonDeserializer<?> deserializer) {
        deserializer.addTrustedPackages("*");
        deserializer.setTypeMapper(new DefaultJackson2JavaTypeMapper());
        deserializer.getTypeMapper().addTrustedPackages("*");
        deserializer.setRemoveTypeHeaders(false);
    }
}