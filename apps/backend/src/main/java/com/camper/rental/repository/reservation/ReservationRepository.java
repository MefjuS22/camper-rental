package com.camper.rental.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.camper.rental.entity.reservation.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"items", "items.camper", "user"})
    List<Reservation> findAllByUserEmailOrderByCreatedAtDesc(String email);

    @EntityGraph(attributePaths = {"items", "items.camper", "user"})
    List<Reservation> findAllByOrderByCreatedAtDesc();

    Optional<Reservation> findByPublicId(UUID publicId);

    @Query("""
        select count(r) > 0
        from Reservation r
        join r.items i
        where i.camper.id = :camperId
          and r.status in ('PENDING_PAYMENT', 'CONFIRMED')
          and r.startDate <= :endDate
          and r.endDate >= :startDate
        """)
    boolean existsOverlappingReservation(Long camperId, LocalDate startDate, LocalDate endDate);
}
