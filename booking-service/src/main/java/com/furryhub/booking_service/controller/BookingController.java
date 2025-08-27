package com.furryhub.booking_service.controller;


import com.furryhub.booking_service.dto.BookingRequest;
import com.furryhub.booking_service.dto.BookingResponse;
import com.furryhub.booking_service.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    private String emailOrThrow(String email) {
        if (email == null || email.isBlank()) throw new RuntimeException("Missing X-User-Email");
        return email;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(
            @RequestHeader(value = "X-User-Email", required = false) String email,
            @Valid @RequestBody BookingRequest req) {
        return ResponseEntity.ok(service.create(emailOrThrow(email), req));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> my(
            @RequestHeader(value = "X-User-Email", required = false) String email) {
        return ResponseEntity.ok(service.myBookings(emailOrThrow(email)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Email", required = false) String email) {
        return ResponseEntity.ok(service.getById(id, emailOrThrow(email)));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<BookingResponse> accept(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Email", required = false) String email) {
        return ResponseEntity.ok(service.accept(id, emailOrThrow(email)));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<BookingResponse> complete(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Email", required = false) String email) {
        return ResponseEntity.ok(service.complete(id, emailOrThrow(email)));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancel(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Email", required = false) String email) {
        return ResponseEntity.ok(service.cancel(id, emailOrThrow(email)));
    }
}
