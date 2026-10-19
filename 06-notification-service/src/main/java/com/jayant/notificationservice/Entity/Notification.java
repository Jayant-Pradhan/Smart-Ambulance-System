package com.jayant.notificationservice.Entity;

import com.jayant.notificationservice.Enum.NotificationStatus;
import com.jayant.notificationservice.Enum.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long emergencyId;

    @Column(nullable = false, length = 10)
    private String mobileNumber;

    @Column(nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus notificationStatus;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (notificationStatus == null) {
            notificationStatus = NotificationStatus.PENDING;
        }
    }
}