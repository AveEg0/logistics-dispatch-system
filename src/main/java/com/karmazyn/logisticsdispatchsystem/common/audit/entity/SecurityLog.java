package com.karmazyn.logisticsdispatchsystem.common.audit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;

import java.time.OffsetDateTime;

@Entity
@Table(name = "security_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SecurityAction action;

    @Column(nullable = false)
    private boolean success;

    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String userAgent;

    @Column(columnDefinition = "TEXT")
    private String requestUri;

    private String httpMethod;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "created_at", insertable = false, updatable = false)
    @Generated
    private OffsetDateTime createdAt;
}