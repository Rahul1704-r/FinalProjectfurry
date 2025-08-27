package com.furryhub.booking_service.dto;

import com.furryhub.booking_service.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private String userEmail;
    private Long petId;
    private Integer providerId;
    private Integer serviceTypeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private String notes;
    private LocalDateTime createdAt;
}
