package com.camper.rental.controller.reservation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camper.rental.dto.reservation.ReservationResponseDto;
import com.camper.rental.dto.reservation.UpdateReservationStatusRequestDto;
import com.camper.rental.service.reservation.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/reservations")
@RequiredArgsConstructor
@Tag(name = "Admin Reservations")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @Operation(summary = "List all reservations", description = "Returns full reservations list for admin panel.")
    public ResponseEntity<List<ReservationResponseDto>> listAll() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PatchMapping("/{publicId}/status")
    @Operation(summary = "Update reservation status", description = "Updates reservation status by reservation publicId.")
    public ResponseEntity<ReservationResponseDto> updateStatus(
        @PathVariable UUID publicId,
        @Valid @RequestBody UpdateReservationStatusRequestDto requestDto
    ) {
        return ResponseEntity.ok(reservationService.updateReservationStatus(publicId, requestDto));
    }
}
