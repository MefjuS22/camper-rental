package com.camper.rental.dto.reservation;

import com.camper.rental.entity.reservation.ReservationStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReservationStatusRequestDto {
    @NotNull(message = "Status is required")
    private ReservationStatus status;

    @Size(max = 500, message = "Comment max length is 500")
    private String comment;
}
