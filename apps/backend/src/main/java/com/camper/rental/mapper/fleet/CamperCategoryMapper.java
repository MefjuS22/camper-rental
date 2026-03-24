package com.camper.rental.mapper.fleet;

import org.mapstruct.Mapper;

import com.camper.rental.dto.fleet.CamperCategoryDto;
import com.camper.rental.entity.fleet.CamperCategory;

@Mapper(componentModel = "spring")
public interface CamperCategoryMapper {
    CamperCategoryDto toDto(CamperCategory entity);
}
