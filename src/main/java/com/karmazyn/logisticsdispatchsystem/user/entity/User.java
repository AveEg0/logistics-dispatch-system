package com.karmazyn.logisticsdispatchsystem.user.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

/**
 * Entity representing a system user.
 * Stores authentication details, role, and account status.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private UserRole role;

    private boolean enabled = true;

    private boolean passwordChanged = false;

    @Column(name = "created_at", insertable = false, updatable = false)
    @Generated
    private OffsetDateTime createdAt;
}
