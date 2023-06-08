package com.autoparts.mapper.attribute;

import com.autoparts.dto.attribute.AttributeGroupCreateDto;
import com.autoparts.dto.attribute.AttributeGroupDto;
import com.autoparts.entity.AttributeGroup;
import com.autoparts.entity.ProductAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AttributeGroupMapper {

    AttributeGroupDto toDto(AttributeGroup attributeGroup);

    AttributeGroup toEntity(AttributeGroupCreateDto attributeGroup);


    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    List<AttributeGroup> toEntityList(List<AttributeGroupCreateDto> attributeGroup);

    default Set<String> map(Set<ProductAttribute> value){
        return value.stream().map(ProductAttribute::getSlug)
                .collect(Collectors.toSet());
    }

}
