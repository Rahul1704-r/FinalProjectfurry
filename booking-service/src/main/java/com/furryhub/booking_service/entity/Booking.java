package com.furryhub.booking_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_email")
    private String userEmail;

    @Column(nullable = false, name = "pet_id")
    private Long petId;

    @Column(nullable = false, name = "provider_id")
    private Integer providerId;

    @Column(nullable = false, name = "service_type_id")
    private Integer serviceTypeId;

    @Column(nullable = false, name = "start_time")
    private LocalDateTime startTime;

    @Column(nullable = false, name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;
}
