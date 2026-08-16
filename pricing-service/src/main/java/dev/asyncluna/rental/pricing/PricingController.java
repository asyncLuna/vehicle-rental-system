package dev.asyncluna.rental.pricing;

import org.springframework.web.bind.annotation.*;

import java.math.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/pricing")
public class PricingController {
    @GetMapping("/calculate")
    public Price calculate(@RequestParam String vehicleCategory, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        long days = Math.max(1, ChronoUnit.DAYS.between(startDate, endDate));
        BigDecimal rate = switch (vehicleCategory.toUpperCase()) {
            case "COMPACT" -> BigDecimal.valueOf(40);
            case "SUV" -> BigDecimal.valueOf(70);
            case "LUXURY" -> BigDecimal.valueOf(120);
            default -> BigDecimal.valueOf(30);
        };
        BigDecimal discount = days >= 30 ? BigDecimal.valueOf(.8) : days >= 7 ? BigDecimal.valueOf(.9) : BigDecimal.ONE;
        return new Price(days, rate.multiply(BigDecimal.valueOf(days)).multiply(discount));
    }

    public record Price(long days, BigDecimal totalPrice) {
    }
}
