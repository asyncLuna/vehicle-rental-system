package dev.asyncluna.rental.customer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerRepository repo;

    public CustomerController(CustomerRepository r) {
        repo = r;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Customer create(@Valid @RequestBody Customer c) {
        return repo.save(c);
    }

    @GetMapping("/{id}")
    Customer get(@PathVariable UUID id) {
        return repo.findById(id).orElseThrow();
    }
}
