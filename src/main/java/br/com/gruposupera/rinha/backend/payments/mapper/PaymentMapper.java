package br.com.gruposupera.rinha.backend.payments.mapper;

import br.com.gruposupera.rinha.backend.payments.dto.ProcessorSummaryProjection;
import br.com.gruposupera.rinha.backend.payments.dto.response.PaymentSummary;
import br.com.gruposupera.rinha.backend.payments.dto.response.Processor;
import br.com.gruposupera.rinha.backend.payments.enums.ProcessorTypeEnum;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    default PaymentSummary toPaymentSummary(List<ProcessorSummaryProjection> projections) {
        Processor defaultSummary = new Processor(BigDecimal.ZERO, BigDecimal.ZERO);
        Processor fallbackSummary = new Processor(BigDecimal.ZERO, BigDecimal.ZERO);

        for (ProcessorSummaryProjection projection : projections) {
            Processor processor = new Processor(new BigDecimal(projection.totalRequests()), projection.totalAmount());
            if (projection.processorType() == ProcessorTypeEnum.DEFAULT) {
                defaultSummary = processor;
            } else if (projection.processorType() == ProcessorTypeEnum.FALLBACK) {
                fallbackSummary = processor;
            }
        }
        return new PaymentSummary(defaultSummary, fallbackSummary);
    }
}