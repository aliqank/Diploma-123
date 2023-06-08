package com.autoparts.mapper.product;

import com.autoparts.dto.attribute.ProductTypeDto;
import com.autoparts.entity.ProductAttribute;
import com.autoparts.entity.ProductType;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {

    ProductTypeDto map(ProductType productType);

    default Set<String> map(Set<ProductAttribute> value){
        return value.stream().map(ProductAttribute::getSlug)
                .collect(Collectors.toSet());
    }
}
