package com.autoparts.dto.tovar;


import com.autoparts.dto.attribute.AttributeGroupCreateDto;
import com.autoparts.dto.image.ProductImageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TovarCreateDto {

    String name;
    String slug;
    String sku;
    Integer price;
    List<ProductImageDTO> images;
    Long brandId;
    Long categoryId;
    Set<AttributeGroupCreateDto> attributeGroup;
//    List<AttributeCreateDto> attributes;
}
