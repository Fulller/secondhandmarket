package com.secondhandmarket.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PostReviewRequest {
    @NotBlank(message = "Vui lòng đánh giá")
            @Min(value = 1)
            @Max(value = 5)
    Integer rating;
    @NotBlank(message = "Vui lòng nhập bình luận")
    String comment;
    String image;
}
