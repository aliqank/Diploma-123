package com.autoparts.dto.tovar;


import com.autoparts.dto.attribute.AttributeGroupCreateDto;
import com.autoparts.dto.image.ProductImageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TovarCreateEditDto {

    String name;
    String slug;
    String sku;
    Integer price;
    String stock;
    String description;
    String excerpt;
    int quantity;
    List<ProductImageDTO> images;
    String brand;
    String category;
    Set<AttributeGroupCreateDto> attributeGroup;
    MultipartFile image;
}
