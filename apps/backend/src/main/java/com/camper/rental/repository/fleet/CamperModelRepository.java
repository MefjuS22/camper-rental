package com.camper.rental.repository.fleet;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camper.rental.entity.fleet.CamperModel;

public interface CamperModelRepository extends JpaRepository<CamperModel, Long> {
}
