package com.karmazyn.logisticsdispatchsystem.order.controller;

import com.karmazyn.logisticsdispatchsystem.order.dto.CompleteOrderDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.AssignDriverRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.CreateOrderRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.OrderResponseDto;
import com.karmazyn.logisticsdispatchsystem.order.service.OrderService;
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
     * Create new order
     */
    @PostMapping
    public OrderResponseDto createOrder(@RequestBody CreateOrderRequestDto dto) {
        return orderService.createOrder(dto);
    }

    /**
     * Assign driver to order
     */
    @PutMapping("/{orderId}/assign")
    public OrderResponseDto assignDriver(
            @PathVariable Long orderId,
            @RequestBody AssignDriverRequestDto dto
    ) {
        return orderService.assignDriver(orderId, dto);
    }

    /**
     * Complete order
     */
    @PutMapping("/{orderId}/complete")
    public OrderResponseDto completeOrder(
            @PathVariable Long orderId,
            @RequestBody CompleteOrderDto dto
    ) {
        return orderService.completeOrder(orderId, dto);
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
