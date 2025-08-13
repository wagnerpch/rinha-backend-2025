package br.com.gruposupera.rinha.backend.payments.service;

import br.com.gruposupera.rinha.backend.payments.dto.request.IncomingPaymentRequest;

public interface PaymentProcessingService {
    void process(IncomingPaymentRequest request);
}