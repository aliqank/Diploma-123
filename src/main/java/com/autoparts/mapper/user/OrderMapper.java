package com.autoparts.mapper.user;

import com.autoparts.dto.order.OrderDto;
import com.autoparts.dto.order.OrderStatusDto;
import com.autoparts.entity.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemReadMapper.class})
public interface OrderMapper {


//    @Mapping(target = "productName", source = "items.product.name")
    @Mapping(target = "username", source = "user.username")
    OrderDto toDto(Order order);

    List<OrderDto> toDto(List<Order> orders);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "shippingAddress", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "billingAddress", ignore = true)
    void update(@MappingTarget Order entity, OrderStatusDto updateEntity);
}
