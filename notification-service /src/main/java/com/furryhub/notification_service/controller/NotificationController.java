package com.furryhub.notification_service.controller;


import com.furryhub.notification_service.dto.NotificationResponse;
import com.furryhub.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> myNotifications(
            @RequestHeader("X-User-Email") String userEmail) {
        return ResponseEntity.ok(service.list(userEmail));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(
            @PathVariable Long id,
            @RequestHeader("X-User-Email") String userEmail) {
        service.markRead(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}
