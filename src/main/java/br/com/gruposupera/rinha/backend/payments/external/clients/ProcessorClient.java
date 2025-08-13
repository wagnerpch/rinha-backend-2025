package br.com.gruposupera.rinha.backend.payments.external.clients;

import br.com.gruposupera.rinha.backend.payments.dto.request.ExternalPaymentRequest;

public interface ProcessorClient {
    void pay(ExternalPaymentRequest request);
}