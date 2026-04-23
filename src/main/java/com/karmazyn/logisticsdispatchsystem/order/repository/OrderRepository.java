package com.karmazyn.logisticsdispatchsystem.order.repository;

import com.karmazyn.logisticsdispatchsystem.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
