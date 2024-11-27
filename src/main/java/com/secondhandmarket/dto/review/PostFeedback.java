package com.secondhandmarket.dto.review;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PostFeedback {
    @NotBlank(message = "Vui lòng nhập phản hồi")
    String feedback;
}
