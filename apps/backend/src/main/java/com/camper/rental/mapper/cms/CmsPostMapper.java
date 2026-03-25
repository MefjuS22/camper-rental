package com.camper.rental.mapper.cms;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.camper.rental.dto.cms.CmsPostDto;
import com.camper.rental.dto.cms.UpsertCmsPostRequestDto;
import com.camper.rental.entity.cms.CmsPost;

@Mapper(componentModel = "spring")
public interface CmsPostMapper {

    CmsPostDto toDto(CmsPost entity);

    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    CmsPost toEntity(UpsertCmsPostRequestDto dto);

    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    void updateEntity(UpsertCmsPostRequestDto dto, @MappingTarget CmsPost entity);
}
