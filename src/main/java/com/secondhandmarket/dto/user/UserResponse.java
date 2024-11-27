package com.secondhandmarket.dto.user;

import com.secondhandmarket.model.Address;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    String name;
    String phone;
    String avatar;
    Address address;
}
