package br.com.gruposupera.rinha.backend.payments.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record IncomingPaymentRequest(
    UUID correlationId,
    BigDecimal amount
) {}