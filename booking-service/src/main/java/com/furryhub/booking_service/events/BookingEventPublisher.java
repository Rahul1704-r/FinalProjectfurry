package com.furryhub.booking_service.events;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventPublisher {
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public void publish(String topic, String key, BookingEvent event) {
        kafkaTemplate.send(topic, key, event);
    }
}
