package dev.asyncluna.rental.vehicle;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @PostMapping
    @CircuitBreaker(name = "vehicleService", fallbackMethod = "serviceUnavailable")
    public Vehicle create(@RequestBody Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @GetMapping
    @CircuitBreaker(name = "vehicleService", fallbackMethod = "serviceUnavailable")
    public List<Vehicle> all(
            @RequestParam(required = false) Vehicle.Category category,
            @RequestParam(required = false) Vehicle.Status status) {
        return vehicleRepository.findAll().stream()
                .filter(vehicle -> category == null || vehicle.category == category)
                .filter(vehicle -> status == null || vehicle.status == status)
                .toList();
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "vehicleService", fallbackMethod = "serviceUnavailable")
    public Vehicle get(@PathVariable UUID id) {
        return vehicleRepository.findById(id).orElseThrow();
    }

    @PatchMapping("/{id}/status")
    @CircuitBreaker(name = "vehicleService", fallbackMethod = "serviceUnavailable")
    public Vehicle status(@PathVariable UUID id, @RequestParam Vehicle.Status requestedStatus) {
        Vehicle vehicle = get(id);
        vehicle.status = requestedStatus;
        return vehicleRepository.save(vehicle);
    }

    private Vehicle serviceUnavailable(Vehicle ignored, Throwable cause) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE, "Vehicle service temporarily unavailable", cause);
    }

    private List<Vehicle> serviceUnavailable(Vehicle.Category category, Vehicle.Status status, Throwable cause) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE, "Vehicle service temporarily unavailable", cause);
    }

    private Vehicle serviceUnavailable(UUID ignored, Throwable cause) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE, "Vehicle service temporarily unavailable", cause);
    }

    private Vehicle serviceUnavailable(UUID ignored, Vehicle.Status requestedStatus, Throwable cause) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE, "Vehicle service temporarily unavailable", cause);
    }
}
