package dev.asyncluna.rental.vehicle;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleRepository repo;

    public VehicleController(VehicleRepository r) {
        repo = r;
    }

    @PostMapping
    Vehicle create(@RequestBody Vehicle v) {
        return repo.save(v);
    }

    @GetMapping
    List<Vehicle> all(@RequestParam(required = false) Vehicle.Category category, @RequestParam(required = false) Vehicle.Status status) {
        return repo.findAll().stream().filter(v -> category == null || v.category == category).filter(v -> status == null || v.status == status).toList();
    }

    @GetMapping("/{id}")
    Vehicle get(@PathVariable UUID id) {
        return repo.findById(id).orElseThrow();
    }

    @PatchMapping("/{id}/status")
    Vehicle status(@PathVariable UUID id, @RequestParam Vehicle.Status value) {
        Vehicle v = get(id);
        v.status = value;
        return repo.save(v);
    }
}
