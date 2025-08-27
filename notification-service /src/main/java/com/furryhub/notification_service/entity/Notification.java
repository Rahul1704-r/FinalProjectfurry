package com.furryhub.notification_service.entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_email", nullable=false)
    private String userEmail;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false, columnDefinition = "text")
    private String message;

    @Column(nullable=false, length=50)
    private String type;

    @Column(name="booking_id")
    private Long bookingId;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @Column(name="read", nullable=false)
    private boolean read;
}
