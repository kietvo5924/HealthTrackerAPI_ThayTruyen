package com.yourcompany.healthtracker.controllers;

import com.yourcompany.healthtracker.dtos.HealthDataLogRequest;
import com.yourcompany.healthtracker.models.HealthData;
import com.yourcompany.healthtracker.services.HealthDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/health-data")
@RequiredArgsConstructor
@Tag(name = "Health Data API", description = "APIs để ghi và đọc dữ liệu sức khỏe")
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
public class HealthDataController {

    private final HealthDataService healthDataService;

    @Operation(summary = "Ghi hoặc cập nhật dữ liệu sức khỏe",
            description = "Gửi các chỉ số để ghi lại. Nếu đã có bản ghi cho ngày đó, nó sẽ được cập nhật.")
    @PostMapping
    public ResponseEntity<HealthData> logHealthData(@Valid @RequestBody HealthDataLogRequest request) {
        HealthData savedData = healthDataService.logOrUpdateHealthData(request);
        return ResponseEntity.ok(savedData);
    }

    @Operation(summary = "Lấy dữ liệu sức khỏe theo ngày",
            description = "Lấy bản ghi sức khỏe của user đang đăng nhập cho một ngày cụ thể.")
    @GetMapping("/{date}")
    public ResponseEntity<HealthData> getHealthDataByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        HealthData data = healthDataService.getHealthDataForDate(date);
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Lấy dữ liệu sức khỏe theo khoảng ngày",
            description = "Lấy danh sách bản ghi cho biểu đồ (ví dụ: 7 ngày qua).")
    @GetMapping("/range")
    public ResponseEntity<List<HealthData>> getHealthDataByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<HealthData> dataList = healthDataService.getHealthDataForDateRange(startDate, endDate);
        return ResponseEntity.ok(dataList);
    }
}