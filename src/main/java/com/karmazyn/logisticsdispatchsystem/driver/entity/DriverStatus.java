package com.karmazyn.logisticsdispatchsystem.driver.entity;

/**
 * Enumeration of possible driver availability statuses.
 */
public enum DriverStatus {
    /**
     * Driver is active and ready to be assigned to a new order.
     */
    AVAILABLE,
    /**
     * Driver is currently performing a delivery.
     */
    BUSY,
    /**
     * Driver is not currently working.
     */
    OFFLINE,
    /**
     * Driver has been assigned to an order but hasn't started it yet.
     */
    RESERVED
}
