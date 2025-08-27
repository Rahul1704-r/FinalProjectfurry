package com.furryhub.notification_service.dto;

// dto/NotificationResponse.java


import lombok.*;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private String type;
    private Long bookingId;
    private LocalDateTime createdAt;
    private boolean read;
}
