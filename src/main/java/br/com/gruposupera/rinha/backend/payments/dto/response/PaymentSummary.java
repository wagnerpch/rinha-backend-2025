package br.com.gruposupera.rinha.backend.payments.dto.response;

public record PaymentSummary (
    Processor processorDefault,
    Processor processorFallback
){}