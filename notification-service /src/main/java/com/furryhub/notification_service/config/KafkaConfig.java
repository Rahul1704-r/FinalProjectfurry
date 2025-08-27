package com.furryhub.notification_service.config;



import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.*;

@Configuration
public class KafkaConfig {

    // Optional: auto-create topics (if broker allows)
    @Bean
    public NewTopic bookingCreated() {
        return new NewTopic("booking.created", 1, (short)1);
    }
    @Bean
    public NewTopic bookingAccepted() {
        return new NewTopic("booking.accepted", 1, (short)1);
    }
    @Bean
    public NewTopic bookingCompleted() {
        return new NewTopic("booking.completed", 1, (short)1);
    }
    @Bean
    public NewTopic bookingCancelled() {
        return new NewTopic("booking.cancelled", 1, (short)1);
    }
}
