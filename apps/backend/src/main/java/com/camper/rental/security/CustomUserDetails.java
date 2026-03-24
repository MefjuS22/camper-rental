package com.camper.rental.security;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.camper.rental.entity.auth.User;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.user = user;
        Set<String> authorityNames = user.getRoles().stream()
            .map(role -> role.getName().trim().toUpperCase(Locale.ROOT))
            .map(roleName -> roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName)
            .collect(Collectors.toSet());
        this.authorities = authorityNames.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
    }

    public UUID getPublicId() {
        return user.getPublicId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
