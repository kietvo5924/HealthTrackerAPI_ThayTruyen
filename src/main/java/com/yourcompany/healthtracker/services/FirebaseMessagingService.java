package com.yourcompany.healthtracker.services;

import com.google.firebase.messaging.*;
import com.yourcompany.healthtracker.models.Notification;
import com.yourcompany.healthtracker.models.Notification.NotificationType;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.repositories.NotificationRepository;
import com.yourcompany.healthtracker.repositories.UserRepository; // Cần để tìm user từ token nếu cần, hoặc truyền User vào hàm
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessagingService {

    private final NotificationRepository notificationRepository;

    public void sendNotification(String token, String title, String body) {
        sendPushOnly(token, title, body);
    }

    // Gửi Push + Lưu DB
    @Transactional
    public void sendNotificationToUser(User user, String title, String body, NotificationType type) {
        // 1. Lưu vào Database
        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .body(body)
                .type(type)
                .build();
        notificationRepository.save(notification);

        // 2. Gửi Push qua Firebase (nếu user có token)
        if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
            sendPushOnly(user.getFcmToken(), title, body);
        }
    }

    private void sendPushOnly(String token, String title, String body) {
        try {
            com.google.firebase.messaging.Notification notification = com.google.firebase.messaging.Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Lỗi gửi FCM: {}", e.getMessage());
        }
    }
}