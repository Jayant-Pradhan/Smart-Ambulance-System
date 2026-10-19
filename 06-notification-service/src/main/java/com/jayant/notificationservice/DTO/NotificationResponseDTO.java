package com.jayant.notificationservice.DTO;

import com.jayant.notificationservice.Enum.NotificationStatus;
import com.jayant.notificationservice.Enum.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long id;

    private Long emergencyId;

    private String mobileNumber;

    private String message;

    private NotificationType notificationType;

    private NotificationStatus notificationStatus;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;
}