package com.autoparts.mapper.product;

import com.autoparts.dto.product.ProductUpdateDto;
import com.autoparts.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductUpdateMapper {
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductUpdateDto productUpdateDto);

}
