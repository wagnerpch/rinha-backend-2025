package br.com.gruposupera.rinha.backend.payments.messaging.consumer;

import br.com.gruposupera.rinha.backend.payments.config.RabbitMQConfig;
import br.com.gruposupera.rinha.backend.payments.dto.request.ExternalPaymentRequest;
import br.com.gruposupera.rinha.backend.payments.messaging.dto.PaymentErrorEvent;
import br.com.gruposupera.rinha.backend.payments.service.PaymentProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentErrorConsumer {

    private final PaymentProcessingService paymentProcessingService;

    @RabbitListener(
            id = "errorListener",
            queues = RabbitMQConfig.Q_PAYMENT_ERROR,
            autoStartup = "false"
    )
    public void onPaymentError(PaymentErrorEvent errorEvent) {
        log.info("Reprocessando pagamento com erro da fila: {}", errorEvent.correlationId());
        ExternalPaymentRequest requestToReprocess = new ExternalPaymentRequest(
                errorEvent.correlationId(),
                errorEvent.amount(),
                errorEvent.timestamp()
        );
        paymentProcessingService.reprocess(requestToReprocess);
    }
}