package com.autoparts.dto.tovar;

import com.autoparts.dto.attribute.ProductTypeDto;
import com.autoparts.dto.category.CategoryDto;
import com.autoparts.entity.Brand;
import com.autoparts.entity.ProductAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TovarDto {

    Long id;
    String name;
    String slug;
    String sku;
    Integer price;
    List<String> images;
    int rating;
    int reviews;
    String availability;
    Brand brand;
    String description;
    String excerpt;
    CategoryDto categories;
    String partNumber;
    Long   compareAtPrice;
    String compatibility;
    String tags;
    String brandName;
    String stock;
    List<ProductAttribute> attributes;
    ProductTypeDto type;
    int quantity;
}
