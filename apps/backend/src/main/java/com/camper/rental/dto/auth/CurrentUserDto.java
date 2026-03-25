package com.camper.rental.dto.auth;

import java.util.List;
import java.util.UUID;

import com.camper.rental.security.Permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserDto {
    private UUID publicId;
    private String email;
    private List<String> roles;
    private List<Permission> permissions;
}
