package br.com.gruposupera.rinha.backend.payments.messaging.dto;

import br.com.gruposupera.rinha.backend.payments.enums.ProcessorTypeEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentSuccessEvent(
    UUID correlationId,
    BigDecimal amount,
    ProcessorTypeEnum processorType,
    Instant timestamp
) {}