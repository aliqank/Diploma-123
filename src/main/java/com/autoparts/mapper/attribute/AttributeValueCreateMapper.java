package com.autoparts.mapper.attribute;

import com.autoparts.dto.attribute.AttributeValueCreateDto;
import com.autoparts.entity.ProductAttributeValue;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface AttributeValueCreateMapper {

    Set<ProductAttributeValue> toEntityList(Set<AttributeValueCreateDto> values);
    ProductAttributeValue toEntity(AttributeValueCreateDto value);

}
