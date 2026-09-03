package dev.asyncluna.rental.notification;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    @RabbitListener(queues = "vehicle-rental-system.notifications")
    @CircuitBreaker(name = "notificationService", fallbackMethod = "notificationUnavailable")
    public void receive(String rentalEvent) {
        System.out.println("Notification dispatched: " + rentalEvent);
    }

    private void notificationUnavailable(String rentalEvent, Throwable cause) {
        System.err.println("Notification temporarily unavailable for rental: " + rentalEvent);
    }
}
