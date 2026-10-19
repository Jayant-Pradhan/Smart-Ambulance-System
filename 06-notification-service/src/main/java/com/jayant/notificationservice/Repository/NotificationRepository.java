package com.jayant.notificationservice.Repository;

import com.jayant.notificationservice.Entity.Notification;
import com.jayant.notificationservice.Enum.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {


    List<Notification> findByEmergencyId(Long emergencyId);

    List<Notification> findByNotificationStatus(NotificationStatus status);

}
