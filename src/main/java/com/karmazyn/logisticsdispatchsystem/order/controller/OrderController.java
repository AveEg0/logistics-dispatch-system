package com.karmazyn.logisticsdispatchsystem.order.controller;

import com.karmazyn.logisticsdispatchsystem.common.audit.annotation.AuditAction;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserAction;
import com.karmazyn.logisticsdispatchsystem.n8n.annotation.WebhookEvent;
import com.karmazyn.logisticsdispatchsystem.n8n.entity.WebhookEventType;
import com.karmazyn.logisticsdispatchsystem.order.dto.*;
import com.karmazyn.logisticsdispatchsystem.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing delivery orders.
 * Provides endpoints for order creation, driver assignment, and order lifecycle management.
 */
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Operations related to delivery orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Creates a new delivery order.
     *
     * @param dto the order creation request data
     * @return the created order details
     */
    @AuditAction(UserAction.CREATE_ORDER)
    @PostMapping
    @Operation(summary = "Create a new order", description = "Places a new delivery order in the system with status PENDING.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public OrderResponseDto createOrder(@Valid @RequestBody CreateOrderRequestDto dto) {
        return orderService.createOrder(dto);
    }

    /**
     * Assigns a specific driver to an order.
     *
     * @param orderId the unique identifier of the order
     * @param dto the driver assignment request data
     * @return the updated order details
     */
    @AuditAction(UserAction.ASSIGN_DRIVER)
    @WebhookEvent(WebhookEventType.ORDER_ASSIGNED)
    @PutMapping("/{orderId}/assign")
    @Operation(summary = "Assign driver to order", description = "Manually assigns a driver to a pending order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver assigned"),
            @ApiResponse(responseCode = "404", description = "Order or Driver not found"),
            @ApiResponse(responseCode = "400", description = "Driver not available or invalid order status")
    })
    public OrderResponseDto assignDriver(
            @Parameter(description = "ID of the order", example = "1")
            @PathVariable Long orderId,
            @Valid @RequestBody AssignDriverRequestDto dto
    ) {

        return orderService.assignDriver(orderId, dto);

    }

    /**
     * Marks an order as completed.
     *
     * @param orderId the unique identifier of the order
     * @param dto the completion request data (optional comment)
     * @return the updated order details
     */
    @PreAuthorize("hasRole('DRIVER')")
    @AuditAction(UserAction.COMPLETE_ORDER)
    @WebhookEvent(WebhookEventType.ORDER_COMPLETED)
    @PutMapping("/{orderId}/complete")
    @Operation(summary = "Complete order", description = "Marks an in-progress order as COMPLETED.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order completed"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid order status for completion")
    })
    public OrderResponseDto completeOrder(
            @Parameter(description = "ID of the order", example = "1001")
            @PathVariable Long orderId,
            @Valid CompleteOrderRequestDto dto
    ) {
        return orderService.completeOrder(orderId, dto);
    }

    /**
     * Cancels an existing order.
     *
     * @param orderId the unique identifier of the order
     * @param dto the cancellation request data (optional comment)
     * @return the updated order details
     */
    @AuditAction(UserAction.CANCEL_ORDER)
    @WebhookEvent(WebhookEventType.ORDER_CANCELLED)
    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order", description = "Marks an order as CANCELED. This can only be done for orders in certain states.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order canceled"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Order cannot be canceled in its current state")
    })
    public OrderResponseDto cancelOrder(
            @Parameter(description = "ID of the order", example = "1001")
            @PathVariable Long orderId,
            @Valid @RequestBody CancelOrderRequestDto dto
    ) {
        return orderService.cancelOrder(orderId, dto);
    }

    /**
     * Accepts an assigned order (driver action).
     *
     * @param orderId the unique identifier of the order
     * @return the updated order details
     */
    @AuditAction(UserAction.ACCEPT_ORDER)
    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/{orderId}/accept")
    @Operation(summary = "Accept order", description = "Action for a driver to accept an order that has been assigned to them.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order accepted"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Order cannot be accepted (e.g., already accepted or not assigned)")
    })
    public OrderResponseDto acceptOrder(
            @Parameter(description = "ID of the order", example = "1")
            @PathVariable Long orderId) {
        return orderService.acceptOrder(orderId);
    }

    /**
     * Rejects an assigned order (driver action).
     *
     * @param orderId the unique identifier of the order
     * @return the updated order details
     */
    @AuditAction(UserAction.REJECT_ORDER)
    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/{orderId}/reject")
    @Operation(summary = "Reject order", description = "Action for a driver to reject an order that has been assigned to them.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order rejected"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Order cannot be rejected")
    })
    public OrderResponseDto rejectOrder(
            @Parameter(description = "ID of the order", example = "1")
            @PathVariable Long orderId) {
        return orderService.rejectOrder(orderId);
    }

    /**
     * Retrieves order details by their unique identifier.
     *
     * @param id the unique identifier of the order
     * @return the order details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Returns detailed information about a delivery order based on its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public OrderResponseDto getOrderById(
            @Parameter(description = "ID of the order to retrieve", example = "1001")
            @PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    /**
     * Retrieves a paginated list of orders.
     *
     * @param pageable pagination and sorting information
     * @return a page of order details
     */
    @GetMapping
    @Operation(summary = "Get orders", description = "Returns a paginated list of delivery orders in the system.")
    public Page<OrderResponseDto> getOrders(
            @ModelAttribute OrderFilterDto filter,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return orderService.getOrders(filter, pageable);
    }

    /**
     * Retrieves the current order assigned to the authenticated driver.
     *
     * @return an order
     */
    @GetMapping("/my-current")
    @PreAuthorize("hasRole('DRIVER')")
    public OrderResponseDto getMyCurrentOrder() {
        return orderService.getCurrentOrderForDriver();
    }
}
