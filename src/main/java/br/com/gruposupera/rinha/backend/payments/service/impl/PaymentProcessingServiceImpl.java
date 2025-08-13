package br.com.gruposupera.rinha.backend.payments.service.impl;

import br.com.gruposupera.rinha.backend.payments.dto.request.ExternalPaymentRequest;
import br.com.gruposupera.rinha.backend.payments.dto.request.IncomingPaymentRequest;
import br.com.gruposupera.rinha.backend.payments.handler.PaymentProcessorHandler;
import br.com.gruposupera.rinha.backend.payments.service.PaymentProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentProcessingServiceImpl implements PaymentProcessingService {

    private final PaymentProcessorHandler paymentProcessorChain;

    public void process(IncomingPaymentRequest request) {
        ExternalPaymentRequest externalPaymentRequest = new ExternalPaymentRequest(
                request.correlationId(),request.amount(), Instant.now());
        paymentProcessorChain.handle(externalPaymentRequest);
    }
}