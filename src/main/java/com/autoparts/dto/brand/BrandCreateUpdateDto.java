package com.autoparts.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandCreateUpdateDto {
    String name;
    String slug;
    String image;
    String country;
}
