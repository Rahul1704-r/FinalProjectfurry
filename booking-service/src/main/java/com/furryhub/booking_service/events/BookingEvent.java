package com.furryhub.booking_service.events;

import com.furryhub.booking_service.entity.BookingStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class BookingEvent {
    private String eventId;
    private String eventType;
    private String eventTime;
    private Long bookingId;

    private String userEmail;
    private Long petId;
    private Integer providerId;
    private Integer serviceTypeId;
    private String startTime;
    private String endTime;
    private BookingStatus status;
    private String message;
}
