package com.autoparts.dto.ExcelReportDto;

import com.autoparts.dto.order.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSellsDto {
    Long id;
    String number;
    String payment;
    int quantity;
    double total;
    String username;
    String status;
    List<OrderItemDto> items;

    // Fields from OrderItemDto
    double itemPrice;
    int itemQuantity;
    double itemTotal;

    // Fields from TovarReadDto
    Long productId;
    String productName;
    String productSlug;
    String productSku;
    Integer productPrice;
}
