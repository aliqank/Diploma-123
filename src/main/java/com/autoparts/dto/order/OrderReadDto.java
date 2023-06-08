package com.autoparts.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderReadDto {

    Long id;
    String token;
    String number;
    Date createdAt;
    String payment;
    int quantity;
    double subtotal;
    double total;
    String status;
    List<OrderItemDto> items;
    List<OrderTotalDto> totals;
    AddressDto shippingAddress;
    AddressDto billingAddress;

}
