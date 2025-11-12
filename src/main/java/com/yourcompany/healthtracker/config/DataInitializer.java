package com.yourcompany.healthtracker.config;

import com.yourcompany.healthtracker.models.Food;
import com.yourcompany.healthtracker.repositories.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FoodRepository foodRepository;

    @Override
    public void run(String... args) throws Exception {
        // Chỉ thêm dữ liệu nếu bảng 'foods' đang trống
        if (foodRepository.count() > 0) {
            System.out.println("Food data already exists. Skipping initialization.");
            return;
        }

        System.out.println("Initializing 50 common food items...");

        // 20 món đầu tiên
        Food food1 = Food.builder().name("Cơm trắng").unit("chén").calories(204.0).proteinGrams(4.3).carbsGrams(44.5).fatGrams(0.4).addedByUser(null).build();
        Food food2 = Food.builder().name("Ức gà (nướng/luộc)").unit("100g").calories(165.0).proteinGrams(31.0).carbsGrams(0.0).fatGrams(3.6).addedByUser(null).build();
        Food food3 = Food.builder().name("Trứng ốp la").unit("quả").calories(90.0).proteinGrams(6.0).carbsGrams(0.6).fatGrams(7.0).addedByUser(null).build();
        Food food4 = Food.builder().name("Cá hồi (áp chảo)").unit("100g").calories(208.0).proteinGrams(20.0).carbsGrams(0.0).fatGrams(13.0).addedByUser(null).build();
        Food food5 = Food.builder().name("Phở bò (tái)").unit("tô").calories(450.0).proteinGrams(25.0).carbsGrams(50.0).fatGrams(18.0).addedByUser(null).build();
        Food food6 = Food.builder().name("Bánh mì thịt").unit("ổ").calories(400.0).proteinGrams(18.0).carbsGrams(50.0).fatGrams(15.0).addedByUser(null).build();
        Food food7 = Food.builder().name("Chuối").unit("quả").calories(105.0).proteinGrams(1.3).carbsGrams(27.0).fatGrams(0.4).addedByUser(null).build();
        Food food8 = Food.builder().name("Táo").unit("quả").calories(95.0).proteinGrams(0.5).carbsGrams(25.0).fatGrams(0.3).addedByUser(null).build();
        Food food9 = Food.builder().name("Sữa tươi (không đường)").unit("100ml").calories(60.0).proteinGrams(3.4).carbsGrams(4.8).fatGrams(3.3).addedByUser(null).build();
        Food food10 = Food.builder().name("Bông cải xanh (luộc)").unit("100g").calories(55.0).proteinGrams(3.7).carbsGrams(11.2).fatGrams(0.6).addedByUser(null).build();
        Food food11 = Food.builder().name("Cơm sườn bì chả").unit("dĩa").calories(600.0).proteinGrams(30.0).carbsGrams(70.0).fatGrams(22.0).addedByUser(null).build();
        Food food12 = Food.builder().name("Hủ tiếu nam vang").unit("tô").calories(410.0).proteinGrams(20.0).carbsGrams(45.0).fatGrams(15.0).addedByUser(null).build();
        Food food13 = Food.builder().name("Sữa chua (không đường)").unit("hũ 100g").calories(59.0).proteinGrams(10.0).carbsGrams(3.6).fatGrams(0.4).addedByUser(null).build();
        Food food14 = Food.builder().name("Đậu hũ (chiên)").unit("100g").calories(120.0).proteinGrams(8.0).carbsGrams(3.0).fatGrams(9.0).addedByUser(null).build();
        Food food15 = Food.builder().name("Rau muống xào tỏi").unit("dĩa").calories(150.0).proteinGrams(5.0).carbsGrams(10.0).fatGrams(10.0).addedByUser(null).build();
        Food food16 = Food.builder().name("Bò bít tết (beefsteak)").unit("phần").calories(550.0).proteinGrams(45.0).carbsGrams(30.0).fatGrams(28.0).addedByUser(null).build();
        Food food17 = Food.builder().name("Khoai lang (luộc)").unit("100g").calories(86.0).proteinGrams(1.6).carbsGrams(20.0).fatGrams(0.1).addedByUser(null).build();
        Food food18 = Food.builder().name("Hạt điều (rang)").unit("100g").calories(553.0).proteinGrams(18.0).carbsGrams(30.0).fatGrams(44.0).addedByUser(null).build();
        Food food19 = Food.builder().name("Bánh bao (thịt trứng)").unit("cái").calories(320.0).proteinGrams(12.0).carbsGrams(45.0).fatGrams(10.0).addedByUser(null).build();
        Food food20 = Food.builder().name("Gỏi cuốn (tôm thịt)").unit("cái").calories(60.0).proteinGrams(4.0).carbsGrams(7.0).fatGrams(2.0).addedByUser(null).build();

        // 30 món ăn thêm
        Food food21 = Food.builder().name("Bún bò Huế").unit("tô").calories(550.0).proteinGrams(28.0).carbsGrams(55.0).fatGrams(20.0).addedByUser(null).build();
        Food food22 = Food.builder().name("Bún chả Hà Nội").unit("phần").calories(500.0).proteinGrams(22.0).carbsGrams(60.0).fatGrams(18.0).addedByUser(null).build();
        Food food23 = Food.builder().name("Bánh xèo").unit("cái").calories(350.0).proteinGrams(10.0).carbsGrams(40.0).fatGrams(15.0).addedByUser(null).build();
        Food food24 = Food.builder().name("Bánh cuốn (chả lụa)").unit("dĩa").calories(380.0).proteinGrams(14.0).carbsGrams(50.0).fatGrams(12.0).addedByUser(null).build();
        Food food25 = Food.builder().name("Xôi mặn").unit("gói").calories(450.0).proteinGrams(15.0).carbsGrams(65.0).fatGrams(15.0).addedByUser(null).build();
        Food food26 = Food.builder().name("Cơm gà xối mỡ").unit("dĩa").calories(650.0).proteinGrams(30.0).carbsGrams(75.0).fatGrams(25.0).addedByUser(null).build();
        Food food27 = Food.builder().name("Cơm chiên Dương Châu").unit("dĩa").calories(520.0).proteinGrams(18.0).carbsGrams(70.0).fatGrams(18.0).addedByUser(null).build();
        Food food28 = Food.builder().name("Thịt kho trứng (1 trứng)").unit("phần").calories(350.0).proteinGrams(20.0).carbsGrams(10.0).fatGrams(25.0).addedByUser(null).build();
        Food food29 = Food.builder().name("Canh chua cá lóc").unit("chén").calories(120.0).proteinGrams(10.0).carbsGrams(12.0).fatGrams(3.0).addedByUser(null).build();
        Food food30 = Food.builder().name("Cá kho tộ").unit("phần").calories(250.0).proteinGrams(22.0).carbsGrams(8.0).fatGrams(15.0).addedByUser(null).build();
        Food food31 = Food.builder().name("Đùi gà chiên").unit("cái 150g").calories(400.0).proteinGrams(28.0).carbsGrams(10.0).fatGrams(28.0).addedByUser(null).build();
        Food food32 = Food.builder().name("Tôm (luộc/hấp)").unit("100g").calories(99.0).proteinGrams(24.0).carbsGrams(0.2).fatGrams(0.3).addedByUser(null).build();
        Food food33 = Food.builder().name("Thịt ba rọi (luộc)").unit("100g").calories(290.0).proteinGrams(12.0).carbsGrams(0.0).fatGrams(27.0).addedByUser(null).build();
        Food food34 = Food.builder().name("Cải thìa xào dầu hào").unit("dĩa").calories(130.0).proteinGrams(4.0).carbsGrams(10.0).fatGrams(8.0).addedByUser(null).build();
        Food food35 = Food.builder().name("Rau luộc (thập cẩm)").unit("dĩa 150g").calories(60.0).proteinGrams(3.0).carbsGrams(10.0).fatGrams(0.5).addedByUser(null).build();
        Food food36 = Food.builder().name("Canh bí đỏ (nấu tôm)").unit("chén").calories(90.0).proteinGrams(7.0).carbsGrams(10.0).fatGrams(2.0).addedByUser(null).build();
        Food food37 = Food.builder().name("Dưa hấu").unit("100g").calories(30.0).proteinGrams(0.6).carbsGrams(8.0).fatGrams(0.2).addedByUser(null).build();
        Food food38 = Food.builder().name("Thanh long").unit("100g").calories(60.0).proteinGrams(1.2).carbsGrams(13.0).fatGrams(0.6).addedByUser(null).build();
        Food food39 = Food.builder().name("Cam").unit("quả").calories(62.0).proteinGrams(1.2).carbsGrams(15.0).fatGrams(0.2).addedByUser(null).build();
        Food food40 = Food.builder().name("Đu đủ").unit("100g").calories(43.0).proteinGrams(0.5).carbsGrams(11.0).fatGrams(0.3).addedByUser(null).build();
        Food food41 = Food.builder().name("Nước cam vắt").unit("ly 200ml").calories(110.0).proteinGrams(1.5).carbsGrams(26.0).fatGrams(0.5).addedByUser(null).build();
        Food food42 = Food.builder().name("Bột chiên").unit("dĩa").calories(420.0).proteinGrams(8.0).carbsGrams(55.0).fatGrams(18.0).addedByUser(null).build();
        Food food43 = Food.builder().name("Bánh flan").unit("cái").calories(150.0).proteinGrams(6.0).carbsGrams(20.0).fatGrams(5.0).addedByUser(null).build();
        Food food44 = Food.builder().name("Chè bưởi").unit("ly").calories(380.0).proteinGrams(4.0).carbsGrams(75.0).fatGrams(8.0).addedByUser(null).build();
        Food food45 = Food.builder().name("Trà sữa trân châu (size M)").unit("ly").calories(350.0).proteinGrams(2.0).carbsGrams(50.0).fatGrams(15.0).addedByUser(null).build();
        Food food46 = Food.builder().name("Hạt hạnh nhân").unit("100g").calories(579.0).proteinGrams(21.0).carbsGrams(22.0).fatGrams(50.0).addedByUser(null).build();
        Food food47 = Food.builder().name("Hạt óc chó").unit("100g").calories(654.0).proteinGrams(15.0).carbsGrams(14.0).fatGrams(65.0).addedByUser(null).build();
        Food food48 = Food.builder().name("Cá điêu hồng (chiên)").unit("100g").calories(180.0).proteinGrams(22.0).carbsGrams(0.0).fatGrams(10.0).addedByUser(null).build();
        Food food49 = Food.builder().name("Miến gà").unit("tô").calories(380.0).proteinGrams(20.0).carbsGrams(40.0).fatGrams(15.0).addedByUser(null).build();
        Food food50 = Food.builder().name("Salad trộn (dầu giấm)").unit("dĩa").calories(150.0).proteinGrams(4.0).carbsGrams(15.0).fatGrams(8.0).addedByUser(null).build();

        // Lưu tất cả 50 món ăn vào CSDL
        foodRepository.saveAll(List.of(
                food1, food2, food3, food4, food5, food6, food7, food8, food9, food10,
                food11, food12, food13, food14, food15, food16, food17, food18, food19, food20,
                food21, food22, food23, food24, food25, food26, food27, food28, food29, food30,
                food31, food32, food33, food34, food35, food36, food37, food38, food39, food40,
                food41, food42, food43, food44, food45, food46, food47, food48, food49, food50
        ));

        System.out.println("Finished initializing 50 food items.");
    }
}