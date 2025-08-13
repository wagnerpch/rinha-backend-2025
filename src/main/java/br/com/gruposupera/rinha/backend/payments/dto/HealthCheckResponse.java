package br.com.gruposupera.rinha.backend.payments.dto;

public record HealthCheckResponse(
   boolean failing,
   int minResponseTime
) {}