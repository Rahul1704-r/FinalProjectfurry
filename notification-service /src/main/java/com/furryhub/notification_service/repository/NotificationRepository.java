package com.furryhub.notification_service.repository;


import com.furryhub.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
      SELECT n FROM Notification n
      WHERE n.userEmail = :email
      ORDER BY n.createdAt DESC
    """)
    List<Notification> findAllForUser(@Param("email") String email);
}
