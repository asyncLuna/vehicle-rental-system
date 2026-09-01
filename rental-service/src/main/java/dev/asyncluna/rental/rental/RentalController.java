package dev.asyncluna.rental.rental;

import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

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
    @ResponseStatus(HttpStatus.CREATED)
    Rental create(@Valid @RequestBody Rental request) {
        request.status = Rental.Status.PENDING;
        request.totalPrice = BigDecimal.ZERO;
        Rental savedRental = rentalRepository.save(request);
        rabbitTemplate.convertAndSend("vehicle-rental-system.notifications", savedRental.id.toString());
        return savedRental;
    }

    @GetMapping("/{id}")
    Rental get(@PathVariable UUID id) {
        return rentalRepository.findById(id).orElseThrow();
    }
}
