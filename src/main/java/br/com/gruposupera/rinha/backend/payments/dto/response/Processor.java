package br.com.gruposupera.rinha.backend.payments.dto.response;

import java.math.BigDecimal;

public record Processor (
    BigDecimal totalRequests,
    BigDecimal totalAmount
) {}