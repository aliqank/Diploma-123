package com.autoparts.dto.ExcelReportDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemsReportDto {

    Long id;
    double price;
    int quantity;
    double total;
    Long productId;
    String productName;
}
