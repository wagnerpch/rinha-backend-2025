package br.com.gruposupera.rinha.backend.payments.exception;

public class ProcessorUnavailableException extends RuntimeException {
    public ProcessorUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}