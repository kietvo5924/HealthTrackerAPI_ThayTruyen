package com.yourcompany.healthtracker.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = null;

            // LOGIC MỚI: Kiểm tra xem đường dẫn có phải là file trên hệ thống (Render) hay không
            try {
                // Thử đọc như một file bình thường (Dành cho Render: /etc/secrets/...)
                serviceAccount = new FileInputStream(firebaseConfigPath);
                System.out.println("Đang đọc Firebase config từ file hệ thống: " + firebaseConfigPath);
            } catch (IOException e) {
                // Nếu không tìm thấy file hệ thống, thử tìm trong Classpath (Dành cho Localhost)
                System.out.println("Không tìm thấy file hệ thống. Đang thử đọc từ Classpath...");
                serviceAccount = new ClassPathResource(firebaseConfigPath).getInputStream();
            }

            if (serviceAccount == null) {
                throw new RuntimeException("Không thể tìm thấy file cấu hình Firebase!");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK initialized successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Lưu ý: Không nên throw exception làm crash app nếu chỉ lỗi firebase, tùy nhu cầu
            System.err.println("Error initializing Firebase Admin SDK: " + e.getMessage());
        }
    }
}