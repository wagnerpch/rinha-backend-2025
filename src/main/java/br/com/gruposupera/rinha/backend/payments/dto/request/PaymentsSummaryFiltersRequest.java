package br.com.gruposupera.rinha.backend.payments.dto.request;

import java.time.Instant;

public record PaymentsSummaryFiltersRequest (
    Instant from,
    Instant to
) {}