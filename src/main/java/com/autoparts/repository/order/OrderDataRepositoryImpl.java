package com.autoparts.repository.order;

import com.autoparts.dto.ml.ProductPredictDto;
import com.autoparts.entity.QTovar;
import com.autoparts.entity.order.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.autoparts.entity.order.QOrderItem.orderItem;


public class OrderDataRepositoryImpl extends QuerydslRepositorySupport implements OrderDataRepository {

    private final EntityManager entityManager;

    public OrderDataRepositoryImpl(EntityManager entityManager) {
        super(Order.class);
        this.entityManager = entityManager;
    }

    public Page<Order> findUserOrders(Pageable pageable, String username) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;

        JPAQuery<Order> query = queryFactory.selectFrom(order)
                .leftJoin(order.shippingAddress, QAddress.address)
                .fetchJoin()
                .leftJoin(order.items, QOrderItem.orderItem)
                .fetchJoin()
                .leftJoin(orderItem.tovar, QTovar.tovar)
                .fetchJoin()
                .where(order.user.username.eq(username))
                .fetchAll();

        List<Order> orders = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(orders, pageable, query.fetchCount());

    }

    public Order findOrderById(Long id) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;

        return  queryFactory.selectFrom(order)
                .where(order.id.eq(id))
                .leftJoin(order.shippingAddress, QAddress.address)
                .fetchJoin()
                .leftJoin(order.items, QOrderItem.orderItem)
                .fetchJoin()
                .leftJoin(orderItem.tovar, QTovar.tovar)
                .fetchJoin()
                .fetchOne();
    }

    @Override
    public Order findOrderByToken(String token) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;

        return  queryFactory.selectFrom(order)
                .where(order.token.eq(token))
                .leftJoin(order.shippingAddress, QAddress.address)
                .fetchJoin()
                .leftJoin(order.items, QOrderItem.orderItem)
                .fetchJoin()
                .leftJoin(orderItem.tovar, QTovar.tovar)
                .fetchJoin()
                .fetchOne();
    }

    public List<Tuple> getOrderGroupedByMonth() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder order = QOrder.order;
        return queryFactory
                .select(order.createdAt, order.count())
                .from(order)
                .groupBy(order.createdAt)
                .fetch();
    }

    public List<Tuple> getOrderGroupedByProduct() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFactory
                .select(orderItem.tovar.name, orderItem.tovar.count())
                .from(orderItem)
                .groupBy(orderItem.tovar.name)
                .fetch();
    }

    public List<Tuple> getSellsGroupedByCategory() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFactory
                .select(orderItem.tovar.category.name, orderItem.tovar.count())
                .from(orderItem)
                .groupBy(orderItem.tovar.category.name)
                .fetch();
    }

    public List<Tuple> getSellsGroupedByBrands() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFactory
                .select(orderItem.tovar.brand.name, orderItem.tovar.count())
                .from(orderItem)
                .groupBy(orderItem.tovar.brand.name)
                .fetch();
    }

    public List<Tuple> getSellsTotalForMonth() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder order = QOrder.order;
        return queryFactory
                .select(order.createdAt, order.total.sum())
                .from(order)
                .groupBy(order.createdAt)
                .orderBy(order.createdAt.asc())
                .fetch();
    }


    public List<Object[]> getRevenueByWeekForMonth() {
        LocalDate firstDayOfCurrentMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfCurrentMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        String sql = "SELECT date_trunc('week', created_at) as week, sum(total) " +
                "FROM Orders " +
                "WHERE created_at >= :startOfMonth AND created_at <= :endOfMonth " +
                "GROUP BY week " +
                "ORDER BY week";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("startOfMonth", firstDayOfCurrentMonth);
        query.setParameter("endOfMonth", lastDayOfCurrentMonth);

        List<Object[]> results = query.getResultList();

        return results;
    }

    public List<Object[]> getRevenueByDayForMonth() {
        LocalDate firstDayOfCurrentMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfCurrentMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        String sql = "SELECT date_trunc('day', created_at) as day, sum(total) " +
                "FROM Orders " +
                "WHERE created_at >= :startOfMonth AND created_at <= :endOfMonth " +
                "GROUP BY day " +
                "ORDER BY day";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("startOfMonth", firstDayOfCurrentMonth);
        query.setParameter("endOfMonth", lastDayOfCurrentMonth);

        List<Object[]> results = query.getResultList();

        return results;
    }


    public List<ProductPredictDto> fetchProductPredictData() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;
        QTovar tovar = QTovar.tovar;

        return queryFactory
                .select(Projections.constructor(ProductPredictDto.class,
                        orderItem.tovar.id,
                        order.createdAt,
                        orderItem.quantity.longValue(),
                        orderItem.price.longValue(),
                        orderItem.tovar.brand.id))
                .from(order)
                .join(order.items, orderItem)
                .join(orderItem.tovar, tovar)
                .orderBy(orderItem.tovar.id.desc())
                .groupBy(orderItem.tovar.id, order.createdAt, orderItem.quantity, orderItem.price, orderItem.tovar.brand.id)
                .fetch();
    }

    public List<OrderDetailsDto> findTovarsByOrderId(Long orderId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QTovar tovar = QTovar.tovar;
        QOrderItem orderItem = QOrderItem.orderItem;
        QOrder order = QOrder.order;

        Order fetchedOrder = queryFactory
                .selectFrom(order)
                .where(order.id.eq(orderId))
                .fetchOne();

        // Fetch the list of OrderItem associated with the order
        List<OrderItem> orderItems = queryFactory
                .selectFrom(orderItem)
                .where(orderItem.in(fetchedOrder.getItems()))  // Use the fetched order items
                .fetch();

        // Convert each OrderItem to an OrderDetailsDto and return the list
        return orderItems.stream()
                .map(OrderDetailsDto::new)
                .collect(Collectors.toList());
    }

    public List<Long> findTovarSalesForCurrentMonth(List<Long> tovarIds) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QTovar tovar = QTovar.tovar;
        QOrderItem orderItem = QOrderItem.orderItem;
        QOrder order = QOrder.order;


        LocalDate startDate = YearMonth.now().atDay(1);
        LocalDate endDate = YearMonth.now().atEndOfMonth();

        // Fetch the sales count

        Map<Long, Long> tovarSales = new LinkedHashMap<>();
        for (Long id : tovarIds) {
            tovarSales.put(id, 0L);
        }

        // Fetch sum of quantities for each tovarId
        List<Tuple> results = queryFactory
                .select(orderItem.tovar.id, orderItem.quantity.sum().castToNum(Long.class))
                .from(orderItem)
                .where(orderItem.tovar.id.in(tovarIds))
                .groupBy(orderItem.tovar.id)
                .fetch();

        // Update quantities in the map
        for (Tuple row : results) {
            tovarSales.put(row.get(orderItem.tovar.id), row.get(orderItem.quantity.sum().castToNum(Long.class)));
        }

        // Return list of quantities in the order of the input list
        return new ArrayList<>(tovarSales.values());
    }
    }
