package com.autoparts.repository.order;

import com.autoparts.entity.order.OrderTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTotalRepository extends JpaRepository<OrderTotal, Long> {


}
