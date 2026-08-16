package dev.asyncluna.rental.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    @RabbitListener(queues = "vehicle-rental-system.notifications")
    public void receive(String event) {
        System.out.println("Notification dispatched: " + event);
    }
}
