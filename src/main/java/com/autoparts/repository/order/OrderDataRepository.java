package com.autoparts.repository.order;

import com.autoparts.dto.ml.ProductPredictDto;
import com.autoparts.entity.order.Order;
import com.autoparts.entity.order.OrderDetailsDto;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDataRepository{

    Page<Order> findUserOrders(Pageable pageable, String username);

    Order findOrderById(Long id);
    Order findOrderByToken(String token);

    List<Tuple> getOrderGroupedByMonth();

    List<Tuple> getOrderGroupedByProduct();

    List<Tuple> getSellsGroupedByCategory();

    List<Tuple> getSellsGroupedByBrands();

    List<Tuple> getSellsTotalForMonth();

    List<Object[]> getRevenueByWeekForMonth();
    List<ProductPredictDto> fetchProductPredictData();

    List<OrderDetailsDto> findTovarsByOrderId(Long orderId);
    List<Long> findTovarSalesForCurrentMonth(List<Long> tovarIds);
    List<Object[]> getRevenueByDayForMonth();
}
