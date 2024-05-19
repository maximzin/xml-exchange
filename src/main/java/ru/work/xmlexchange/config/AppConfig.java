package ru.work.xmlexchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public WebClientConfig webClientConfig() {
        return new WebClientConfig();
    }

}