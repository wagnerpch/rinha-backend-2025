package br.com.gruposupera.rinha.backend.payments.messaging.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentErrorEvent(
    UUID correlationId,
    BigDecimal amount,
    String errorMessage,
    Instant timestamp
) {}