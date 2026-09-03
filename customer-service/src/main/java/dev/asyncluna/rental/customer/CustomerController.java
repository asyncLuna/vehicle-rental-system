package dev.asyncluna.rental.customer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping
    @CircuitBreaker(name = "customerService", fallbackMethod = "serviceUnavailable")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@Valid @RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "customerService", fallbackMethod = "serviceUnavailable")
    public Customer get(@PathVariable UUID id) {
        return customerRepository.findById(id).orElseThrow();
    }

    private Customer serviceUnavailable(Customer ignored, Throwable cause) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE, "Customer service temporarily unavailable", cause);
    }

    private Customer serviceUnavailable(UUID ignored, Throwable cause) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE, "Customer service temporarily unavailable", cause);
    }
}
