package com.secondhandmarket.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AddressCreationRequest {
    String id;
    String ward;
    String district;
    String province;
    String detail;
}
