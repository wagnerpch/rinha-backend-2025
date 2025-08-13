package br.com.gruposupera.rinha.backend.payments.health;

import br.com.gruposupera.rinha.backend.payments.dto.HealthCheckResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessorHealthMonitor {

    private final HealthStatusCache healthStatusCache;
    private final WebClient defaultProcessorWebClient;
    private final WebClient fallbackProcessorWebClient;

    @Scheduled(fixedRate = 6100)
    public void checkProcessorsHealth() {
        log.debug("Verificando saúde dos processadores...");

        defaultProcessorWebClient.get().uri("/payments/service-health")
                .retrieve()
                .bodyToMono(HealthCheckResponse.class)
                .doOnSuccess(response -> {
                    healthStatusCache.setDefaultProcessorFailing(response.failing());
                    log.info("Saúde do processador DEFAULT verificada. Marcando como: {}", response.failing());
                })
                .doOnError(error -> {
                    log.warn("Falha ao verificar saúde do processador DEFAULT. Marcando como indisponível.");
                    healthStatusCache.setDefaultProcessorFailing(true);
                })
                .subscribe();

        fallbackProcessorWebClient.get().uri("/payments/service-health")
                .retrieve()
                .bodyToMono(HealthCheckResponse.class)
                .doOnSuccess(response -> {
                    healthStatusCache.setFallbackProcessorFailing(response.failing());
                    log.info("Saúde do processador FALLBACK verificada. Marcando como: {}", response.failing());
                })
                .doOnError(error -> {
                    log.warn("Falha ao verificar saúde do processador FALLBACK. Marcando como indisponível.");
                    healthStatusCache.setFallbackProcessorFailing(true);
                })
                .subscribe();
    }
}