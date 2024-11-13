package com.secondhandmarket.dto.review;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReviewUpdateRequest {
    Integer rating;
    String comment;
    String image;
}
