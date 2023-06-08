package com.autoparts.serivce;

import com.autoparts.dto.ExcelReportDto.OrderItemsReportDto;
import com.autoparts.dto.order.OrderItemDto;
import com.autoparts.mapper.user.OrderItemExcelMapper;
import com.autoparts.mapper.user.OrderItemReadMapper;
import com.autoparts.repository.order.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemsService {

    private final OrderItemRepository itemRepository;
    private final OrderItemReadMapper orderItemReadMapper;
    private final ExcelGeneratorService excelGeneratorService;
    private final OrderItemExcelMapper orderItemExcelMapper;

    public List<OrderItemDto> findAll(){
        return itemRepository.findAll().stream()
                .map(orderItemReadMapper::toDto)
                .toList();
    }

    public ByteArrayInputStream toExcel(){

        List<OrderItemsReportDto> orderItemDtos = itemRepository.findAll().stream()
                .map(orderItemExcelMapper::toDto)
                .toList();

        return excelGeneratorService.listToExcelFile(orderItemDtos);
    }


}
