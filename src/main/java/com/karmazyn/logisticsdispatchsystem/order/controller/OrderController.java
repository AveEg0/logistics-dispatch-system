package com.karmazyn.logisticsdispatchsystem.order.controller;

import com.karmazyn.logisticsdispatchsystem.order.dto.CompleteOrderRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.CancelOrderRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.AssignDriverRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.CreateOrderRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.OrderResponseDto;
import com.karmazyn.logisticsdispatchsystem.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Create a new order
     */
    @PostMapping
    public OrderResponseDto createOrder(@Valid @RequestBody CreateOrderRequestDto dto) {
        return orderService.createOrder(dto);
    }

    /**
     * Assign a driver to order
     */
    @PutMapping("/{orderId}/assign")
    public OrderResponseDto assignDriver(
            @PathVariable Long orderId,
            @Valid @RequestBody AssignDriverRequestDto dto
    ) {
        return orderService.assignDriver(orderId, dto);
    }

    /**
     * Complete order
     */
    @PutMapping("/{orderId}/complete")
    public OrderResponseDto completeOrder(
            @PathVariable Long orderId,
            @Valid CompleteOrderRequestDto dto
    ) {
        return orderService.completeOrder(orderId, dto);
    }

    /**
     * Cancel order
     */
    @PutMapping("/{orderId}/cancel")
    public OrderResponseDto cancelOrder(
            @PathVariable Long orderId,
            @Valid CancelOrderRequestDto dto
    ) {
        return orderService.cancelOrder(orderId, dto);
    }

    /**
     * Accept order (by driver)
     */
    @PutMapping("/{orderId}/accept")
    public OrderResponseDto acceptOrder(@PathVariable Long orderId) {
        return orderService.acceptOrder(orderId);
    }

    /**
     * Reject order (by driver)
     */
    @PutMapping("/{orderId}/reject")
    public OrderResponseDto rejectOrder(@PathVariable Long orderId) {
        return orderService.rejectOrder(orderId);
    }

    /**
     * Get order by id
     */
    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    /**
     * Get all orders
     */
    @GetMapping
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }
}
