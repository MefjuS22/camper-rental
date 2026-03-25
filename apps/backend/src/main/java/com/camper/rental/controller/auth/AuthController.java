package com.camper.rental.controller.auth;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camper.rental.dto.auth.JwtResponseDto;
import com.camper.rental.dto.auth.LoginRequestDto;
import com.camper.rental.dto.auth.CurrentUserDto;
import com.camper.rental.dto.auth.RegisterRequestDto;
import com.camper.rental.service.auth.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates user credentials and returns a JWT token.")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Creates a new account and returns a JWT token.")
    public ResponseEntity<JwtResponseDto> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        return ResponseEntity.ok(authService.register(requestDto));
    }

    @GetMapping("/me")
    @Operation(summary = "Current user profile", description = "Returns current authenticated user profile and roles.")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CurrentUserDto> me(Authentication authentication) {
        return ResponseEntity.ok(authService.currentUser(authentication.getName()));
    }
}
