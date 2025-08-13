package br.com.gruposupera.rinha.backend.payments.service.impl;

import br.com.gruposupera.rinha.backend.payments.dto.ProcessorSummaryProjection;
import br.com.gruposupera.rinha.backend.payments.dto.request.PaymentsSummaryFiltersRequest;
import br.com.gruposupera.rinha.backend.payments.repository.PaymentRepository;
import br.com.gruposupera.rinha.backend.payments.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final transient PaymentRepository paymentRepository;

    @Override
    public List<ProcessorSummaryProjection> getSummaryPayments(PaymentsSummaryFiltersRequest filters) {
        Instant from = filters.from() != null ? filters.from() : null;
        Instant to = filters.to() != null ? filters.to() : null;
        return paymentRepository.getSummary(from, to);
    }
}
