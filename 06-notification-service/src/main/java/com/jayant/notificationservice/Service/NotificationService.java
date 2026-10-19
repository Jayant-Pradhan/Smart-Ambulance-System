package com.jayant.notificationservice.Service;

import com.jayant.notificationservice.DTO.NotificationRequestDTO;
import com.jayant.notificationservice.DTO.NotificationResponseDTO;
import com.jayant.notificationservice.Enum.NotificationStatus;

import java.util.List;

public interface NotificationService {

    NotificationResponseDTO sendNotification(NotificationRequestDTO requestDTO);

    NotificationResponseDTO getNotificationById(Long notificationId);

    List<NotificationResponseDTO> getAllNotifications();

    List<NotificationResponseDTO> getNotificationsByEmergencyId(Long emergencyId);

    List<NotificationResponseDTO> getNotificationsByStatus(NotificationStatus status);
}
