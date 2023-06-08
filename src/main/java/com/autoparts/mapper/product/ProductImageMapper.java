package com.autoparts.mapper.product;

import com.autoparts.dto.image.ProductImageDTO;
import com.autoparts.entity.ProductImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    ProductImage toEntity(ProductImageDTO dto);

    ProductImageDTO toDto(ProductImage entity);

    List<ProductImage> toEntityList(List<ProductImageDTO> productImageDTOS);
}
