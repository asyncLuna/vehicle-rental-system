package dev.asyncluna.rental.rental;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, UUID> {}
