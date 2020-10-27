package ru.otus.spring.course.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.course.config.indicator.HealthCheckIndicator;


/**
 * Применение фейкового HealthIndicator как метрики prometheus
 */
@Configuration
@RequiredArgsConstructor
public class MicrometerHealthConfig {

    private final HealthCheckIndicator healthCheckIndicator;

    @Bean
    public MeterRegistryCustomizer prometheusHealthCheck(HealthEndpoint healthEndpoint) {
        return registry -> registry.gauge("health", healthEndpoint, this::healthToCode);
    }

    private int healthToCode(HealthEndpoint healthEndpoint) {
        return healthCheckIndicator.health().getStatus().toString().equals("UP") ? 1 : 0;
    }
}
