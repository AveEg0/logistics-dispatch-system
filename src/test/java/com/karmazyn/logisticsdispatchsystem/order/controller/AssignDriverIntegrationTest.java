package com.karmazyn.logisticsdispatchsystem.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karmazyn.logisticsdispatchsystem.driver.entity.Driver;
import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import com.karmazyn.logisticsdispatchsystem.driver.repository.DriverRepository;
import com.karmazyn.logisticsdispatchsystem.order.dto.AssignDriverRequestDto;
import com.karmazyn.logisticsdispatchsystem.order.entity.Order;
import com.karmazyn.logisticsdispatchsystem.order.entity.OrderStatus;
import com.karmazyn.logisticsdispatchsystem.order.repository.OrderRepository;
import com.karmazyn.logisticsdispatchsystem.security.service.JwtService;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for PUT /orders/{id}/assign endpoint.
 *
 * What is tested end-to-end:
 *   JwtAuthFilter → SecurityFilterChain → OrderController
 *     → OrderService (@Transactional + pessimistic locking)
 *       → OrderRepository / DriverRepository (PostgreSQL via Testcontainers)
 *         ← GlobalExceptionHandler (ControllerAdvice)
 *
 * What is NOT mocked:
 *   - Security filter chain ( JWT is generated and parsed)
 *   - Database (PostgreSQL via Testcontainers with Flyway migrations)
 *   - OrderService and DriverService
 *   - GlobalExceptionHandler
 *
 * The n8n webhook call is suppressed
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class AssignDriverIntegrationTest {


    @Container
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("logistics_test")
                    .withUsername("test")
                    .withPassword("test");

    /**
     * Override Spring datasource properties at runtime with Testcontainers coordinates.
     * Flyway migrations run automatically on startup of the Spring context,
     * creating all 8 schema versions including PostgreSQL native ENUM types.
     */
    @DynamicPropertySource
    static void registerContainerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",                postgres::getJdbcUrl);
        registry.add("spring.datasource.username",           postgres::getUsername);
        registry.add("spring.datasource.password",           postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }


    @Autowired private MockMvc         mockMvc;
    @Autowired private ObjectMapper    objectMapper;
    @Autowired private JwtService      jwtService;
    @Autowired private JdbcTemplate    jdbcTemplate;
    @Autowired private UserRepository  userRepository;
    @Autowired private DriverRepository driverRepository;
    @Autowired private OrderRepository  orderRepository;

    // Test-scoped state
    private User   dispatcherUser;
    private Driver availableDriver;

    /**
     * Resets all application tables before each test so tests are fully independent.
     * Uses PostgreSQL CASCADE to handle FK constraints in a single statement.
     * RESTART IDENTITY resets sequences for predictable IDs.
     *
     * Then creates the minimal fixture needed by both tests:
     *   - one DISPATCHER user  → produces the JWT used to authenticate requests
     *   - one DRIVER user + Driver profile (AVAILABLE by default)
     */
    @BeforeEach
    void resetDatabaseAndCreateBaseFixture() {
        jdbcTemplate.execute(
                "TRUNCATE TABLE orders, refresh_tokens, user_logs, security_logs, drivers, users " +
                        "RESTART IDENTITY CASCADE"
        );

        // The entity that will be authenticated in requests
        dispatcherUser = userRepository.save(
                buildUser("dispatcher@logistics-test.com", UserRole.DISPATCHER)
        );

        // The entity that will be assigned to orders
        User driverUser = userRepository.save(
                buildUser("driver@logistics-test.com", UserRole.DRIVER)
        );
        availableDriver = driverRepository.save(
                buildDriver("John Doe", DriverStatus.AVAILABLE, driverUser)
        );
    }

    // Happy path

    @Test
    @DisplayName("""
        GIVEN a CREATED order and an AVAILABLE driver,
        WHEN a DISPATCHER authenticates with a valid JWT and calls PUT /orders/{id}/assign,
        THEN the API returns HTTP 200 with the order in ASSIGNED state referencing the driver,
        AND the database reflects order.status=ASSIGNED and driver.status=RESERVED.
        """)
    void givenAvailableDriverAndCreatedOrder_whenDispatcherAssigns_thenReturns200AndPersistsStateTransitions()
            throws Exception {

        // Arrange

        Order order = orderRepository.save(
                buildOrder("LA, CA", "Milwaukee, WI", dispatcherUser)
        );

        // Generate a real signed JWT, JwtAuthFilter will validate this token
        // and call userRepository.findByEmail("dispatcher@logistics-test.com") to set the SecurityContext
        // The user exists in the DB (created in @BeforeEach).
        String dispatcherJwt = jwtService.generateToken(dispatcherUser);

        AssignDriverRequestDto requestBody = new AssignDriverRequestDto();
        requestBody.setDriverId(availableDriver.getId());

        // Act

        mockMvc.perform(
                        put("/orders/{id}/assign", order.getId())
                                .header("Authorization", "Bearer " + dispatcherJwt)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody))
                )

                //  Assert: HTTP response
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // Core status transition visible in the response DTO
                .andExpect(jsonPath("$.status").value("ASSIGNED"))
                // Driver reference must be populated in the response DTO
                .andExpect(jsonPath("$.driverId").value(availableDriver.getId()))
                .andExpect(jsonPath("$.driverName").value("John Doe"));

        // Assert: Database state
        // Re-fetch from DB to confirm the transaction actually committed

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(savedOrder.getStatus())
                .as("Order status must have transitioned CREATED → ASSIGNED")
                .isEqualTo(OrderStatus.ASSIGNED);
        assertThat(savedOrder.getDriver())
                .as("Order must have a driver reference after successful assignment")
                .isNotNull();
        assertThat(savedOrder.getDriver().getId())
                .as("Order must reference the correct driver")
                .isEqualTo(availableDriver.getId());

        Driver savedDriver = driverRepository.findById(availableDriver.getId()).orElseThrow();
        assertThat(savedDriver.getStatus())
                .as("Driver status must have transitioned AVAILABLE → RESERVED after assignment")
                .isEqualTo(DriverStatus.RESERVED);
    }

    // Conflict path

    @Test
    @DisplayName("""
        GIVEN a CREATED order and a BUSY driver,
        WHEN a DISPATCHER calls PUT /orders/{id}/assign,
        THEN the API returns HTTP 409 CONFLICT (GlobalExceptionHandler → DriverNotAvailableException),
        AND neither the order status nor the driver status is changed in the database.
        """)
    void givenBusyDriverAndCreatedOrder_whenDispatcherAssigns_thenReturns409AndLeavesDbUnchanged()
            throws Exception {

        // Arrange

        // Put the driver into BUSY state to trigger DriverNotAvailableException in OrderService
        availableDriver.setStatus(DriverStatus.BUSY);
        driverRepository.save(availableDriver);

        Order order = orderRepository.save(
                buildOrder("Dallas, TX", "NYC, NY", dispatcherUser)
        );

        String dispatcherJwt = jwtService.generateToken(dispatcherUser);

        AssignDriverRequestDto requestBody = new AssignDriverRequestDto();
        requestBody.setDriverId(availableDriver.getId());

        // Act

        mockMvc.perform(
                        put("/orders/{id}/assign", order.getId())
                                .header("Authorization", "Bearer " + dispatcherJwt)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody))
                )

                // Assert: HTTP response
                // GlobalExceptionHandler maps DriverNotAvailableException 409
                // and writes ex.getMessage() as a plain String body (not JSON)
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("not available")));

        // Assert: No state mutation in the database

        Order unchangedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(unchangedOrder.getStatus())
                .as("Order status must remain CREATED — transactional rollback on exception")
                .isEqualTo(OrderStatus.CREATED);
        assertThat(unchangedOrder.getDriver())
                .as("Order must have no driver reference after a failed assignment")
                .isNull();

        Driver unchangedDriver = driverRepository.findById(availableDriver.getId()).orElseThrow();
        assertThat(unchangedDriver.getStatus())
                .as("Driver status must remain BUSY — no side effects from the failed assignment")
                .isEqualTo(DriverStatus.BUSY);
    }

    @Test
    @DisplayName("""
    GIVEN two dispatcher threads racing to assign the SAME order to their own AVAILABLE driver,
    WHEN both PUT /orders/{orderId}/assign requests hit the DB simultaneously,
    THEN the pessimistic lock (SELECT ... FOR UPDATE on the orders row) serializes
         the two transactions so that:
         - exactly ONE thread wins   → HTTP 200, order.status = ASSIGNED
         - exactly ONE thread loses  → HTTP 409 (IllegalStateException:
           "Order cannot be assigned in its current state: ASSIGNED"
            ← GlobalExceptionHandler)
    AND the database is consistent: one driver RESERVED, one driver still AVAILABLE.
    """)
    void givenTwoConcurrentAssignRequests_pessimisticLockEnsuresExactlyOneSucceedsAndOneIsRejected()
            throws InterruptedException {

        // Arrange

        /*
         * Two DISTINCT drivers - each thread gets its own driver
         *
         * If both threads tried to use the SAME driver, one would fail with
         * DriverNotAvailableException (status already RESERVED)
         * That maps to HTTP 409, but it tests driver-level locking
         * ORDER row lock (findByIdForUpdate on Order) is the guard against double-assignment
         *
         * With two AVAILABLE drivers:
         *   - Thread 1 locks order row → checks status = CREATED -> proceeds -> commits
         *   - Thread 2 acquires the lock AFTER commit -> reads status = ASSIGNED -> 409
         *   The driver lock is never the deciding factor here
         */
        User driverUser2 = userRepository.save(
                buildUser("driver2@test.com", UserRole.DRIVER));
        Driver driver2 = driverRepository.save(
                buildDriver("John Doe", DriverStatus.AVAILABLE, driverUser2));

        // The shared order — BOTH threads race to assign it
        Order sharedOrder = orderRepository.save(
                buildOrder("LA, CA", "NYC, NY", dispatcherUser));

        /*
         * One JWT for both threads
         * Spring Security's SecurityContextHolder uses ThreadLocalSecurityContextHolderStrategy
         * Each thread maintains its own SecurityContext, so sharing one JWT string is safe
         * JwtAuthFilter sets the SecurityContext independently per thread per request
         */
        String jwt = jwtService.generateToken(dispatcherUser);

        // Serialize request bodies BEFORE spawning threads
        final String bodyThread1;
        final String bodyThread2;
        try {
            bodyThread1 = objectMapper.writeValueAsString(dtoWith(availableDriver.getId()));
            bodyThread2 = objectMapper.writeValueAsString(dtoWith(driver2.getId()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize request bodies", e);
        }

        // Concurrency primitives

        /*
         * startGate (count = 1):
         *   Both worker threads call startGate.await() and block
         *   Main thread calls startGate.countDown() -> count drops to 0 -> both unblock together
         *   This maximizes the chance of true concurrent execution at the DB level
         *
         * doneLatch (count = 2):
         *   Main thread calls doneLatch.await() and blocks
         *   Each worker thread calls doneLatch.countDown() when it finishes
         *   When count reaches 0 -> main thread unblocks and can read results
         */
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch doneLatch  = new CountDownLatch(2);

        /*
         * Thread-safe result collector
         * Written by worker threads (concurrently), read by main thread only AFTER
         * doneLatch.await() completes, at that point both threads are done
         * so reads are safe without additional synchronization
         */
        List<Integer> statusCodes = Collections.synchronizedList(new ArrayList<>());

        boolean bothFinished;


        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {

            //Thread 1 - assigns driver1 to sharedOrder
            executor.submit(() -> {
                try {
                    startGate.await();

                    int status = mockMvc.perform(
                                    put("/orders/{id}/assign", sharedOrder.getId())
                                            .header("Authorization", "Bearer " + jwt)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(bodyThread1))
                            .andReturn()
                            .getResponse()
                            .getStatus();

                    statusCodes.add(status);

                } catch (Exception e) {
                    // Unexpected exception - mark as 500 so the assertion will fail
                    statusCodes.add(500);
                } finally {
                    doneLatch.countDown();
                }
            });

            // Thread 2 - assigns driver2 to the SAME sharedOrder
            executor.submit(() -> {
                try {
                    startGate.await();

                    int status = mockMvc.perform(
                                    put("/orders/{id}/assign", sharedOrder.getId())
                                            .header("Authorization", "Bearer " + jwt)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(bodyThread2))
                            .andReturn()
                            .getResponse()
                            .getStatus();

                    statusCodes.add(status);

                } catch (Exception e) {
                    statusCodes.add(500);
                } finally {
                    doneLatch.countDown();
                }
            });

            // Act

            // both threads released
            startGate.countDown();


            bothFinished = doneLatch.await(10, TimeUnit.SECONDS);
        }

        // Assert: concurrency control

        assertThat(bothFinished)
                .as("""
                Both threads must finish within 10 seconds.
                If this fails: suspect a deadlock or infinite lock wait
                """)
                .isTrue();

        assertThat(statusCodes)
                .as("Both threads must have produced exactly one HTTP status code each")
                .hasSize(2);

        long successCount  = statusCodes.stream().filter(s -> s == 200).count();
        long conflictCount = statusCodes.stream().filter(s -> s == 409).count();

        assertThat(successCount)
                .as("Exactly ONE request must succeed (HTTP 200)")
                .isEqualTo(1);

        assertThat(conflictCount)
                .as("""
                Exactly ONE request must fail (HTTP 409)
                If this count is 0: pessimistic locking is broken
                """)
                .isEqualTo(1);

        // Assert: database integrity

        Order finalOrder = orderRepository.findById(sharedOrder.getId()).orElseThrow();

        assertThat(finalOrder.getStatus())
                .as("Order must be in ASSIGNED state")
                .isEqualTo(OrderStatus.ASSIGNED);

        assertThat(finalOrder.getDriver())
                .as("Order must reference exactly one driver - set by the winning transaction")
                .isNotNull();

        // Winner's driver -> RESERVED; loser's driver -> AVAILABLE
        Driver finalDriver1 = driverRepository.findById(availableDriver.getId()).orElseThrow();
        Driver finalDriver2 = driverRepository.findById(driver2.getId()).orElseThrow();

        long reservedCount  = Stream.of(finalDriver1, finalDriver2)
                .filter(d -> d.getStatus() == DriverStatus.RESERVED)
                .count();
        long availableCount = Stream.of(finalDriver1, finalDriver2)
                .filter(d -> d.getStatus() == DriverStatus.AVAILABLE)
                .count();

        assertThat(reservedCount)
                .as("Exactly ONE driver must be RESERVED")
                .isEqualTo(1);

        assertThat(availableCount)
                .as("The loser's driver must remain AVAILABLE")
                .isEqualTo(1);

        // The driver on the order must be the one that is RESERVED
        Long assignedDriverId = finalOrder.getDriver().getId();
        Driver driverOnOrder = driverRepository.findById(assignedDriverId).orElseThrow();

        assertThat(driverOnOrder.getStatus())
                .as("The driver referenced by the order must be RESERVED, " +
                        "order.driverId and driver.status must be consistent")
                .isEqualTo(DriverStatus.RESERVED);
    }


    private static User buildUser(String email, UserRole role) {
        User u = new User();
        u.setEmail(email);
        u.setRole(role);
        // BCrypt hash of "test-password" (value does not affect these tests)
        u.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy");
        u.setEnabled(true);
        return u;
    }

    private static Driver buildDriver(String name, DriverStatus status, User linkedUser) {
        Driver d = new Driver();
        d.setName(name);
        d.setStatus(status);
        d.setUser(linkedUser);
        return d;
    }

    private static Order buildOrder(String pickup, String delivery, User createdBy) {
        Order o = new Order();
        o.setPickupLocation(pickup);
        o.setDeliveryLocation(delivery);
        o.setStatus(OrderStatus.CREATED);
        o.setCreatedBy(createdBy);
        return o;
    }

    private static AssignDriverRequestDto dtoWith(Long driverId) {
        AssignDriverRequestDto dto = new AssignDriverRequestDto();
        dto.setDriverId(driverId);
        return dto;
    }
}