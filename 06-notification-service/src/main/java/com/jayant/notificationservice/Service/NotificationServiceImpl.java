package com.jayant.notificationservice.Service;

import com.jayant.notificationservice.DTO.NotificationRequestDTO;
import com.jayant.notificationservice.DTO.NotificationResponseDTO;
import com.jayant.notificationservice.Entity.Notification;
import com.jayant.notificationservice.Enum.NotificationStatus;
import com.jayant.notificationservice.Enum.NotificationType;
import com.jayant.notificationservice.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationResponseDTO sendNotification(NotificationRequestDTO requestDTO) {


        NotificationResponseDTO responseDTO = new NotificationResponseDTO();

        Notification notification = new Notification();
        notification.setEmergencyId(requestDTO.getEmergencyId());
        notification.setMobileNumber(requestDTO.getMobileNumber());
        notification.setMessage(requestDTO.getMessage());
        notification.setNotificationStatus(NotificationStatus.SENT);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSentAt(LocalDateTime.now());
        notification.setNotificationType(NotificationType.AMBULANCE_ASSIGNED);

        Notification savedNotification = notificationRepository.save(notification);

        responseDTO.setId(savedNotification.getId());
        responseDTO.setEmergencyId(savedNotification.getEmergencyId());
        responseDTO.setMobileNumber(savedNotification.getMobileNumber());
        responseDTO.setMessage(savedNotification.getMessage());
        responseDTO.setNotificationType(savedNotification.getNotificationType());
        responseDTO.setNotificationStatus(savedNotification.getNotificationStatus());
        responseDTO.setCreatedAt(savedNotification.getCreatedAt());
        responseDTO.setSentAt(savedNotification.getSentAt());

        return responseDTO;

    }






    @Override
    public NotificationResponseDTO getNotificationById(Long notificationId) {
        Optional<Notification> isAvail = notificationRepository.findById(notificationId);

        NotificationResponseDTO responseDTO = new NotificationResponseDTO();

        if (isAvail.isEmpty()) {
            throw new RuntimeException(
                    "Notification not found with id: " + notificationId
            );
        }

        Notification notification = isAvail.get();



        responseDTO.setId(notification.getId());
        responseDTO.setEmergencyId(notification.getEmergencyId());
        responseDTO.setMobileNumber(notification.getMobileNumber());
        responseDTO.setMessage(notification.getMessage());
        responseDTO.setNotificationType(notification.getNotificationType());
        responseDTO.setNotificationStatus(notification.getNotificationStatus());
        responseDTO.setCreatedAt(notification.getCreatedAt());
        responseDTO.setSentAt(notification.getSentAt());

        return responseDTO;
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();

        return notifications.stream()
                .map(notification -> {
                    NotificationResponseDTO responseDTO = new NotificationResponseDTO();
                    responseDTO.setId(notification.getId());
                    responseDTO.setEmergencyId(notification.getEmergencyId());
                    responseDTO.setMobileNumber(notification.getMobileNumber());
                    responseDTO.setMessage(notification.getMessage());
                    responseDTO.setNotificationType(notification.getNotificationType());
                    responseDTO.setNotificationStatus(notification.getNotificationStatus());
                    responseDTO.setCreatedAt(notification.getCreatedAt());
                    responseDTO.setSentAt(notification.getSentAt());

                    return responseDTO;
                })
                .toList();
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByEmergencyId(Long emergencyId) {
        List<Notification> notifications =
                notificationRepository.findByEmergencyId(emergencyId);

        return notifications.stream()
                .map(notification -> {
                    NotificationResponseDTO responseDTO =
                            new NotificationResponseDTO();

                    responseDTO.setId(notification.getId());
                    responseDTO.setEmergencyId(notification.getEmergencyId());
                    responseDTO.setMobileNumber(notification.getMobileNumber());
                    responseDTO.setMessage(notification.getMessage());
                    responseDTO.setNotificationType(notification.getNotificationType());
                    responseDTO.setNotificationStatus(notification.getNotificationStatus());
                    responseDTO.setCreatedAt(notification.getCreatedAt());
                    responseDTO.setSentAt(notification.getSentAt());

                    return responseDTO;
                })
                .toList();

    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByStatus(NotificationStatus status) {

        List<Notification> notifications =
                notificationRepository.findByNotificationStatus(status);

        return notifications.stream()
                .map(notification -> {
                    NotificationResponseDTO responseDTO =
                            new NotificationResponseDTO();

                    responseDTO.setId(notification.getId());
                    responseDTO.setEmergencyId(notification.getEmergencyId());
                    responseDTO.setMobileNumber(notification.getMobileNumber());
                    responseDTO.setMessage(notification.getMessage());
                    responseDTO.setNotificationType(notification.getNotificationType());
                    responseDTO.setNotificationStatus(notification.getNotificationStatus());
                    responseDTO.setCreatedAt(notification.getCreatedAt());
                    responseDTO.setSentAt(notification.getSentAt());

                    return responseDTO;
                })
                .toList();
    }
}
