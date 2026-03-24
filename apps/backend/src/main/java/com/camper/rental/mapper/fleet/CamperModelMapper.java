package com.camper.rental.mapper.fleet;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.camper.rental.dto.fleet.CamperModelDto;
import com.camper.rental.entity.fleet.CamperModel;

@Mapper(componentModel = "spring")
public interface CamperModelMapper {

    @Mapping(target = "categoryId", source = "camperCategory.id")
    CamperModelDto toDto(CamperModel entity);
}
