package com.camper.rental.dto.fleet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CamperResponseDto {
    private Long id;
    private String registrationNumber;
    private String vin;
    private Integer mileage;
    private String status;
    private CamperModelDto model;
}
