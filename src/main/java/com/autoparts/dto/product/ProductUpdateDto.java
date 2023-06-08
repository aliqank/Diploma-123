package com.autoparts.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateDto {
    String brand;
    String articleNo;
    String name;
    int count;
    BigDecimal price;
    String carName;
    Long categoryId;
}
