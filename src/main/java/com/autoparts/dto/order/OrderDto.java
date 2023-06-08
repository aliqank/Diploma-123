package com.autoparts.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    Long id;
    String number;
    String payment;
    int quantity;
    double total;
    String username;
    String status;
    List<OrderItemDto> items;
    AddressDto shippingAddress;
    String productNames = (items != null) ? items.stream()
            .map(s -> Optional.ofNullable(s.getProduct()).map(TovarReadDto::getName).orElse(""))
            .collect(Collectors.joining(", ")) : "";

}
