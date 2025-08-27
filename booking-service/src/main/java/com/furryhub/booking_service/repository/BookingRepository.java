package com.furryhub.booking_service.repository;


import com.furryhub.booking_service.entity.Booking;
import com.furryhub.booking_service.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserEmailOrderByCreatedAtDesc(String userEmail);

    @Query("""
                SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
                FROM Booking b
                WHERE b.providerId = :providerId
                  AND b.status IN :statuses
                  AND b.startTime < :endTime
                  AND b.endTime   > :startTime
            """)
    boolean existsOverlap(@Param("providerId") Integer providerId,
                          @Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime,
                          @Param("statuses") Collection<BookingStatus> statuses);

    // For ACCEPT: exclude the same booking; check only against CONFIRMED
    @Query("""
                SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
                FROM Booking b
                WHERE b.providerId = :providerId
                  AND b.id <> :bookingId
                  AND b.status IN :statuses
                  AND b.startTime < :endTime
                  AND b.endTime   > :startTime
            """)
    boolean overlapsExcludingSelf(@Param("providerId") Integer providerId,
                                  @Param("bookingId") Long bookingId,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime,
                                  @Param("statuses") Collection<BookingStatus> statuses);
}
