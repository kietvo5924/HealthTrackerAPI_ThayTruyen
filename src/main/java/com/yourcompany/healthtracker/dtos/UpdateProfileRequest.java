package com.yourcompany.healthtracker.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "Đối tượng yêu cầu để cập nhật thông tin cá nhân")
public class UpdateProfileRequest {

    @Schema(description = "Họ và tên đầy đủ", example = "Nguyễn Văn B")
    @Size(min = 1, message = "Họ tên không được để trống")
    private String fullName;

    @Schema(description = "Số điện thoại", example = "0987654322")
    @Size(min = 10, max = 10, message = "Số điện thoại phải có đúng 10 chữ số")
    private String phoneNumber;

    @Schema(description = "Ngày sinh", example = "2000-01-15")
    @Past(message = "Ngày sinh phải là một ngày trong quá khứ")
    private LocalDate dateOfBirth;

    @Schema(description = "Địa chỉ", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;

    @Schema(description = "Tiền sử bệnh án", example = "Từng bị viêm phổi lúc nhỏ")
    private String medicalHistory;

    @Schema(description = "Dị ứng", example = "Dị ứng với phấn hoa")
    private String allergies;
}