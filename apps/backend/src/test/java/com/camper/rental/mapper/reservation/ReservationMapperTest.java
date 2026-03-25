package com.camper.rental.mapper.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import com.camper.rental.dto.reservation.ReservationResponseDto;
import com.camper.rental.entity.auth.User;
import com.camper.rental.entity.fleet.Camper;
import com.camper.rental.entity.reservation.Reservation;
import com.camper.rental.entity.reservation.ReservationItem;
import com.camper.rental.entity.reservation.ReservationStatus;

class ReservationMapperTest {

    private final ReservationMapper mapper = Mappers.getMapper(ReservationMapper.class);

    @Test
    void toDto_shouldMapAllFieldsIncludingFirstItemCamper() {
        User user = new User();
        user.setEmail("test@example.com");

        Camper camper = new Camper();
        ReflectionTestUtils.setField(camper, "id", 44L, Long.class);
        camper.setRegistrationNumber("KR44TEST");

        ReservationItem item = new ReservationItem();
        item.setCamper(camper);
        item.setDaysCount(2);
        item.setPricePerDay(BigDecimal.valueOf(250));

        Reservation reservation = new Reservation();
        reservation.setPublicId(UUID.randomUUID());
        reservation.setUser(user);
        reservation.setStartDate(LocalDate.of(2026, 5, 10));
        reservation.setEndDate(LocalDate.of(2026, 5, 11));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setTotalPrice(BigDecimal.valueOf(500));
        reservation.setNotes("Note");
        reservation.setItems(List.of(item));

        ReservationResponseDto dto = mapper.toDto(reservation);

        assertThat(dto.getCustomerEmail()).isEqualTo("test@example.com");
        assertThat(dto.getCamperId()).isEqualTo(44L);
        assertThat(dto.getCamperRegistrationNumber()).isEqualTo("KR44TEST");
        assertThat(dto.getStatus()).isEqualTo("CONFIRMED");
        assertThat(dto.getTotalPrice()).isEqualByComparingTo("500");
    }

    @Test
    void toDto_shouldMapNullStatusSafely() {
        Reservation reservation = new Reservation();
        reservation.setItems(List.of());

        ReservationResponseDto dto = mapper.toDto(reservation);

        assertThat(dto.getStatus()).isNull();
    }
}
