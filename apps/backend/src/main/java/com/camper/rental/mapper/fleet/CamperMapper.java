package com.camper.rental.mapper.fleet;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.camper.rental.dto.fleet.CamperRequestDto;
import com.camper.rental.dto.fleet.CamperResponseDto;
import com.camper.rental.entity.fleet.Camper;

@Mapper(componentModel = "spring", uses = {CamperModelMapper.class})
public interface CamperMapper {

    @Mapping(target = "model", source = "camperModel")
    CamperResponseDto toDto(Camper camper);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "camperModel", ignore = true)
    Camper toEntity(CamperRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "camperModel", ignore = true)
    void updateEntity(CamperRequestDto requestDto, @MappingTarget Camper camper);
}
