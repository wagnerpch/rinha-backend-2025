package br.com.gruposupera.rinha.backend.payments.controller;

import br.com.gruposupera.rinha.backend.payments.dto.ProcessorSummaryProjection;
import br.com.gruposupera.rinha.backend.payments.dto.request.IncomingPaymentRequest;
import br.com.gruposupera.rinha.backend.payments.dto.request.PaymentsSummaryFiltersRequest;
import br.com.gruposupera.rinha.backend.payments.dto.response.PaymentSummary;
import br.com.gruposupera.rinha.backend.payments.mapper.PaymentMapper;
import br.com.gruposupera.rinha.backend.payments.service.PaymentProcessingService;
import br.com.gruposupera.rinha.backend.payments.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentProcessingService paymentProcessingService;
    private final SummaryService summaryService;
    private final PaymentMapper paymentMapper;

    @PostMapping("/payments")
    public ResponseEntity<Void> processPayment(@RequestBody IncomingPaymentRequest incomingPaymentRequest) {
        paymentProcessingService.process(incomingPaymentRequest);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/admin/payments")
    public ResponseEntity<PaymentSummary> getPaymentsSumary(PaymentsSummaryFiltersRequest filters) {
        List<ProcessorSummaryProjection> result = summaryService.getSummaryPayments(filters);
        PaymentSummary summaryResponse = paymentMapper.toPaymentSummary(result);
        return ResponseEntity.ok(summaryResponse);
    }
}