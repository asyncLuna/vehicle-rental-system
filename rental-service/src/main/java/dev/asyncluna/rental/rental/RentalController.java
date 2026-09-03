package dev.asyncluna.rental.rental;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    private final RentalRepository rentalRepository;
    private final RabbitTemplate rabbitTemplate;

    public RentalController(RentalRepository rentalRepository, RabbitTemplate rabbitTemplate) {
        this.rentalRepository = rentalRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    @CircuitBreaker(name = "rentalService", fallbackMethod = "serviceUnavailable")
    @ResponseStatus(HttpStatus.CREATED)
    public Rental create(@Valid @RequestBody Rental request) {
        request.status = Rental.Status.PENDING;
        request.totalPrice = BigDecimal.ZERO;
        Rental savedRental = rentalRepository.save(request);
        rabbitTemplate.convertAndSend("vehicle-rental-system.notifications", savedRental.id.toString());
        return savedRental;
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "rentalService", fallbackMethod = "serviceUnavailable")
    public Rental get(@PathVariable UUID id) {
        return rentalRepository.findById(id).orElseThrow();
    }

    private Rental serviceUnavailable(Rental ignored, Throwable cause) {
        throw new ResponseStatusException(
                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                "Rental service temporarily unavailable",
                cause);
    }

    private Rental serviceUnavailable(UUID ignored, Throwable cause) {
        throw new ResponseStatusException(
                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                "Rental service temporarily unavailable",
                cause);
    }
}
