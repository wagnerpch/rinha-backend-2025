package br.com.gruposupera.rinha.backend.payments.service;

import br.com.gruposupera.rinha.backend.payments.dto.ProcessorSummaryProjection;
import br.com.gruposupera.rinha.backend.payments.dto.request.PaymentsSummaryFiltersRequest;

import java.util.List;

public interface SummaryService {

    List<ProcessorSummaryProjection> getSummaryPayments(
            PaymentsSummaryFiltersRequest filters);
}