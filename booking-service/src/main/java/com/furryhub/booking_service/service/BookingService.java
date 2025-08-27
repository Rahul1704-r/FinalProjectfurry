package com.furryhub.booking_service.service;

import com.furryhub.booking_service.client.CatalogClient;
import com.furryhub.booking_service.dto.BookingRequest;
import com.furryhub.booking_service.dto.BookingResponse;
import com.furryhub.booking_service.entity.Booking;
import com.furryhub.booking_service.entity.BookingStatus;
import com.furryhub.booking_service.events.BookingEvent;
import com.furryhub.booking_service.events.BookingEventPublisher;
import com.furryhub.booking_service.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository repo;
    private final CatalogClient catalog;
    private final BookingEventPublisher publisher;

    private BookingEvent evt(Booking b, String type, String msg, String providerOwnerEmail) {
        return BookingEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(type)
                .eventTime(Instant.now().toString())
                .bookingId(b.getId())
                .userEmail(b.getUserEmail())
                .petId(b.getPetId())
                .providerId(b.getProviderId())
                .serviceTypeId(b.getServiceTypeId())
                .startTime(b.getStartTime().toString())     // String
                .endTime(b.getEndTime().toString())
                .status(b.getStatus())
                .message(msg)
                .providerId(b.getProviderId())
                .build();
    }

    private BookingResponse toResponse(Booking b) {
        return new BookingResponse(
                b.getId(), b.getUserEmail(), b.getPetId(), b.getProviderId(), b.getServiceTypeId(),
                b.getStartTime(), b.getEndTime(), b.getStatus(), b.getNotes(), b.getCreatedAt()
        );
    }

    @Transactional
    public BookingResponse create(String userEmail, BookingRequest req) {
        if (req.getEndTime().isBefore(req.getStartTime())) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }

        // block overlap with PENDING or CONFIRMED at creation time
        boolean clash = repo.existsOverlap(
                req.getProviderId(),
                req.getStartTime(),
                req.getEndTime(),
                EnumSet.of(BookingStatus.PENDING, BookingStatus.CONFIRMED)
        );
        if (clash) throw new IllegalStateException("Time slot not available for provider");

        Booking b = Booking.builder()
                .userEmail(userEmail)
                .petId(req.getPetId())
                .providerId(req.getProviderId())
                .serviceTypeId(req.getServiceTypeId())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .status(BookingStatus.PENDING)
                .notes(req.getNotes())
                .createdAt(LocalDateTime.now())
                .build();

        Booking saved = repo.save(b);

        // include owner email for notification targeting
        var provider = catalog.getProvider(saved.getProviderId());
        publisher.publish(
                "booking.created",
                saved.getId().toString(),
                evt(saved, "BOOKING_CREATED", null, provider.ownerEmail())
        );

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> myBookings(String userEmail) {
        return repo.findByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BookingResponse getById(Long id, String userEmail) {
        Booking b = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        if (!b.getUserEmail().equalsIgnoreCase(userEmail)) {
            var p = catalog.getProvider(b.getProviderId());
            if (p.ownerEmail() == null || !p.ownerEmail().equalsIgnoreCase(userEmail)) {
                throw new SecurityException("Not allowed");
            }
        }
        return toResponse(b);
    }

    @Transactional
    public BookingResponse accept(Long id, String userEmail) {
        Booking b = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        if (b.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only PENDING bookings can be accepted");
        }

        var provider = catalog.getProvider(b.getProviderId());
        if (provider.ownerEmail() == null || !provider.ownerEmail().equalsIgnoreCase(userEmail)) {
            throw new SecurityException("Only provider owner can accept");
        }

        // exclude self and check only against CONFIRMED
        boolean clash = repo.overlapsExcludingSelf(
                b.getProviderId(),
                b.getId(),
                b.getStartTime(),
                b.getEndTime(),
                EnumSet.of(BookingStatus.CONFIRMED)
        );
        if (clash) throw new IllegalStateException("Time slot not available for provider");

        b.setStatus(BookingStatus.CONFIRMED);
        Booking saved = repo.save(b);

        publisher.publish(
                "booking.accepted",
                saved.getId().toString(),
                evt(saved, "BOOKING_ACCEPTED", null, provider.ownerEmail())
        );

        return toResponse(saved);
    }

    @Transactional
    public BookingResponse complete(Long id, String userEmail) {
        Booking b = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        if (b.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED bookings can be completed");
        }

        var provider = catalog.getProvider(b.getProviderId());
        if (provider.ownerEmail() == null || !provider.ownerEmail().equalsIgnoreCase(userEmail)) {
            throw new SecurityException("Only provider owner can complete");
        }

        b.setStatus(BookingStatus.COMPLETED);
        Booking saved = repo.save(b);

        publisher.publish(
                "booking.completed",
                saved.getId().toString(),
                evt(saved, "BOOKING_COMPLETED", null, provider.ownerEmail())
        );

        return toResponse(saved);
    }

    @Transactional
    public BookingResponse cancel(Long id, String userEmail) {
        Booking b = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        if (b.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Completed booking cannot be cancelled");
        }

        boolean allowed = b.getUserEmail().equalsIgnoreCase(userEmail);
        if (!allowed) {
            var provider = catalog.getProvider(b.getProviderId());
            allowed = provider.ownerEmail() != null && provider.ownerEmail().equalsIgnoreCase(userEmail);
            if (!allowed) throw new SecurityException("Not allowed to cancel this booking");
        }

        b.setStatus(BookingStatus.CANCELLED);
        Booking saved = repo.save(b);

        publisher.publish(
                "booking.cancelled",
                saved.getId().toString(),
                evt(saved, "BOOKING_CANCELLED", "Cancelled by " + userEmail, null)
        );

        return toResponse(saved);
    }
}