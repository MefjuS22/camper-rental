package com.camper.rental.dto.reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
    private UUID publicId;
    private String customerEmail;
    private Long camperId;
    private String camperRegistrationNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private BigDecimal totalPrice;
    private String notes;
}
