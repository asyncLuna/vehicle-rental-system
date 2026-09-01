package dev.asyncluna.rental.vehicle;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @PostMapping
    Vehicle create(@RequestBody Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @GetMapping
    List<Vehicle> all(@RequestParam(required = false) Vehicle.Category category, @RequestParam(required = false) Vehicle.Status status) {
        return vehicleRepository.findAll().stream().filter(vehicle -> category == null || vehicle.category == category).filter(vehicle -> status == null || vehicle.status == status).toList();
    }

    @GetMapping("/{id}")
    Vehicle get(@PathVariable UUID id) {
        return vehicleRepository.findById(id).orElseThrow();
    }

    @PatchMapping("/{id}/status")
    Vehicle status(@PathVariable UUID id, @RequestParam Vehicle.Status requestedStatus) {
        Vehicle vehicle = get(id);
        vehicle.status = requestedStatus;
        return vehicleRepository.save(vehicle);
    }
}
