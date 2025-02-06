package ua.berlinets.transactions_fraud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class TransactionsFraudApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionsFraudApplication.class, args);
    }

}
