package com.secondhandmarket.dto.user;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserSellerInProductDTO {
    String id;
    String name;
    String avatar;
    Double rating;
}