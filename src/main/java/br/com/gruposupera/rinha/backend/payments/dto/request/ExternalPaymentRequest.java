package br.com.gruposupera.rinha.backend.payments.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ExternalPaymentRequest(
    UUID correlationId,
    BigDecimal amount,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    Instant requestedAt
) {}