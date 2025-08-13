package br.com.gruposupera.rinha.backend.payments.external.clients.impl;

import br.com.gruposupera.rinha.backend.payments.dto.request.ExternalPaymentRequest;
import br.com.gruposupera.rinha.backend.payments.exception.ProcessorUnavailableException;
import br.com.gruposupera.rinha.backend.payments.external.clients.ProcessorClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class FallbackProcessorClient implements ProcessorClient {

    private final WebClient webClient;

    public FallbackProcessorClient(@Qualifier("fallbackProcessorWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    @CircuitBreaker(name = "fallbackPay", fallbackMethod = "errorFallbackPay")
    public void pay(ExternalPaymentRequest request) {
        log.info("Enviando pagamento {} para o processador FALLBACK.", request.correlationId());
        webClient.post()
                .uri("/payments")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private void errorFallbackPay(ExternalPaymentRequest request, Throwable t) {
        log.warn("Circuit Breaker ativado para o processador FALLBACK no pagamento {}. Causa: {}", request.correlationId(), t.getMessage());
        throw new ProcessorUnavailableException("Fallback Processor está indisponível", t);
    }
}
