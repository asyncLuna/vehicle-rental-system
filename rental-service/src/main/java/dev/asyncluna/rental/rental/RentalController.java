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
    private final RentalRepository repo;
    private final RabbitTemplate rabbit;

    public RentalController(RentalRepository r, RabbitTemplate q) {
        repo = r;
        rabbit = q;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Rental create(@Valid @RequestBody Rental request) {
        request.status = Rental.Status.PENDING;
        request.totalPrice = BigDecimal.ZERO;
        Rental saved = repo.save(request);
        rabbit.convertAndSend("vehicle-rental-system.notifications", saved.id.toString());
        return saved;
    }

    @GetMapping("/{id}")
    Rental get(@PathVariable UUID id) {
        return repo.findById(id).orElseThrow();
    }
}
