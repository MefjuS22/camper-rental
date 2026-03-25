package com.camper.rental.mapper.reservation;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.camper.rental.dto.reservation.ReservationResponseDto;
import com.camper.rental.entity.reservation.Reservation;
import com.camper.rental.entity.reservation.ReservationItem;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "customerEmail", source = "user.email")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "camperId", source = "items", qualifiedByName = "firstCamperId")
    @Mapping(target = "camperRegistrationNumber", source = "items", qualifiedByName = "firstCamperRegistration")
    ReservationResponseDto toDto(Reservation reservation);

    @Named("mapStatus")
    default String mapStatus(com.camper.rental.entity.reservation.ReservationStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("firstCamperId")
    default Long firstCamperId(List<ReservationItem> items) {
        if (items == null || items.isEmpty() || items.getFirst().getCamper() == null) {
            return null;
        }
        return items.getFirst().getCamper().getId();
    }

    @Named("firstCamperRegistration")
    default String firstCamperRegistration(List<ReservationItem> items) {
        if (items == null || items.isEmpty() || items.getFirst().getCamper() == null) {
            return null;
        }
        return items.getFirst().getCamper().getRegistrationNumber();
    }
}
