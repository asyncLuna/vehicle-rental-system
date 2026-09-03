package dev.asyncluna.rental.vehicle;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @NotBlank
    @Column(unique = true)
    public String registrationNumber;

    @NotBlank
    public String make;

    @NotBlank
    public String model;

    @Enumerated(EnumType.STRING)
    public Category category;

    public BigDecimal dailyRate;

    @Enumerated(EnumType.STRING)
    public Status status = Status.AVAILABLE;

    protected Vehicle() {}

    public enum Category {
        ECONOMY,
        COMPACT,
        SUV,
        LUXURY
    }

    public enum Status {
        AVAILABLE,
        RESERVED,
        RENTED,
        MAINTENANCE
    }
}
