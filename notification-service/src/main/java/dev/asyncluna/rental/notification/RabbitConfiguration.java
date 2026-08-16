package dev.asyncluna.rental.notification;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    @Bean
    Queue notificationQueue() {
        return new Queue("vehicle-rental-system.notifications", true);
    }
}
