package com.camper.rental.repository.fleet;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camper.rental.entity.fleet.Camper;

public interface CamperRepository extends JpaRepository<Camper, Long> {

    List<Camper> findAllByStatus(String status);

    Optional<Camper> findByRegistrationNumber(String registrationNumber);
}
