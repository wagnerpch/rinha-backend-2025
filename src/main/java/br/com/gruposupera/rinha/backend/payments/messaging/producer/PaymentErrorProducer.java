package br.com.gruposupera.rinha.backend.payments.messaging.producer;

import br.com.gruposupera.rinha.backend.payments.config.RabbitMQConfig;
import br.com.gruposupera.rinha.backend.payments.dto.request.ExternalPaymentRequest;
import br.com.gruposupera.rinha.backend.payments.messaging.dto.PaymentErrorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentErrorProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendPaymentError(ExternalPaymentRequest request) {
        log.info("Enviando evento de pagamento com erro para a exchange: {}", request.correlationId());
        
        PaymentErrorEvent errorEvent = new PaymentErrorEvent(
                request.correlationId(),
                request.amount(),
                "Todos os processadores indisponíveis",
                Instant.now()
        );
        
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_PAYMENT_ERROR,
                errorEvent
        );
    }
}