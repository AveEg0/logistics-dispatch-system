package com.karmazyn.logisticsdispatchsystem.driver.entity;

import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status = DriverStatus.AVAILABLE;

    private String currentLocation;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
