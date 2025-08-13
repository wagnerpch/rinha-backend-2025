package br.com.gruposupera.rinha.backend.payments.dto;

import br.com.gruposupera.rinha.backend.payments.enums.ProcessorTypeEnum;

import java.math.BigDecimal;

public record ProcessorSummaryProjection(
    ProcessorTypeEnum processorType,
    long totalRequests,
    BigDecimal totalAmount
) {}