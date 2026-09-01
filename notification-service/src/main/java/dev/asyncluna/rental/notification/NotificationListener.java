package dev.asyncluna.rental.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    @RabbitListener(queues = "vehicle-rental-system.notifications")
    public void receive(String rentalEvent) {
        System.out.println("Notification dispatched: " + rentalEvent);
    }
}
