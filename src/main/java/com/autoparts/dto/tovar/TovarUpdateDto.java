package com.autoparts.dto.tovar;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TovarUpdateDto {

    String name;
    String slug;
    String sku;
    Integer price;
//    List<ProductImageDTO> images;
    int quantity;
    Long brandId;
    Long categoryId;
    String description;
    String excerpt;
    Long   compareAtPrice;
//    Set<AttributeGroupCreateDto> attributeGroup;
//    List<AttributeCreateDto> attributes;
}
