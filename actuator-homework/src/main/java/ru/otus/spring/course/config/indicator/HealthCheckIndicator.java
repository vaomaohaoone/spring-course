package ru.otus.spring.course.config.indicator;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;


import static org.springframework.boot.actuate.health.Health.down;
import static org.springframework.boot.actuate.health.Health.up;

/**
 * Фейковый HealthIndicator
 */
@Component
public class HealthCheckIndicator implements HealthIndicator {
    @Override
    public Health health() {
        if (RandomUtils.nextInt(0, 1000) > 500) {
            return up().withDetail("message", "UP! Ha-ha").build();
        } else {
            return down().withDetail("message", "DOWN :( Cry...").build();
        }
    }
}
