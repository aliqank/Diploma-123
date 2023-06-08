package com.autoparts.mapper.user;

import com.autoparts.dto.ExcelReportDto.OrderItemsReportDto;
import com.autoparts.entity.order.OrderItem;
import com.autoparts.mapper.tovar.TovarReadMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = TovarReadMapper.class)
public interface OrderItemExcelMapper {

    @Mapping(target = "productName", source = "tovar.name")
    @Mapping(target = "productId", source = "tovar.id")
    OrderItemsReportDto toDto(OrderItem orderItem);

    List<OrderItemsReportDto> toDto(List<OrderItem> orderItems);
}
