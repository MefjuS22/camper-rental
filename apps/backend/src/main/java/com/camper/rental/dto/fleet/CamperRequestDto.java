package com.camper.rental.dto.fleet;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CamperRequestDto {
    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "VIN is required")
    private String vin;

    @NotNull(message = "Mileage is required")
    @Min(value = 0, message = "Mileage cannot be negative")
    private Integer mileage;

    @NotBlank(message = "Camper status is required")
    private String status;

    @NotNull(message = "Camper model id is required")
    private Long modelId;
}
