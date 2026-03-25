package com.camper.rental.service.auth;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camper.rental.dto.auth.JwtResponseDto;
import com.camper.rental.dto.auth.LoginRequestDto;
import com.camper.rental.dto.auth.RegisterRequestDto;
import com.camper.rental.dto.auth.CurrentUserDto;
import com.camper.rental.entity.auth.Role;
import com.camper.rental.entity.auth.PermissionEntity;
import com.camper.rental.entity.auth.User;
import com.camper.rental.exception.BusinessLogicException;
import com.camper.rental.repository.auth.RoleRepository;
import com.camper.rental.repository.auth.UserRepository;
import com.camper.rental.security.JwtService;
import com.camper.rental.security.CustomUserDetails;
import com.camper.rental.security.Permission;

import java.util.Objects;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String DEFAULT_ROLE = "USER";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public JwtResponseDto login(LoginRequestDto requestDto) {
        String normalizedEmail = requestDto.getEmail().trim().toLowerCase(Locale.ROOT);
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, requestDto.getPassword())
            );

            var principal = authentication.getPrincipal();
            String token = jwtService.generateToken(
                (org.springframework.security.core.userdetails.UserDetails) principal
            );

            List<String> roles;
            List<Permission> permissions;
            var publicId = principal instanceof CustomUserDetails customUserDetails
                ? customUserDetails.getPublicId()
                : userRepository.findByEmail(normalizedEmail).map(User::getPublicId).orElse(null);

            if (principal instanceof CustomUserDetails customUserDetails) {
                roles = customUserDetails.getRoleNames().stream().toList();
                permissions = customUserDetails.getPermissions().stream().toList();
            } else {
                User user = userRepository.findByEmail(normalizedEmail)
                    .orElseThrow(() -> new BusinessLogicException("Authenticated user was not found."));
                roles = user.getRoles().stream()
                    .map(Role::getName)
                    .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                    .map(role -> role.toUpperCase(Locale.ROOT))
                    .toList();
                permissions = user.getRoles().stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .map(PermissionEntity::getCode)
                    .filter(code -> code != null && !code.isBlank())
                    .map(code -> code.trim().toUpperCase(Locale.ROOT))
                    .map(code -> {
                        try {
                            return Permission.valueOf(code);
                        } catch (IllegalArgumentException ex) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
            }

            return JwtResponseDto.builder()
                .token(token)
                .publicId(publicId)
                .email(normalizedEmail)
                .roles(roles)
                .permissions(permissions)
                .build();
        } catch (BadCredentialsException ex) {
            throw new BusinessLogicException("Invalid email or password.");
        }
    }

    @Transactional
    public JwtResponseDto register(RegisterRequestDto requestDto) {
        String normalizedEmail = requestDto.getEmail().trim().toLowerCase(Locale.ROOT);
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new BusinessLogicException("An account with this email already exists.");
        }

        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE)
            .orElseThrow(() -> new BusinessLogicException("Default role USER is not configured."));

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(requestDto.getPassword()));
        user.setActive(true);
        user.setRoles(Set.of(defaultRole));

        User savedUser = userRepository.save(user);
        var userDetails = new org.springframework.security.core.userdetails.User(
            savedUser.getEmail(),
            savedUser.getPasswordHash(),
            defaultRoleAuthorities(savedUser.getRoles())
        );

        List<String> roles = savedUser.getRoles().stream()
            .map(Role::getName)
            .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
            .map(role -> role.toUpperCase(Locale.ROOT))
            .toList();
        List<Permission> permissions = savedUser.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(PermissionEntity::getCode)
            .filter(code -> code != null && !code.isBlank())
            .map(code -> code.trim().toUpperCase(Locale.ROOT))
            .map(code -> {
                try {
                    return Permission.valueOf(code);
                } catch (IllegalArgumentException ex) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .toList();

        return JwtResponseDto.builder()
            .token(jwtService.generateToken(userDetails))
            .publicId(savedUser.getPublicId())
            .email(savedUser.getEmail())
            .roles(roles)
            .permissions(permissions)
            .build();
    }

    @Transactional(readOnly = true)
    public CurrentUserDto currentUser(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessLogicException("Authenticated user was not found."));

        return CurrentUserDto.builder()
            .publicId(user.getPublicId())
            .email(user.getEmail())
            .roles(
                user.getRoles().stream()
                    .map(Role::getName)
                    .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                    .map(role -> role.toUpperCase(Locale.ROOT))
                    .toList()
            )
            .permissions(
                user.getRoles().stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .map(PermissionEntity::getCode)
                    .filter(code -> code != null && !code.isBlank())
                    .map(code -> code.trim().toUpperCase(Locale.ROOT))
                    .map(code -> {
                        try {
                            return Permission.valueOf(code);
                        } catch (IllegalArgumentException ex) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList()
            )
            .build();
    }

    private List<SimpleGrantedAuthority> defaultRoleAuthorities(Set<Role> roles) {
        return roles.stream()
            .map(Role::getName)
            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
            .map(SimpleGrantedAuthority::new)
            .toList();
    }
}
