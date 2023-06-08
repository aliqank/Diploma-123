package com.autoparts.entity.order;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailsDto {

    Long id;
    String name;
    String slug;
    String sku;
    Integer price;
    Integer quantity;
    double total;

    public OrderDetailsDto(OrderItem orderItem) {
        this.id = orderItem.getTovar().getId();
        this.name = orderItem.getTovar().getName();
        this.slug = orderItem.getTovar().getSlug();
        this.sku = orderItem.getTovar().getSku();
        this.price = orderItem.getTovar().getPrice();
        this.quantity = orderItem.getQuantity();
        this.total = orderItem.getTotal();
    }
}
