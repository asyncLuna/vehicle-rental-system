package dev.asyncluna.rental.rental;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @NotNull
    public UUID customerId;

    @NotNull
    public UUID vehicleId;

    @NotNull
    public LocalDate startDate;

    @NotNull
    public LocalDate endDate;

    @Enumerated(EnumType.STRING)
    public Status status = Status.PENDING;

    public BigDecimal totalPrice;

    protected Rental() {}

    public enum Status {
        PENDING,
        CONFIRMED,
        ACTIVE,
        COMPLETED,
        CANCELLED
    }
}
