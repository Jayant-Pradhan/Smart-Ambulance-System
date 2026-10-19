package com.jayant.notificationservice.Controller;

import com.jayant.notificationservice.DTO.NotificationRequestDTO;
import com.jayant.notificationservice.DTO.NotificationResponseDTO;
import com.jayant.notificationservice.Enum.NotificationStatus;
import com.jayant.notificationservice.Service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    // Send notification
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> sendNotification(
            @Valid @RequestBody NotificationRequestDTO requestDTO) {

        NotificationResponseDTO responseDTO = notificationService.sendNotification(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // Get notification by ID
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(
            @PathVariable Long notificationId) {

        return ResponseEntity.ok(notificationService.getNotificationById(notificationId));
    }

    // Get all notifications
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {

        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    // Get notifications by emergency ID
    @GetMapping("/emergency/{emergencyId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByEmergencyId(
            @PathVariable Long emergencyId) {

        return ResponseEntity.ok(notificationService.getNotificationsByEmergencyId(emergencyId));
    }

    // Get notifications by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByStatus(
            @PathVariable NotificationStatus status) {

        return ResponseEntity.ok(notificationService.getNotificationsByStatus(status));
    }
}
