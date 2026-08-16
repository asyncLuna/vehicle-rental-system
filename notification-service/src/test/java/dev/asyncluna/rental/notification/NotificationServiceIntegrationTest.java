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
    static RabbitMQContainer mq = new RabbitMQContainer("rabbitmq:3-management");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.rabbitmq.host", mq::getHost);
        r.add("spring.rabbitmq.port", mq::getAmqpPort);
    }

    @Test
    void contextLoads() {
    }
}
