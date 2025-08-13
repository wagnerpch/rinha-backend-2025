package br.com.gruposupera.rinha.backend.payments.messaging.consumer;

import br.com.gruposupera.rinha.backend.payments.messaging.dto.PaymentSuccessEvent;
import br.com.gruposupera.rinha.backend.payments.entity.PaymentEntity;
import br.com.gruposupera.rinha.backend.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentPersistenceConsumer {

    private final transient PaymentRepository paymentRepository;

    @RabbitListener(queues = "q.payment.success")
    public void onPaymentSuccess(PaymentSuccessEvent event) {
        log.info("Recebido evento de pagamento da fila: {}", event.correlationId());

        PaymentEntity payment = new PaymentEntity();
        payment.setCorrelationId(event.correlationId());
        payment.setAmount(event.amount());
        payment.setProcessorType(event.processorType());
        payment.setProcessedAt(event.timestamp());

        try {
            paymentRepository.save(payment);
            log.info("Pagamento {} salvo no banco de dados.", event.correlationId());
        } catch (Exception e) {
            // Lidar com possíveis erros, como falha de constraint de 'correlationId' único.
            // Isso indica que a mensagem pode ter sido processada duas vezes.
            log.error("Erro ao salvar pagamento {}: {}", event.correlationId(), e.getMessage());
        }
    }
}