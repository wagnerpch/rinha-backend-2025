package br.com.gruposupera.rinha.backend.payments.handler.impl;

import br.com.gruposupera.rinha.backend.payments.dto.request.ExternalPaymentRequest;
import br.com.gruposupera.rinha.backend.payments.enums.ProcessorTypeEnum;
import br.com.gruposupera.rinha.backend.payments.exception.ProcessorUnavailableException;
import br.com.gruposupera.rinha.backend.payments.external.clients.impl.FallbackProcessorClient;
import br.com.gruposupera.rinha.backend.payments.handler.PaymentProcessorHandler;
import br.com.gruposupera.rinha.backend.payments.health.HealthStatusCache;
import br.com.gruposupera.rinha.backend.payments.messaging.dto.PaymentSuccessEvent;
import br.com.gruposupera.rinha.backend.payments.messaging.producer.PaymentSuccessProducer;
import br.com.gruposupera.rinha.backend.payments.messaging.producer.PaymentErrorProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class FallbackPaymentProcessorHandler implements PaymentProcessorHandler {

    private final FallbackProcessorClient fallbackProcessorClient;
    private final PaymentSuccessProducer paymentSuccessProducer;
    private final PaymentErrorProducer paymentErrorProducer;
    private final HealthStatusCache healthStatusCache;
    private PaymentProcessorHandler nextHandler;

    @Override
    public void setNext(PaymentProcessorHandler next) {
        this.nextHandler = next;
    }

    @Override
    public void handle(ExternalPaymentRequest request) {
        if (!healthStatusCache.isFallbackProcessorFailing()) {
            log.warn("Processador FALLBACK marcado como indisponível pelo Health Check. Fim da cadeia de processamento.");
            if (nextHandler != null) {
                nextHandler.handle(request);
            } else {
                log.error("Fim da cadeia. Nenhum processador disponível para o pagamento {}.", request.correlationId());
                paymentErrorProducer.sendPaymentError(request);
            }
            return;
        }

        try {
            fallbackProcessorClient.pay(request);
            log.info("Pagamento {} processado com sucesso pelo processador FALLBACK.", request.correlationId());
            PaymentSuccessEvent paymentSuccessEvent = new PaymentSuccessEvent(
                    request.correlationId(),
                    request.amount(),
                    ProcessorTypeEnum.FALLBACK,
                    Instant.now());
            paymentSuccessProducer.sendPaymentSuccess(paymentSuccessEvent);
        } catch (ProcessorUnavailableException e) {
            log.error("Falha ao processar com o FALLBACK (via Circuit Breaker). Fim da cadeia de processamento.");
            if (nextHandler != null) {
                nextHandler.handle(request);
            } else {
                log.error("Fim da cadeia. Nenhum processador disponível para o pagamento {}.", request.correlationId());
                paymentErrorProducer.sendPaymentError(request);
            }
        }
    }
}