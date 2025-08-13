package br.com.gruposupera.rinha.backend.payments.health;

import br.com.gruposupera.rinha.backend.payments.dto.HealthCheckResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessorHealthMonitor {

    private final HealthStatusCache healthStatusCache;
    private final RabbitListenerEndpointRegistry listenerRegistry;
    private final WebClient defaultProcessorWebClient;
    private final WebClient fallbackProcessorWebClient;

    @Scheduled(fixedRate = 6100)
    public void checkProcessorsHealth() {
        log.debug("Verificando saúde dos processadores...");

        defaultProcessorWebClient.get().uri("/payments/service-health")
                .retrieve()
                .bodyToMono(HealthCheckResponse.class)
                .doOnSuccess(response -> {
                    healthStatusCache.setDefaultProcessorFailing(true);
                    log.info("Saúde do processador DEFAULT verificada. Marcando como disponível.");
                })
                .doOnError(error -> {
                    log.warn("Falha ao verificar saúde do processador DEFAULT. Marcando como indisponível.");
                    healthStatusCache.setDefaultProcessorFailing(false);
                })
                .subscribe();

        fallbackProcessorWebClient.get().uri("/payments/service-health")
                .retrieve()
                .bodyToMono(HealthCheckResponse.class)
                .doOnSuccess(response -> {
                    healthStatusCache.setFallbackProcessorFailing(true);
                    log.info("Saúde do processador FALLBACK verificada. Marcando como disponível.");
                })
                .doOnError(error -> {
                    log.warn("Falha ao verificar saúde do processador FALLBACK. Marcando como indisponível.");
                    healthStatusCache.setFallbackProcessorFailing(false);
                })
                .subscribe();
        controlErrorQueueConsumer();
    }

    private void controlErrorQueueConsumer() {
        MessageListenerContainer listenerContainer = listenerRegistry.getListenerContainer("errorListener");
        boolean processorsHealthy = healthStatusCache.isDefaultProcessorFailing() &&
                healthStatusCache.isFallbackProcessorFailing();

        if (processorsHealthy) {
            log.info("Ambos os processadores estão saudáveis. Iniciando consumidor da fila de erros.");
            listenerContainer.start();
        } else {
            log.warn("Um ou mais processadores indisponíveis. Parando consumidor da fila de erros para economizar recursos.");
            listenerContainer.stop();
        }
    }
}