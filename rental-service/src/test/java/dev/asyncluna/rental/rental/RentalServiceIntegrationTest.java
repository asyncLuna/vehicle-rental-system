package dev.asyncluna.rental.rental;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class RentalServiceIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16");
    @Container
    static RabbitMQContainer rabbitMqContainer = new RabbitMQContainer("rabbitmq:3-management");

    @DynamicPropertySource
    static void registerServiceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", postgresContainer::getUsername);
        propertyRegistry.add("spring.datasource.password", postgresContainer::getPassword);
        propertyRegistry.add("spring.rabbitmq.host", rabbitMqContainer::getHost);
        propertyRegistry.add("spring.rabbitmq.port", rabbitMqContainer::getAmqpPort);
    }

    @Test
    void contextLoads() {
    }
}
