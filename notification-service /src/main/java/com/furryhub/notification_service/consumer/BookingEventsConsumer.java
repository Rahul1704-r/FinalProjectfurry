package com.furryhub.notification_service.consumer;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.furryhub.notification_service.dto.BookingEventPayload;
import com.furryhub.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventsConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NotificationService service;

    @KafkaListener(topics = "booking.created", groupId = "notification-consumers")
    public void onCreated(ConsumerRecord<String, String> rec) {
        handle(rec, "BOOKING_CREATED");
    }

    @KafkaListener(topics = "booking.accepted", groupId = "notification-consumers")
    public void onAccepted(ConsumerRecord<String, String> rec) {
        handle(rec, "BOOKING_ACCEPTED");
    }

    @KafkaListener(topics = "booking.completed", groupId = "notification-consumers")
    public void onCompleted(ConsumerRecord<String, String> rec) {
        handle(rec, "BOOKING_COMPLETED");
    }

    @KafkaListener(topics = "booking.cancelled", groupId = "notification-consumers")
    public void onCancelled(ConsumerRecord<String, String> rec) {
        handle(rec, "BOOKING_CANCELLED");
    }

    private void handle(ConsumerRecord<String,String> rec, String fallbackType) {
        try {
            var payload = objectMapper.readValue(rec.value(), BookingEventPayload.class);
            String type = payload.getEventType() != null ? payload.getEventType() : fallbackType;

            // Who to notify: for created/accepted/completed/cancelled we notify the booking user.
            // You can also notify provider owner on created/cancelled if desired.
            String user = payload.getUserEmail();
            if (user == null || user.isBlank()) {
                log.warn("No userEmail in event; skipping. value={}", rec.value());
                return;
            }

            String title = switch (type) {
                case "BOOKING_ACCEPTED" -> "Your booking was accepted";
                case "BOOKING_COMPLETED" -> "Your booking is completed";
                case "BOOKING_CANCELLED" -> "Your booking was cancelled";
                default -> "Your booking was created";
            };

            String message = "Booking #" + payload.getBookingId() + " [" + type + "] "
                    + "from " + payload.getStartTime() + " to " + payload.getEndTime();

            service.save(user, title, message, type, payload.getBookingId());

            log.info("Notification saved for {} type={} bookingId={}", user, type, payload.getBookingId());
        } catch (Exception e) {
            log.error("Failed to handle event: {}", rec.value(), e);
            // TODO: push to DLT if needed
        }
    }
}
