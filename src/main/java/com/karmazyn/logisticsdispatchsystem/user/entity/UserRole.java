package com.karmazyn.logisticsdispatchsystem.user.entity;

/**
 * Enumeration of available user roles within the system.
 */
public enum UserRole {
    /**
     * Administrator with full access to the system.
     */
    ADMIN,
    /**
     * Dispatcher responsible for managing orders and drivers.
     */
    DISPATCHER,
    /**
     * Driver responsible for delivering orders.
     */
    DRIVER
}
