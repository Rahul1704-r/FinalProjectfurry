package com.furryhub.notification_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingEventPayload {
    private String eventType;
    private Long bookingId;
    private Integer providerId;
    private Integer serviceTypeId;
    private String userEmail;
    private String providerOwnerEmail;
    private String startTime;
    private String endTime;
    private String notes;
}
