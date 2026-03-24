package com.camper.rental.repository.fleet;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camper.rental.entity.fleet.CamperCategory;

public interface CamperCategoryRepository extends JpaRepository<CamperCategory, Long> {
}
