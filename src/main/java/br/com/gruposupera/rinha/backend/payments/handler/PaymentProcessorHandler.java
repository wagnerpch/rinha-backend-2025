package br.com.gruposupera.rinha.backend.payments.handler;

import br.com.gruposupera.rinha.backend.payments.dto.request.ExternalPaymentRequest;

public interface PaymentProcessorHandler {
    void setNext(PaymentProcessorHandler next);
    void handle(ExternalPaymentRequest request);
}