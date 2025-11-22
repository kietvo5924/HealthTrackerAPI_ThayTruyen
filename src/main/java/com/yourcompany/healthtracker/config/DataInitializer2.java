package com.yourcompany.healthtracker.config;

import com.yourcompany.healthtracker.models.Achievement;
import com.yourcompany.healthtracker.models.Achievement.AchievementType;
import com.yourcompany.healthtracker.repositories.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer2 implements CommandLineRunner {

    private final AchievementRepository achievementRepository;

    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra nếu chưa có thành tựu nào thì mới thêm vào (tránh trùng lặp khi restart)
        if (achievementRepository.count() == 0) {
            List<Achievement> achievements = List.of(
                    // --- NHÓM ĐI BỘ (STEPS) ---
                    Achievement.builder()
                            .code("STEPS_5K").name("Khởi động nhẹ").description("Đi được 5.000 bước trong một ngày")
                            .targetValue(5000).type(AchievementType.STEPS).iconUrl("assets/icons/steps_bronze.png").build(),
                    Achievement.builder()
                            .code("STEPS_10K").name("Vận động viên").description("Đi được 10.000 bước trong một ngày")
                            .targetValue(10000).type(AchievementType.STEPS).iconUrl("assets/icons/steps_silver.png").build(),
                    Achievement.builder()
                            .code("STEPS_20K").name("Thánh đi bộ").description("Đi được 20.000 bước trong một ngày")
                            .targetValue(20000).type(AchievementType.STEPS).iconUrl("assets/icons/steps_gold.png").build(),

                    // --- NHÓM TẬP LUYỆN (WORKOUT_COUNT) ---
                    Achievement.builder()
                            .code("WORKOUT_1").name("Bước đầu tiên").description("Hoàn thành bài tập đầu tiên của bạn")
                            .targetValue(1).type(AchievementType.WORKOUT_COUNT).iconUrl("assets/icons/workout_1.png").build(),
                    Achievement.builder()
                            .code("WORKOUT_10").name("Quyết tâm cao").description("Hoàn thành 10 bài tập bất kỳ")
                            .targetValue(10).type(AchievementType.WORKOUT_COUNT).iconUrl("assets/icons/workout_10.png").build(),
                    Achievement.builder()
                            .code("WORKOUT_50").name("Gymer chính hiệu").description("Hoàn thành 50 bài tập")
                            .targetValue(50).type(AchievementType.WORKOUT_COUNT).iconUrl("assets/icons/workout_50.png").build(),

                    // --- NHÓM CALO (CALORIES_BURNED - Trong 1 buổi) ---
                    Achievement.builder()
                            .code("CAL_300").name("Đốt cháy mỡ thừa").description("Đốt hơn 300 kcal trong một buổi tập")
                            .targetValue(300).type(AchievementType.CALORIES_BURNED).iconUrl("assets/icons/fire_small.png").build(),
                    Achievement.builder()
                            .code("CAL_700").name("Lò luyện kim").description("Đốt hơn 700 kcal trong một buổi tập")
                            .targetValue(700).type(AchievementType.CALORIES_BURNED).iconUrl("assets/icons/fire_big.png").build()
            );

            achievementRepository.saveAll(achievements);
            System.out.println(">>> Đã khởi tạo dữ liệu Thành tựu (Achievements) thành công!");
        }
    }
}