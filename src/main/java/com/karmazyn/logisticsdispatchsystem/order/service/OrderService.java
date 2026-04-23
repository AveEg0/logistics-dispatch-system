package com.karmazyn.logisticsdispatchsystem.order.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.DriverNotAvailableException;
import com.karmazyn.logisticsdispatchsystem.common.exception.DriverNotFoundException;
import com.karmazyn.logisticsdispatchsystem.common.exception.OrderNotFoundException;
import com.karmazyn.logisticsdispatchsystem.driver.entity.Driver;
import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import com.karmazyn.logisticsdispatchsystem.driver.repository.DriverRepository;
import com.karmazyn.logisticsdispatchsystem.order.entity.Order;
import com.karmazyn.logisticsdispatchsystem.order.entity.OrderStatus;
import com.karmazyn.logisticsdispatchsystem.order.repository.OrderRepository;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    /**
     * Crates order for an existing user
     */
    @Transactional
    public Order createOrder(String pickup, String delivery, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setPickupLocation(pickup);
        order.setDeliveryLocation(delivery);
        order.setCreatedBy(user);
        order.setStatus(OrderStatus.CREATED);

        return orderRepository.save(order);
    }

    /**
     * Assigns driver to an order
     * Sets the order status to ASSIGNED
     * Sets the driver status to BUSY
     */
    @Transactional
    public Order assignDriver(Long orderId, Long driverId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        //pessimistic lock to prevent race condition
        Driver driver = driverRepository.findByIdForUpdate(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found"));

        // Check if driver is available
        if (driver.getStatus() != DriverStatus.AVAILABLE) {
            throw new DriverNotAvailableException("Driver is not available");
        }

        order.setDriver(driver);
        order.setStatus(OrderStatus.ASSIGNED);

        // Update driver status
        driver.setStatus(DriverStatus.BUSY);
        driverRepository.save(driver);

        return orderRepository.save(order);
    }

    /**
     * Completes the order
     * Sets the order status to COMPLETED
     * Sets the driver status to AVAILABLE
     */

    @Transactional
    public Order completeOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        // Mark order as completed
        order.setStatus(OrderStatus.COMPLETED);

        Driver driver = order.getDriver();

        // If driver exists, free him
        if (driver != null) {
            driver.setStatus(DriverStatus.AVAILABLE);
            driverRepository.save(driver);
        }

        return orderRepository.save(order);
    }
}
