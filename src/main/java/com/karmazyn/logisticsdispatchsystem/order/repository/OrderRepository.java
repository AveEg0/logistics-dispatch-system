package com.karmazyn.logisticsdispatchsystem.order.repository;

import com.karmazyn.logisticsdispatchsystem.order.entity.Order;
import com.karmazyn.logisticsdispatchsystem.order.entity.OrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdForUpdate(Long id);

    Page<Order> findAllByDriverId(Long driverId, Pageable pageable);

    Optional<Order> findByDriverId(Long id);

    Optional<Order> findFirstByDriverIdAndStatusIn(Long id, List<OrderStatus> statuses);
}
