package com.secondhandmarket.dto.report;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse {
    String reason;
    LocalDateTime reportedAt;
    UserDTO accused;
    UserDTO defendant;

    @Data
    public static class UserDTO {
        String id;
        String name;
        String phone;
        String email;
        String avatar;
        Double rating;
    }
}
