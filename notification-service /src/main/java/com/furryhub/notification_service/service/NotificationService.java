package com.furryhub.notification_service.service;

// service/NotificationService.java

import com.furryhub.notification_service.dto.NotificationResponse;
import com.furryhub.notification_service.entity.Notification;
import com.furryhub.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;

    @Transactional
    public void save(String userEmail, String title, String message, String type, Long bookingId) {
        Notification n = Notification.builder()
                .userEmail(userEmail)
                .title(title)
                .message(message)
                .type(type)
                .bookingId(bookingId)
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();
        repo.save(n);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> list(String userEmail) {
        return repo.findAllForUser(userEmail)
                .stream()
                .map(n -> NotificationResponse.builder()
                        .id(n.getId())
                        .title(n.getTitle())
                        .message(n.getMessage())
                        .type(n.getType())
                        .bookingId(n.getBookingId())
                        .createdAt(n.getCreatedAt())
                        .read(n.isRead())
                        .build())
                .toList();
    }

    @Transactional
    public void markRead(Long id, String userEmail) {
        var n = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        if (!n.getUserEmail().equalsIgnoreCase(userEmail)) {
            throw new SecurityException("Forbidden");
        }
        n.setRead(true);
        repo.save(n);
    }


}
