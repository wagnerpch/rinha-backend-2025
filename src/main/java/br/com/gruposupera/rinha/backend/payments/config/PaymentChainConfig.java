package br.com.gruposupera.rinha.backend.payments.config;

import br.com.gruposupera.rinha.backend.payments.handler.PaymentProcessorHandler;
import br.com.gruposupera.rinha.backend.payments.handler.impl.DefaultPaymentProcessorHandler;
import br.com.gruposupera.rinha.backend.payments.handler.impl.FallbackPaymentProcessorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PaymentChainConfig {

    @Bean
    @Primary
    public PaymentProcessorHandler paymentProcessorChain(
            DefaultPaymentProcessorHandler defaultHandler,
            FallbackPaymentProcessorHandler fallbackHandler
    ) {
        defaultHandler.setNext(fallbackHandler);
        fallbackHandler.setNext(null);
        return defaultHandler;
    }
}