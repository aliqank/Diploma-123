package com.autoparts.repository.order;

import com.autoparts.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends
        JpaRepository<Order, Long>,
        OrderDataRepository,
        QuerydslPredicateExecutor<Order> {

    Page<Order> findAllByToken(String token, Pageable pageable);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.token = :token")
    List<Order> findByTokenWithItems(@Param("token") String token);

    @Query("SELECT o FROM Order o WHERE o.token = :token")
    List<Order> findByTokenWithItems1(@Param("token") String token);
}
