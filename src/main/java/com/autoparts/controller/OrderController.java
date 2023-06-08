package com.autoparts.controller;

import com.autoparts.dto.ml.ProductPredictDto;
import com.autoparts.dto.order.OrderCreateDto;
import com.autoparts.dto.order.OrderReadDto;
import com.autoparts.dto.response.OrderResponse;
import com.autoparts.entity.order.Order;
import com.autoparts.serivce.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    public OrderReadDto create(@RequestBody OrderCreateDto dto) {
        return orderService.create(dto);
    }

    @GetMapping()
    public List<Order> findAll(){
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public OrderReadDto findAll(@PathVariable Long id){
        return orderService.findOrderById(id);
    }

    @GetMapping("/productPredictData")
    public List<ProductPredictDto> productPredictData(){
        return orderService.productPredictData();
    }

    @GetMapping("/{token}/token")
    public OrderReadDto findAll(@PathVariable String token){
        return orderService.findOrderByToken(token);
    }


    @GetMapping("/user/orders")
    public OrderResponse<OrderReadDto> findUserOrder(Pageable pageable){
        Page<OrderReadDto> page = orderService.findUserOrders(pageable);
        return OrderResponse.of(page);
    }

//    @GetMapping("/user/orders1")
//    public List<OrderReadDto> findUserOrder1(){
//        return orderService.findUserOrders();
//    }

}
