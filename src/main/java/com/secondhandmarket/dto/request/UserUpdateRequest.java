package com.secondhandmarket.dto.request;

import com.secondhandmarket.model.Address;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserUpdateRequest {
    String name;
    String phone;
    String avatar;
    Address address;
}
