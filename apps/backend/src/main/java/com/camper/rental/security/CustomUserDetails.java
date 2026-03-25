package com.camper.rental.security;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.camper.rental.entity.auth.User;
import com.camper.rental.entity.auth.PermissionEntity;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Set<String> roleNames;
    private final Set<Permission> permissions;

    public CustomUserDetails(User user) {
        this.user = user;

        this.roleNames = user.getRoles().stream()
            .map(role -> role.getName().trim().toUpperCase(Locale.ROOT))
            .collect(Collectors.toSet());

        this.permissions = user.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(PermissionEntity::getCode)
            .filter(Objects::nonNull)
            .map(code -> code.trim().toUpperCase(Locale.ROOT))
            .map(code -> {
                try {
                    return Permission.valueOf(code);
                } catch (IllegalArgumentException ex) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        this.authorities = this.permissions.stream()
            .map(permission -> permission.name())
            .map(SimpleGrantedAuthority::new)
            .toList();
    }

    public Set<String> getRoleNames() {
        return roleNames;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<String> getPermissionNames() {
        return permissions.stream()
            .map(Permission::name)
            .collect(Collectors.toSet());
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
