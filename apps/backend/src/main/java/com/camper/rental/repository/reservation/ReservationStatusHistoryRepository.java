package com.camper.rental.repository.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camper.rental.entity.reservation.ReservationStatusHistory;

public interface ReservationStatusHistoryRepository extends JpaRepository<ReservationStatusHistory, Long> {
}
