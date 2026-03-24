package com.camper.rental.service.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camper.rental.dto.reservation.CreateReservationRequestDto;
import com.camper.rental.dto.reservation.ReservationResponseDto;
import com.camper.rental.dto.reservation.UpdateReservationStatusRequestDto;
import com.camper.rental.entity.auth.User;
import com.camper.rental.entity.fleet.Camper;
import com.camper.rental.entity.reservation.Reservation;
import com.camper.rental.entity.reservation.ReservationItem;
import com.camper.rental.entity.reservation.ReservationStatus;
import com.camper.rental.entity.reservation.ReservationStatusHistory;
import com.camper.rental.exception.BusinessLogicException;
import com.camper.rental.exception.ResourceNotFoundException;
import com.camper.rental.mapper.reservation.ReservationMapper;
import com.camper.rental.repository.auth.UserRepository;
import com.camper.rental.repository.fleet.CamperRepository;
import com.camper.rental.repository.reservation.ReservationRepository;
import com.camper.rental.repository.reservation.ReservationStatusHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationStatusHistoryRepository reservationStatusHistoryRepository;
    private final UserRepository userRepository;
    private final CamperRepository camperRepository;
    private final ReservationMapper reservationMapper;

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getCurrentUserReservations(String userEmail) {
        return reservationRepository.findAllByUserEmailOrderByCreatedAtDesc(userEmail).stream()
            .map(reservationMapper::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getAllReservations() {
        return reservationRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(reservationMapper::toDto)
            .toList();
    }

    @Transactional
    public ReservationResponseDto createReservation(String userEmail, CreateReservationRequestDto dto) {
        if (reservationRepository.existsOverlappingReservation(dto.getCamperId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BusinessLogicException("Selected camper is not available for given date range.");
        }

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Authenticated user was not found."));
        Camper camper = camperRepository.findById(dto.getCamperId())
            .orElseThrow(() -> new ResourceNotFoundException("Camper resource was not found for id: " + dto.getCamperId()));

        long days = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);
        reservation.setNotes(dto.getNotes());
        reservation.setTotalPrice(dto.getPricePerDay().multiply(BigDecimal.valueOf(days)));

        ReservationItem item = new ReservationItem();
        item.setReservation(reservation);
        item.setCamper(camper);
        item.setDaysCount((int) days);
        item.setPricePerDay(dto.getPricePerDay());
        reservation.setItems(List.of(item));

        Reservation saved = reservationRepository.save(reservation);
        saveStatusHistory(saved, saved.getStatus(), "Reservation created");
        return reservationMapper.toDto(saved);
    }

    @Transactional
    public ReservationResponseDto updateReservationStatus(UUID publicId, UpdateReservationStatusRequestDto dto) {
        Reservation reservation = reservationRepository.findByPublicId(publicId)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation resource was not found for publicId: " + publicId));

        ReservationStatus nextStatus = dto.getStatus();

        reservation.setStatus(nextStatus);
        Reservation saved = reservationRepository.save(reservation);
        saveStatusHistory(saved, nextStatus, dto.getComment());
        return reservationMapper.toDto(saved);
    }

    @Transactional
    public void cancelOwnReservation(String userEmail, UUID publicId) {
        Reservation reservation = reservationRepository.findByPublicId(publicId)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation resource was not found for publicId: " + publicId));

        if (!reservation.getUser().getEmail().equalsIgnoreCase(userEmail)) {
            throw new BusinessLogicException("You are not allowed to cancel this reservation.");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        saveStatusHistory(reservation, ReservationStatus.CANCELLED, "Cancelled by customer");
    }

    private void saveStatusHistory(Reservation reservation, ReservationStatus status, String comment) {
        ReservationStatusHistory history = new ReservationStatusHistory();
        history.setReservation(reservation);
        history.setStatus(status);
        history.setChangedAt(LocalDateTime.now());
        history.setComment(comment);
        reservationStatusHistoryRepository.save(history);
    }

}
