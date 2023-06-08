package com.autoparts.controller;

import com.autoparts.dto.order.OrderDto;
import com.autoparts.dto.order.OrderItemDto;
import com.autoparts.dto.order.OrderStatusDto;
import com.autoparts.dto.tovar.TovarCreateEditDto;
import com.autoparts.dto.tovar.TovarDto;
import com.autoparts.entity.order.OrderDetailsDto;
import com.autoparts.serivce.OrderItemsService;
import com.autoparts.serivce.OrderService;
import com.autoparts.serivce.TovarService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v3/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final TovarService tovarService;
    private final OrderService orderService;
    private final OrderItemsService orderItemsService;

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<TovarDto> findAll(){
        return tovarService.getTovars();
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OrderDto> findAllOrders(){
        return orderService.findAllOrders();
    }

    @GetMapping("/dataItems")
    public List<OrderDetailsDto> dataItems(Long id){
        return orderService.getTovars(id);
    }

    @GetMapping("/orderItems")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OrderItemDto> findAllOrderItems(){
        return orderItemsService.findAll();
    }

    @GetMapping("/orderItems/download")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Resource> orderItemsExcel() {
        ByteArrayInputStream excel = orderItemsService.toExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=objects.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ByteArrayResource(excel.readAllBytes()));
    }

    @GetMapping("/download")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Resource> downloadFile() {
        ByteArrayInputStream excel = orderService.listToExcelFile();

        var currentTime = LocalDateTime.now().toString();
        var fileName = "Orders_" + currentTime + ".xlsx"; // Include file extension
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // Specify MIME type

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(excel));
    }

    @GetMapping("/orderValues")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> orderValues(){
        return orderService.getOrderStatusValues();
    }

    @PutMapping("/order/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@RequestBody OrderStatusDto orderStatusDto, @PathVariable("id") Long id) {
        orderService.update(id ,orderStatusDto);
    }
    @DeleteMapping("/order/{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return orderService.delete(id)
                ? noContent().build()
                : notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public void create(@RequestBody TovarCreateEditDto tovarCreateDto) {
        tovarService.createByName(tovarCreateDto);
    }
}
