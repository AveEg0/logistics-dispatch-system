package com.karmazyn.logisticsdispatchsystem.common.audit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserAction action;

    private String entity;

    private Long entityId;

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
