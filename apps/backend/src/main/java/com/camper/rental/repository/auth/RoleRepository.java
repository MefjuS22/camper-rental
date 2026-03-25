package com.camper.rental.repository.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import com.camper.rental.entity.auth.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @EntityGraph(attributePaths = {"permissions"})
    Optional<Role> findByName(String name);
}
