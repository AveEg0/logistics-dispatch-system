-- ENUM TYPES

CREATE TYPE user_role AS ENUM ('ADMIN', 'DISPATCHER', 'DRIVER');

CREATE TYPE driver_status AS ENUM ('AVAILABLE', 'BUSY', 'OFFLINE');

CREATE TYPE order_status AS ENUM ('CREATED', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');


-- USERS

CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email TEXT NOT NULL UNIQUE
        CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    password TEXT NOT NULL
        CHECK (length(password) >= 6),
    role user_role NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    password_changed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


-- DRIVERS

CREATE TABLE drivers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    status driver_status NOT NULL DEFAULT 'AVAILABLE',
    current_location TEXT,
    user_id BIGINT UNIQUE,

    CONSTRAINT fk_driver_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);


-- ORDERS

CREATE TABLE orders (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pickup_location TEXT NOT NULL,
    delivery_location TEXT NOT NULL,
    status order_status NOT NULL DEFAULT 'CREATED',
    driver_id BIGINT,
    created_by BIGINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_order_driver
        FOREIGN KEY (driver_id)
        REFERENCES drivers(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_order_user
        FOREIGN KEY (created_by)
        REFERENCES users(id)
        ON DELETE SET NULL
);