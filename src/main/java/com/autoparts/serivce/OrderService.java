package com.autoparts.serivce;

import com.autoparts.dto.ml.ProductPredictDto;
import com.autoparts.dto.order.*;
import com.autoparts.entity.Tovar;
import com.autoparts.entity.enums.OrderStatus;
import com.autoparts.entity.order.Address;
import com.autoparts.entity.order.Order;
import com.autoparts.entity.order.OrderDetailsDto;
import com.autoparts.entity.order.OrderItem;
import com.autoparts.mapper.AddressMapper;
import com.autoparts.mapper.tovar.TovarReadMapper;
import com.autoparts.mapper.user.OrderMapper;
import com.autoparts.mapper.user.OrderReadMapper;
import com.autoparts.repository.TovarRepository;
import com.autoparts.repository.order.OrderRepository;
import io.github.rushuat.ocell.document.DocumentOOXML;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final AddressMapper addressMapper;
    private final TovarRepository tovarRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;

    private final OrderReadMapper readMapper;
    private final OrderMapper orderMapper;
    private final ExcelGeneratorService excelGeneratorService;
    private final TovarReadMapper tovarReadMapper;

    public List<OrderDetailsDto> getTovars(Long orderId){
        return orderRepository.findTovarsByOrderId(orderId);
    }

    @Transactional
    public OrderReadDto create(OrderCreateDto orderDto) {

        Address billingAddress = addressMapper.toEntity(orderDto.getBillingAddress());

        List<OrderItem> items = orderDto.getItems().stream().map(orderItemDef -> {
            Optional<Tovar> tovarOpt = tovarRepository.findById(orderItemDef.getProductId());

            Tovar tovar = tovarOpt.orElseThrow(() -> new RuntimeException("Product not found"));

            return OrderItem.builder()
                    .tovar(tovar)
                    .price(tovar.getPrice())
                    .quantity(orderItemDef.getQuantity())
                    .total(tovar.getPrice() * orderItemDef.getQuantity())
                    .build();
        }).toList();


        int quantity = items.stream().mapToInt(OrderItem::getQuantity).sum();
        double subtotal = items.stream().mapToDouble(OrderItem::getTotal).sum();

        var user = userService.getCurrentUserObject().orElseThrow();
        Order order = Order.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .number(UUID.randomUUID().toString())
                .createdAt(LocalDate.now())
                .payment(orderDto.getPayment())
                .status(OrderStatus.PENDING)
                .items(items)
                .quantity(quantity)
                .subtotal(subtotal)
                .total(subtotal)
                .shippingAddress(billingAddress)
                .billingAddress(billingAddress)
                .build();

        orderRepository.saveAndFlush(order);
        return readMapper.toDto(order);
    }

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public List<ProductPredictDto> productPredictData(){
        return orderRepository.fetchProductPredictData();
    }

    public List<OrderDto> findAllOrders(){
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public ByteArrayInputStream excel(){


        ByteArrayInputStream byteArrayInputStream = excelGeneratorService.listToExcelFile(findAll());
        return byteArrayInputStream;
    }

    public OrderReadDto findOrderById(Long id){
        Order order = orderRepository.findOrderById(id);
        return readMapper.toDto(order);
    }

    public OrderReadDto findOrderByToken(String token){
        Order order = orderRepository.findOrderByToken(token);
        return readMapper.toDto(order);
    }

    public Page<OrderReadDto> findUserOrders(Pageable pageable){
        var username = userService.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findUserOrders(pageable, username)
                .map(readMapper::toDto);
    }

    public List<String> getOrderStatusValues(){
        return Arrays.stream(OrderStatus.values())
                .map(Enum::name)
                .toList();
    }

    @Transactional
    public OrderDto update(Long id, OrderStatusDto orderStatusDto){
        Order order = orderRepository.findOrderById(id);
//        orderMapper.update(order, orderStatusDto);
        order.setStatus(OrderStatus.valueOf(orderStatusDto.getStatus()));

        return Optional.of(order)
                .map(orderRepository::save)
                .map(orderMapper::toDto)
                .orElseThrow();
    }

    @Transactional
    public boolean delete(Long id) {
        return orderRepository.findById(id)
                .map(entity -> {
                    orderRepository.delete(entity);
                    orderRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @SneakyThrows
    public ByteArrayInputStream excell() {

        List<OrderDto> all = findAllOrders();

        DocumentOOXML documentOOXML = new DocumentOOXML();
        documentOOXML.addSheet(all);
        byte[] bytes = documentOOXML.toBytes();

        return new ByteArrayInputStream(bytes);

    }

    @SneakyThrows
    public ByteArrayInputStream listToExcelFile() {

        List<OrderDto> orders = findAllOrders();
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Orders");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setWrapText(true);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);

        createHeaderRow(sheet, headerCellStyle);

        CellStyle oddRowStyle = workbook.createCellStyle();
        oddRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        oddRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        oddRowStyle.setAlignment(HorizontalAlignment.CENTER);
        oddRowStyle.setBorderBottom(BorderStyle.THIN);
        oddRowStyle.setBorderTop(BorderStyle.THIN);
        oddRowStyle.setBorderRight(BorderStyle.THIN);
        oddRowStyle.setBorderLeft(BorderStyle.THIN);

        CellStyle evenRowStyle = workbook.createCellStyle();
        evenRowStyle.setAlignment(HorizontalAlignment.CENTER);
        evenRowStyle.setBorderBottom(BorderStyle.THIN);
        evenRowStyle.setBorderTop(BorderStyle.THIN);
        evenRowStyle.setBorderRight(BorderStyle.THIN);
        evenRowStyle.setBorderLeft(BorderStyle.THIN);

        int rowIndex = 1;
        for (OrderDto order : orders) {
            for (OrderItemDto item : order.getItems()) {
                Row row = sheet.createRow(rowIndex++);
                createOrderRow(order, item, row, (rowIndex % 2 == 0) ? evenRowStyle : oddRowStyle);
            }
        }

        for (int i = 0; i < 17; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private void createHeaderRow(Sheet sheet, CellStyle headerCellStyle) {
        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(40);
        createStyledCell(headerRow, 0, "Order ID", headerCellStyle);
        createStyledCell(headerRow, 1, "Order Number", headerCellStyle);
        createStyledCell(headerRow, 2, "Payment", headerCellStyle);
        createStyledCell(headerRow, 3, "Quantity", headerCellStyle);
        createStyledCell(headerRow, 4, "Total", headerCellStyle);
        createStyledCell(headerRow, 5, "Username", headerCellStyle);
        createStyledCell(headerRow, 6, "Status", headerCellStyle);
        createStyledCell(headerRow, 7, "Item ID", headerCellStyle);
        createStyledCell(headerRow, 8, "Price", headerCellStyle);
        createStyledCell(headerRow, 9, "Item Quantity", headerCellStyle);
        createStyledCell(headerRow, 10, "Item Total", headerCellStyle);
        createStyledCell(headerRow, 11, "Product ID", headerCellStyle);
        createStyledCell(headerRow, 12, "Product Name", headerCellStyle);
        createStyledCell(headerRow, 13, "Product Slug", headerCellStyle);
        createStyledCell(headerRow, 14, "Product SKU", headerCellStyle);
        createStyledCell(headerRow, 15, "Product Price", headerCellStyle);
        createStyledCell(headerRow, 16, "Address", headerCellStyle);
        createStyledCell(headerRow, 17, "Full Name", headerCellStyle);
    }

    private void createStyledCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createOrderRow(OrderDto order, OrderItemDto item, Row row, CellStyle style) {
        row.setHeightInPoints(35);
        createStyledCell(row, 0, String.valueOf(order.getId()), style);
        createStyledCell(row, 1, order.getNumber(), style);
        createStyledCell(row, 2, order.getPayment(), style);
        createStyledCell(row, 3, String.valueOf(order.getQuantity()), style);
        createStyledCell(row, 4, String.valueOf(order.getTotal()), style);
        createStyledCell(row, 5, order.getUsername(), style);
        createStyledCell(row, 6, order.getStatus(), style);
        createStyledCell(row, 7, String.valueOf(item.getId()), style);
        createStyledCell(row, 8, String.valueOf(item.getPrice()), style);
        createStyledCell(row, 9, String.valueOf(item.getQuantity()), style);
        createStyledCell(row, 10, String.valueOf(item.getTotal()), style);
        createStyledCell(row, 11, String.valueOf(item.getProduct().getId()), style);
        createStyledCell(row, 12, item.getProduct().getName(), style);
        createStyledCell(row, 13, item.getProduct().getSlug(), style);
        createStyledCell(row, 14, item.getProduct().getSku(), style);
        createStyledCell(row, 15, String.valueOf(item.getProduct().getPrice()), style);
        createStyledCell(row, 16, order.getShippingAddress().getCountry() + " "
                + order.getShippingAddress().getCity() + " "
                + order.getShippingAddress().getPhone() + " "
                + order.getShippingAddress().getAddress1(), style);
        createStyledCell(row, 17, order.getShippingAddress().getFirstName() + " "
                + order.getShippingAddress().getLastName(), style);
    }


//    private void createHeaderRow(Sheet sheet, CellStyle headerCellStyle) {
//        Row headerRow = sheet.createRow(0);
//        createStyledCell(headerRow, 0, "Order ID", headerCellStyle);
//        createStyledCell(headerRow, 1, "Order Number", headerCellStyle);
//        createStyledCell(headerRow, 2, "Payment", headerCellStyle);
//        createStyledCell(headerRow, 3, "Quantity", headerCellStyle);
//        createStyledCell(headerRow, 4, "Total", headerCellStyle);
//        createStyledCell(headerRow, 5, "Username", headerCellStyle);
//        createStyledCell(headerRow, 6, "Status", headerCellStyle);
//        createStyledCell(headerRow, 7, "Item ID", headerCellStyle);
//        createStyledCell(headerRow, 8, "Price", headerCellStyle);
//        createStyledCell(headerRow, 9, "Item Quantity", headerCellStyle);
//        createStyledCell(headerRow, 10, "Item Total", headerCellStyle);
//        createStyledCell(headerRow, 11, "Product ID", headerCellStyle);
//        createStyledCell(headerRow, 12, "Product Name", headerCellStyle);
//        createStyledCell(headerRow, 13, "Product Slug", headerCellStyle);
//        createStyledCell(headerRow, 14, "Product SKU", headerCellStyle);
//        createStyledCell(headerRow, 15, "Product Price", headerCellStyle);
//        createStyledCell(headerRow, 16, "Address", headerCellStyle);
//        createStyledCell(headerRow, 17, "Full Name", headerCellStyle);
//    }

//    private void createStyledCell(Row row, int column, String value, CellStyle style) {
//        Cell cell = row.createCell(column);
//        cell.setCellValue(value);
//        cell.setCellStyle(style);
//    }

    private void createOrderRow(OrderDto order, OrderItemDto item, Row row) {
        row.createCell(0).setCellValue(order.getId());
        row.createCell(1).setCellValue(order.getNumber());
        row.createCell(2).setCellValue(order.getPayment());
        row.createCell(3).setCellValue(order.getQuantity());
        row.createCell(4).setCellValue(order.getTotal());
        row.createCell(5).setCellValue(order.getUsername());
        row.createCell(6).setCellValue(order.getStatus());
        row.createCell(7).setCellValue(item.getId());
        row.createCell(8).setCellValue(item.getPrice());
        row.createCell(9).setCellValue(item.getQuantity());
        row.createCell(10).setCellValue(item.getTotal());
        row.createCell(11).setCellValue(item.getProduct().getId());
        row.createCell(12).setCellValue(item.getProduct().getName());
        row.createCell(13).setCellValue(item.getProduct().getSlug());
        row.createCell(14).setCellValue(item.getProduct().getSku());
        row.createCell(15).setCellValue(item.getProduct().getPrice());
        row.createCell(16).setCellValue(order.getShippingAddress()
                .getCountry() + " "
                +order.getShippingAddress().getCity() + " "
                +order.getShippingAddress().getPhone() + " "
                + order.getShippingAddress().getAddress1() + " "
        );
        row.createCell(17).setCellValue(order.getShippingAddress()
                .getFirstName() + " "
                +order.getShippingAddress().getLastName()
        );
    }
}
