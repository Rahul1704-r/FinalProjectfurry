package com.furryhub.booking_service.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingRequest {
    @NotNull private Long petId;
    @NotNull private Integer providerId;
    @NotNull private Integer serviceTypeId;
    @NotNull private LocalDateTime startTime;
    @NotNull private LocalDateTime endTime;
    private String notes;
}
