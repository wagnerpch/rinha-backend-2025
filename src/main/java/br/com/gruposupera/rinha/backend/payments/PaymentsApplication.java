package br.com.gruposupera.rinha.backend.payments;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@SpringBootApplication
@EnableScheduling
@EnableRabbit
public class PaymentsApplication {
	public static void main(String[] args) {
		SpringApplication.run(PaymentsApplication.class, args);
	}
}