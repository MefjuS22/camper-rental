package com.camper.rental.dto.fleet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CamperModelDto {
    private Long id;
    private String brand;
    private String model;
    private Integer productionYear;
    private Long categoryId;
}
