package br.com.gruposupera.rinha.backend.payments.health;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class HealthStatusCache {

    private final AtomicBoolean defaultProcessorFailing = new AtomicBoolean(false);
    private final AtomicBoolean fallbackProcessorFailing = new AtomicBoolean(false);

    public boolean isDefaultProcessorFailing() {
        return defaultProcessorFailing.get();
    }

    public void setDefaultProcessorFailing(boolean isFailing) {
        defaultProcessorFailing.set(isFailing);
    }

    public boolean isFallbackProcessorFailing() {
        return fallbackProcessorFailing.get();
    }

    public void setFallbackProcessorFailing(boolean isFailing) {
        fallbackProcessorFailing.set(isFailing);
    }
}