package dev.asyncluna.rental.pricing;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.math.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/pricing")
public class PricingController {
    @GetMapping("/calculate")
    @CircuitBreaker(name = "pricingService", fallbackMethod = "serviceUnavailable")
    public Price calculate(
            @RequestParam String vehicleCategory, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        long rentalDays = Math.max(1, ChronoUnit.DAYS.between(startDate, endDate));
        BigDecimal dailyRate =
                switch (vehicleCategory.toUpperCase()) {
                    case "COMPACT" -> BigDecimal.valueOf(40);
                    case "SUV" -> BigDecimal.valueOf(70);
                    case "LUXURY" -> BigDecimal.valueOf(120);
                    default -> BigDecimal.valueOf(30);
                };
        BigDecimal discountFactor =
                rentalDays >= 30 ? BigDecimal.valueOf(.8) : rentalDays >= 7 ? BigDecimal.valueOf(.9) : BigDecimal.ONE;
        return new Price(
                rentalDays, dailyRate.multiply(BigDecimal.valueOf(rentalDays)).multiply(discountFactor));
    }

    private Price serviceUnavailable(String vehicleCategory, LocalDate startDate, LocalDate endDate, Throwable cause) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE, "Pricing service temporarily unavailable", cause);
    }

    public record Price(long days, BigDecimal totalPrice) {}
}
