package com.yourcompany.healthtracker.services;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    // Gửi thông báo đến 1 token cụ thể
    public void sendNotification(String token, String title, String body) {
        if (token == null || token.isEmpty()) {
            return; // Không có token thì bỏ qua
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token) // FCM token của người nhận
                .setNotification(notification)
                // (Tùy chọn) Gửi thêm data (để xử lý trong app)
                // .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                // .putData("workoutId", workoutId.toString())
                .build();

        try {
            // Gửi tin nhắn
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            // Xử lý lỗi (ví dụ: token hết hạn)
            e.printStackTrace();
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }
}