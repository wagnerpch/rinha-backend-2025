package br.com.gruposupera.rinha.backend.payments.messaging.producer;

import br.com.gruposupera.rinha.backend.payments.config.RabbitMQConfig;
import br.com.gruposupera.rinha.backend.payments.messaging.dto.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSuccessProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendPaymentSuccess(PaymentSuccessEvent event) {

        log.info("Enviando evento de pagamento para a exchange: {}", event.correlationId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_PAYMENT_SUCCESS,
                event
        );
    }
}