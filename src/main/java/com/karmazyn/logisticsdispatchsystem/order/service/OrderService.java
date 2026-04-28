package com.karmazyn.logisticsdispatchsystem.order.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.*;
import com.karmazyn.logisticsdispatchsystem.driver.entity.Driver;
import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import com.karmazyn.logisticsdispatchsystem.driver.repository.DriverRepository;
import com.karmazyn.logisticsdispatchsystem.order.dto.AssignDriverRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.CancelOrderRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.CompleteOrderRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.CreateOrderRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.OrderResponseDto;
import com.karmazyn.logisticsdispatchsystem.order.entity.Order;
import com.karmazyn.logisticsdispatchsystem.order.entity.OrderStatus;
import com.karmazyn.logisticsdispatchsystem.order.mapper.OrderMapper;
import com.karmazyn.logisticsdispatchsystem.order.repository.OrderRepository;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for managing orders within the logistics dispatch system.
 * Handles order creation, driver assignment, completion, and retrieval.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    /**
     * Creates a new order for an existing user.
     *
     * @param dto The order creation details.
     * @return The created order as a {@link OrderResponseDto}.
     * @throws UserNotFoundException If the user is not found.
     * @throws InvalidUserRoleException If the user is not a dispatcher or admin.
     */
    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequestDto dto) {

        User user = userRepository.findById(dto.getCreatedByUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() != UserRole.DISPATCHER && user.getRole() != UserRole.ADMIN) {
            throw new InvalidUserRoleException("User must be a dispatcher or admin to create an order");
        }

        Order order = new Order();
        order.setPickupLocation(dto.getPickupLocation());
        order.setDeliveryLocation(dto.getDeliveryLocation());

        String description = dto.getDescription();

        if (description == null || description.isBlank()) {
            description = "No description provided";
        }

        order.setDescription(description);
        order.setCreatedBy(user);
        order.setStatus(OrderStatus.CREATED);

        return orderMapper.toDto(orderRepository.save(order));
    }

    /**
     * Assigns a driver to an existing order.
     * Updates the order status to {@link OrderStatus#ASSIGNED} and the driver status to {@link DriverStatus#RESERVED}.
     *
     * @param orderId  The ID of the order to assign.
     * @param dto      The assignment details.
     * @return The updated order as a {@link OrderResponseDto}.
     * @throws OrderNotFoundException      If the order is not found.
     * @throws DriverNotFoundException     If the driver is not found.
     * @throws DriverNotAvailableException If the driver is not available.
     */
    @Transactional
    public OrderResponseDto assignDriver(Long orderId, AssignDriverRequestDto dto) {

        // Use pessimistic lock for Order to prevent concurrent assignments to the same order
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        // Check if order is in valid state for assignment
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException("Order cannot be assigned in its current state: " + order.getStatus());
        }

        // Use pessimistic lock for Driver to prevent assigning the same driver to multiple orders
        Driver driver = driverRepository.findByIdForUpdate(dto.getDriverId())
                .orElseThrow(() -> new DriverNotFoundException("Driver not found"));

        // Check if driver is available
        if (driver.getStatus() != DriverStatus.AVAILABLE) {
            throw new DriverNotAvailableException("Driver is not available");
        }

        order.setDriver(driver);
        order.setStatus(OrderStatus.ASSIGNED);

        // Update driver status
        driver.setStatus(DriverStatus.RESERVED);
        driverRepository.save(driver);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    /**
     * Cancels an existing order.
     * Updates the order status to {@link OrderStatus#CANCELLED} and makes the driver {@link DriverStatus#AVAILABLE}.
     *
     * @param orderId The ID of the order to cancel.
     * @param dto     The cancellation details.
     * @return The updated order as a {@link OrderResponseDto}.
     * @throws OrderNotFoundException If the order is not found.
     */
    @Transactional
    public OrderResponseDto cancelOrder(Long orderId, CancelOrderRequestDto dto) {
        // Lock order to prevent concurrent state transitions
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order cannot be cancelled in its current state: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);

        String comment = dto.getComment();
        if (comment == null || comment.isBlank()) {
            comment = "Order cancelled by dispatcher";
        }
        order.setComment(comment);

        Driver driver = order.getDriver();
        if (driver != null) {
            driver.setStatus(DriverStatus.AVAILABLE);
            driverRepository.save(driver);
        }

        return orderMapper.toDto(order);
    }

    /**
     * Marks an order as in progress by a driver.
     * Updates the order status to {@link OrderStatus#IN_PROGRESS} and the driver status remains {@link DriverStatus#BUSY}.
     *
     * @param orderId The ID of the order to accept.
     * @return The updated order as a {@link OrderResponseDto}.
     * @throws OrderNotFoundException If the order is not found.
     */
    @Transactional
    public OrderResponseDto acceptOrder(Long orderId) {
        // Lock order to prevent concurrent state transitions
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.ASSIGNED) {
            throw new IllegalStateException("Order cannot be accepted in its current state: " + order.getStatus());
        }

        order.setStatus(OrderStatus.IN_PROGRESS);

        Driver driver = order.getDriver();
        if (driver != null) {
            driver.setStatus(DriverStatus.BUSY);
            driverRepository.save(driver);
        }

        return orderMapper.toDto(order);
    }

    /**
     * Rejects an assigned order by a driver.
     * Resets the order status to {@link OrderStatus#CREATED}, removes the driver from the order,
     * and makes the driver {@link DriverStatus#AVAILABLE} again.
     *
     * @param orderId The ID of the order to reject.
     * @return The updated order as a {@link OrderResponseDto}.
     * @throws OrderNotFoundException If the order is not found.
     * @throws IllegalStateException  If the order is not in {@link OrderStatus#ASSIGNED} state.
     */
    @Transactional
    public OrderResponseDto rejectOrder(Long orderId) {

        // Lock order to prevent concurrent state transitions
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.ASSIGNED) {
            throw new IllegalStateException("Order cannot be rejected in its current state: " + order.getStatus());
        }

        Driver driver = order.getDriver();

        // reset order
        order.setDriver(null);
        order.setStatus(OrderStatus.CREATED);

        // driver becomes available again
        if (driver != null) {
            driver.setStatus(DriverStatus.AVAILABLE);
            driverRepository.save(driver);
        }

        return orderMapper.toDto(order);
    }

    /**
     * Completes an existing order.
     * Updates the order status to {@link OrderStatus#COMPLETED} and makes the driver {@link DriverStatus#AVAILABLE}.
     *
     * @param orderId The ID of the order to complete.
     * @param dto     The completion details.
     * @return The updated order as a {@link OrderResponseDto}.
     * @throws OrderNotFoundException If the order is not found.
     */
    @Transactional
    public OrderResponseDto completeOrder(Long orderId, CompleteOrderRequestDto dto) {

        // Lock order to prevent concurrent state transitions
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Order cannot be completed in its current state: " + order.getStatus());
        }

        // Mark order as completed
        order.setStatus(OrderStatus.COMPLETED);

        String comment = dto.getComment();

        if (comment == null || comment.isBlank()) {
            comment = "Order completed successfully";
        }

        order.setComment(comment);

        Driver driver = order.getDriver();

        // If driver exists, free him
        if (driver != null) {
            driver.setStatus(DriverStatus.AVAILABLE);
            driverRepository.save(driver);
        }

        return orderMapper.toDto(order);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id The ID of the order to retrieve.
     * @return The found order as a {@link OrderResponseDto}.
     * @throws OrderNotFoundException If the order is not found.
     */
    public OrderResponseDto getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        return orderMapper.toDto(order);
    }

    /**
     * Retrieves a paginated list of all orders.
     *
     * @param pageable Pagination and sorting information.
     * @return A {@link Page} of {@link OrderResponseDto}.
     */
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto);

    }
}
