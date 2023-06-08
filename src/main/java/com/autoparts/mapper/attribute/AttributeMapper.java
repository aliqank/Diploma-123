package com.autoparts.mapper.attribute;

import com.autoparts.dto.attribute.AttributeCreateDto;
import com.autoparts.dto.attribute.AttributeGroupCreateDto;
import com.autoparts.dto.attribute.AttributeGroupDto;
import com.autoparts.entity.AttributeGroup;
import com.autoparts.entity.ProductAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AttributeMapper {


    ProductAttribute toEntity(AttributeCreateDto attributeCreateDtos);
    List<ProductAttribute> toEntityList(List<AttributeCreateDto> attributeCreateDtos);

}
