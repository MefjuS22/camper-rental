package com.camper.rental.service.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.camper.rental.dto.reservation.CreateReservationRequestDto;
import com.camper.rental.dto.reservation.ReservationResponseDto;
import com.camper.rental.entity.auth.Role;
import com.camper.rental.entity.auth.User;
import com.camper.rental.entity.fleet.Camper;
import com.camper.rental.entity.reservation.Reservation;
import com.camper.rental.entity.reservation.ReservationStatus;
import com.camper.rental.entity.reservation.ReservationStatusHistory;
import com.camper.rental.exception.BusinessLogicException;
import com.camper.rental.mapper.reservation.ReservationMapper;
import com.camper.rental.repository.auth.UserRepository;
import com.camper.rental.repository.fleet.CamperRepository;
import com.camper.rental.repository.reservation.ReservationRepository;
import com.camper.rental.repository.reservation.ReservationStatusHistoryRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationStatusHistoryRepository reservationStatusHistoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CamperRepository camperRepository;
    @Mock
    private ReservationMapper reservationMapper;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
            reservationRepository,
            reservationStatusHistoryRepository,
            userRepository,
            camperRepository,
            reservationMapper
        );
    }

    @Test
    void createReservation_shouldCalculateTotalAndCreateHistory() {
        String email = "user@example.com";
        CreateReservationRequestDto dto = CreateReservationRequestDto.builder()
            .camperId(10L)
            .startDate(LocalDate.of(2026, 4, 1))
            .endDate(LocalDate.of(2026, 4, 3))
            .pricePerDay(BigDecimal.valueOf(100))
            .notes("Test note")
            .build();

        User user = new User();
        user.setEmail(email);
        user.setRoles(Set.of(new Role()));

        Camper camper = new Camper();
        ReflectionTestUtils.setField(camper, "id", 10L, Long.class);
        camper.setRegistrationNumber("KR1TEST");

        Reservation saved = new Reservation();
        saved.setStatus(ReservationStatus.PENDING_PAYMENT);
        saved.setUser(user);
        saved.setPublicId(UUID.randomUUID());
        saved.setStartDate(dto.getStartDate());
        saved.setEndDate(dto.getEndDate());
        saved.setTotalPrice(BigDecimal.valueOf(300));

        ReservationResponseDto response = ReservationResponseDto.builder()
            .status("PENDING_PAYMENT")
            .totalPrice(BigDecimal.valueOf(300))
            .build();

        when(reservationRepository.existsOverlappingReservation(eq(10L), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(camperRepository.findById(10L)).thenReturn(Optional.of(camper));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(saved);
        when(reservationMapper.toDto(saved)).thenReturn(response);

        ReservationResponseDto result = reservationService.createReservation(email, dto);

        assertThat(result.getStatus()).isEqualTo("PENDING_PAYMENT");
        assertThat(result.getTotalPrice()).isEqualByComparingTo("300");

        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(reservationCaptor.capture());
        Reservation toSave = reservationCaptor.getValue();
        assertThat(toSave.getTotalPrice()).isEqualByComparingTo("300");
        assertThat(toSave.getStatus()).isEqualTo(ReservationStatus.PENDING_PAYMENT);
        assertThat(toSave.getItems()).hasSize(1);
        assertThat(toSave.getItems().getFirst().getDaysCount()).isEqualTo(3);

        verify(reservationStatusHistoryRepository).save(any(ReservationStatusHistory.class));
    }

    @Test
    void createReservation_shouldRejectWhenCamperIsNotAvailable() {
        CreateReservationRequestDto dto = CreateReservationRequestDto.builder()
            .camperId(10L)
            .startDate(LocalDate.of(2026, 4, 1))
            .endDate(LocalDate.of(2026, 4, 3))
            .pricePerDay(BigDecimal.valueOf(100))
            .build();

        when(reservationRepository.existsOverlappingReservation(eq(10L), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(true);

        assertThatThrownBy(() -> reservationService.createReservation("user@example.com", dto))
            .isInstanceOf(BusinessLogicException.class)
            .hasMessage("Selected camper is not available for given date range.");

        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(reservationStatusHistoryRepository, never()).save(any(ReservationStatusHistory.class));
    }
}
