package com.autoparts.mapper.user;

import com.autoparts.dto.order.OrderReadDto;
import com.autoparts.entity.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemReadMapper.class})
public interface OrderReadMapper {

    @Mapping(source = "items", target = "items")
//    @Mapping(source = "totals", target = "totals")
    @Mapping(source = "shippingAddress", target = "shippingAddress")
    OrderReadDto toDto(Order order);

    List<OrderReadDto> toDto(List<Order> orders);
}
