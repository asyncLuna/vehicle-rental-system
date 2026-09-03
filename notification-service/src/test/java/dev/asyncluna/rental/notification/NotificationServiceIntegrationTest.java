package dev.asyncluna.rental.notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.*;

@SpringBootTest
@Testcontainers
class NotificationServiceIntegrationTest {
    @Container
    static RabbitMQContainer rabbitMqContainer = new RabbitMQContainer("rabbitmq:3-management");

    @DynamicPropertySource
    static void registerRabbitMqProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.rabbitmq.host", rabbitMqContainer::getHost);
        propertyRegistry.add("spring.rabbitmq.port", rabbitMqContainer::getAmqpPort);
    }

    @Test
    void contextLoads() {}
}
