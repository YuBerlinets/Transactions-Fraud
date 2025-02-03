package ua.berlinets.transactions_fraud.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure().directory("src/main/resources").load();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
