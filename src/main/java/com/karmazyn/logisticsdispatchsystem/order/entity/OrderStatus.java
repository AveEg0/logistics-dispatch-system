package com.karmazyn.logisticsdispatchsystem.order.entity;

/**
 * Enumeration of possible delivery order statuses.
 */
public enum OrderStatus {
    /**
     * Order has been created and is waiting for driver assignment.
     */
    CREATED,
    /**
     * A driver has been assigned to the order.
     */
    ASSIGNED,
    /**
     * Driver has accepted the order and is currently performing the delivery.
     */
    IN_PROGRESS,
    /**
     * Delivery has been successfully completed.
     */
    COMPLETED,
    /**
     * Order has been canceled and will not be fulfilled.
     */
    CANCELLED
}
