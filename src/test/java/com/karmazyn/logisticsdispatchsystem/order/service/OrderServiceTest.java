package com.karmazyn.logisticsdispatchsystem.order.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.DriverNotAvailableException;
import com.karmazyn.logisticsdispatchsystem.common.exception.OrderNotFoundException;
import com.karmazyn.logisticsdispatchsystem.driver.entity.Driver;
import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import com.karmazyn.logisticsdispatchsystem.driver.repository.DriverRepository;
import com.karmazyn.logisticsdispatchsystem.driver.service.DriverService;
import com.karmazyn.logisticsdispatchsystem.order.dto.AssignDriverRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.CompleteOrderRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.dto.OrderResponseDto;
import com.karmazyn.logisticsdispatchsystem.order.entity.Order;
import com.karmazyn.logisticsdispatchsystem.order.entity.OrderStatus;
import com.karmazyn.logisticsdispatchsystem.order.mapper.OrderMapper;
import com.karmazyn.logisticsdispatchsystem.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private DriverRepository driverRepository;
    @Mock private DriverService driverService;
    @Mock private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    //assignDriver

    @Test
    void assignDriver_Success_OrderAssignedAndDriverReserved() {
        // Given
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CREATED);

        Driver driver = new Driver();
        driver.setId(2L);
        driver.setStatus(DriverStatus.AVAILABLE);

        AssignDriverRequestDto dto = new AssignDriverRequestDto();
        dto.setDriverId(2L);

        OrderResponseDto expected = new OrderResponseDto();

        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));
        when(driverRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(driver));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(expected);

        // When
        OrderResponseDto result = orderService.assignDriver(1L, dto);

        // Then
        assertEquals(OrderStatus.ASSIGNED, order.getStatus());
        assertEquals(driver, order.getDriver());
        verify(driverService).changeDriverStatus(driver, DriverStatus.RESERVED);
        assertEquals(expected, result);
    }

    @Test
    void assignDriver_DriverNotAvailable_ThrowsDriverNotAvailableException() {
        // Given
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);

        Driver driver = new Driver();
        driver.setId(2L);
        driver.setStatus(DriverStatus.BUSY);

        AssignDriverRequestDto dto = new AssignDriverRequestDto();
        dto.setDriverId(2L);

        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));
        when(driverRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(driver));

        // When / Then
        assertThrows(DriverNotAvailableException.class,
                () -> orderService.assignDriver(1L, dto));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void assignDriver_OrderNotCreated_ThrowsIllegalStateException() {
        // Given
        Order order = new Order();
        // already assigned
        order.setStatus(OrderStatus.ASSIGNED);

        AssignDriverRequestDto dto = new AssignDriverRequestDto();
        dto.setDriverId(2L);

        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        // When / Then
        assertThrows(IllegalStateException.class,
                () -> orderService.assignDriver(1L, dto));
        verifyNoInteractions(driverRepository);
    }

    // rejectOrder

    @Test
    void rejectOrder_Success_OrderResetToCreatedAndDriverAvailable() {
        // Given
        Driver driver = new Driver();
        driver.setStatus(DriverStatus.RESERVED);

        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.ASSIGNED);
        order.setDriver(driver);

        OrderResponseDto expected = new OrderResponseDto();

        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(expected);

        // When
        OrderResponseDto result = orderService.rejectOrder(1L);

        // Then
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertNull(order.getDriver());
        verify(driverService).changeDriverStatus(driver, DriverStatus.AVAILABLE);
        assertEquals(expected, result);
    }

    // completeOrder

    @Test
    void completeOrder_Success_OrderCompletedAndDriverFreed() {
        // Given
        Driver driver = new Driver();
        driver.setStatus(DriverStatus.BUSY);

        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setDriver(driver);

        CompleteOrderRequestDto dto = new CompleteOrderRequestDto();
        dto.setComment("Delivered on time");

        OrderResponseDto expected = new OrderResponseDto();

        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(expected);

        // When
        OrderResponseDto result = orderService.completeOrder(1L, dto);

        // Then
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        verify(driverService).changeDriverStatus(driver, DriverStatus.AVAILABLE);
        assertEquals(expected, result);
    }

    @Test
    void completeOrder_OrderNotFound_ThrowsOrderNotFoundException() {
        // Given
        when(orderRepository.findByIdForUpdate(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(OrderNotFoundException.class,
                () -> orderService.completeOrder(99L, new CompleteOrderRequestDto()));
    }
}