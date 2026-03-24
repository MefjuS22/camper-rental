package com.camper.rental.dto.reservation;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequestDto {
    @NotNull(message = "Camper id is required")
    @Positive(message = "Camper id must be greater than zero")
    private Long camperId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Price per day is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price per day must be greater than zero")
    private BigDecimal pricePerDay;

    @Size(max = 1000, message = "Notes max length is 1000")
    private String notes;

    @AssertTrue(message = "End date must be equal or after start date")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }
}
