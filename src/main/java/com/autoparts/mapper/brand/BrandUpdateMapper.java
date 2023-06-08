package com.autoparts.mapper.brand;

import com.autoparts.dto.brand.BrandCreateUpdateDto;
import com.autoparts.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BrandUpdateMapper {

    void update(@MappingTarget Brand entity, BrandCreateUpdateDto updateEntity);

    BrandCreateUpdateDto toDto(Brand brand);

    @Mapping(target = "id", ignore = true)
    Brand toEntity(BrandCreateUpdateDto brandReadDto);

}
