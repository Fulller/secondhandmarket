package com.secondhandmarket.dto.report;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportRequest {
    @NotBlank(message = "Vui lòng nhập cáo buộc")
    @Min(value = 10, message = "Cáo buộc ít nhất 10 ký tự")
    String reason;
    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Số điện thoại không chính xác")
    String phone;

    @NotNull(message = "Thêm id bị cáo")
    String defendant_id;
}
