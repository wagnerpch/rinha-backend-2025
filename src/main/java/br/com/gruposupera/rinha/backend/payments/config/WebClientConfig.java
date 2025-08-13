package br.com.gruposupera.rinha.backend.payments.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "defaultProcessorWebClient")
    public WebClient defaultProcessorWebClient(@Value("${payment.processor.default.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean(name = "fallbackProcessorWebClient")
    public WebClient fallbackProcessorWebClient(@Value("${payment.processor.fallback.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}