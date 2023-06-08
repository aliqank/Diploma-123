package com.autoparts.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TovarReadDto {

    Long id;
    String name;
    String slug;
    String sku;
    Integer price;
    String carBrand;
//    int rating;
//    int reviews;
//    String availability;
//    String description;
//    String excerpt;
//    String partNumber;
//    Long   compareAtPrice;
//    String compatibility;
//    String tags;
//    String stock;
//    int quantity;
//    List<String> images;
}
