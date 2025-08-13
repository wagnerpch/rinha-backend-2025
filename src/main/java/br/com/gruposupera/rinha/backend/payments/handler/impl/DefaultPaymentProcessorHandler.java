package br.com.gruposupera.rinha.backend.payments.handler.impl;

import br.com.gruposupera.rinha.backend.payments.dto.request.ExternalPaymentRequest;
import br.com.gruposupera.rinha.backend.payments.enums.ProcessorTypeEnum;
import br.com.gruposupera.rinha.backend.payments.exception.ProcessorUnavailableException;
import br.com.gruposupera.rinha.backend.payments.external.clients.impl.DefaultProcessorClient;
import br.com.gruposupera.rinha.backend.payments.handler.PaymentProcessorHandler;
import br.com.gruposupera.rinha.backend.payments.health.HealthStatusCache;
import br.com.gruposupera.rinha.backend.payments.messaging.dto.PaymentSuccessEvent;
import br.com.gruposupera.rinha.backend.payments.messaging.producer.PaymentSuccessProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultPaymentProcessorHandler implements PaymentProcessorHandler {

    private final DefaultProcessorClient defaultProcessorClient;
    private final PaymentSuccessProducer paymentSuccessProducer;
    private final HealthStatusCache healthStatusCache;
    private PaymentProcessorHandler nextHandler;

    @Override
    public void setNext(PaymentProcessorHandler next) {
        this.nextHandler = next;
    }

    @Override
    public void handle(ExternalPaymentRequest request) {
        if (!healthStatusCache.isDefaultProcessorFailing()) {
            log.warn("Processador DEFAULT marcado como indisponível pelo Health Check. Pulando para o próximo handler.");
            nextHandler.handle(request);
            return;
        }

        try {
            defaultProcessorClient.pay(request);
            log.info("Pagamento {} processado com sucesso pelo processador DEFAULT.", request.correlationId());
            PaymentSuccessEvent paymentSuccessEvent = new PaymentSuccessEvent(
                    request.correlationId(),
                    request.amount(),
                    ProcessorTypeEnum.DEFAULT,
                    Instant.now());
            paymentSuccessProducer.sendPaymentSuccess(paymentSuccessEvent);
        } catch (ProcessorUnavailableException e) {
            log.warn("Falha ao processar com o DEFAULT (via Circuit Breaker). Tentando o próximo handler.");
            if (nextHandler != null) {
                nextHandler.handle(request);
            } else {
                log.error("Fim da cadeia. Nenhum processador disponível para o pagamento {}.", request.correlationId());
            }
        }
    }
}