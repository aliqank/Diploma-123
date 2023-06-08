package com.autoparts.mapper.user;

import com.autoparts.dto.order.OrderItemDto;
import com.autoparts.entity.order.OrderItem;
import com.autoparts.mapper.tovar.TovarReadMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = TovarReadMapper.class)
public interface OrderItemReadMapper {

    @Mapping(source = "tovar", target = "product")
    OrderItemDto toDto(OrderItem orderItem);
    List<OrderItemDto> toDto(List<OrderItem> orderItems);
}
