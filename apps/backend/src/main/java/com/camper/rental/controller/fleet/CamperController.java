package com.camper.rental.controller.fleet;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camper.rental.dto.fleet.CamperRequestDto;
import com.camper.rental.dto.fleet.CamperResponseDto;
import com.camper.rental.service.fleet.CamperService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/campers")
@RequiredArgsConstructor
@Tag(name = "Campers")
public class CamperController {

    private final CamperService camperService;

    @GetMapping
    @Operation(summary = "Get all campers", description = "Returns the full list of campers in fleet.")
    public ResponseEntity<List<CamperResponseDto>> getAllCampers() {
        return ResponseEntity.ok(camperService.getAllCampers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get camper by id", description = "Returns a single camper by its unique identifier.")
    public ResponseEntity<CamperResponseDto> getCamperById(@PathVariable Long id) {
        return ResponseEntity.ok(camperService.getCamperById(id));
    }

    @PostMapping
    @Operation(summary = "Create camper", description = "Creates a new camper entity in fleet.")
    public ResponseEntity<CamperResponseDto> createCamper(@RequestBody CamperRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(camperService.createCamper(requestDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update camper", description = "Updates an existing camper identified by id.")
    public ResponseEntity<CamperResponseDto> updateCamper(@PathVariable Long id, @RequestBody CamperRequestDto requestDto) {
        return ResponseEntity.ok(camperService.updateCamper(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete camper", description = "Deletes a camper from fleet by id.")
    public ResponseEntity<Void> deleteCamper(@PathVariable Long id) {
        camperService.deleteCamper(id);
        return ResponseEntity.noContent().build();
    }
}
