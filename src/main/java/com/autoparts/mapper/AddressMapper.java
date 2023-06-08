package com.autoparts.mapper;

import com.autoparts.dto.order.AddressDto;
import com.autoparts.entity.order.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto toDto(Address address);

    Address toEntity(AddressDto addressDto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    void update(@MappingTarget Address entity, AddressDto updateEntity);

}
