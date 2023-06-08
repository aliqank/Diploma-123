package com.autoparts.dto.product;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    String brand;
    String articleNo;
    String name;
    int count;
    BigDecimal price;
    String carName;
    Long categoryId;
    MultipartFile image;
}
