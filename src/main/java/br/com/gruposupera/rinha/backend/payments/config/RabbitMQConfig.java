package br.com.gruposupera.rinha.backend.payments.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "ex.payments";
    public static final String Q_PAYMENT_SUCCESS = "q.payment.success";
    public static final String Q_PAYMENT_ERROR = "q.payment.error";
    public static final String RK_PAYMENT_SUCCESS = "rk.payment.success";
    public static final String RK_PAYMENT_ERROR = "rk.payment.error";

    @Bean
    public DirectExchange paymentsExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue paymentSuccessQueue() {
        return new Queue(Q_PAYMENT_SUCCESS, true, false, false);
    }

    @Bean
    public Queue paymentErrorQueue() {
        return new Queue(Q_PAYMENT_ERROR, true, false, false);
    }

    @Bean
    public Binding paymentSuccessBinding(Queue paymentSuccessQueue, DirectExchange paymentsExchange) {
        return BindingBuilder
                .bind(paymentSuccessQueue)
                .to(paymentsExchange)
                .with(RK_PAYMENT_SUCCESS);
    }

    @Bean
    public Binding paymentErrorBinding(Queue paymentErrorQueue, DirectExchange paymentsExchange) {
        return BindingBuilder
                .bind(paymentErrorQueue)
                .to(paymentsExchange)
                .with(RK_PAYMENT_ERROR);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}