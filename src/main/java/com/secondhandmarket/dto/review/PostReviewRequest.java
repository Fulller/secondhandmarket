package com.secondhandmarket.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PostReviewRequest {
    @NotNull(message = "Rating must not be null")
            @Min(value = 1)
            @Max(value = 5)
    Integer rating;
    @NotBlank(message = "Vui lòng nhập bình luận")
    String comment;
    String image;
}
