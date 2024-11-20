package com.secondhandmarket.dto.purchaserequest;

import com.secondhandmarket.enums.PurchaseRequestStatus; // Import enum
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseRequestResponseDTO {
    String id;
    String productId;
    String buyerId;
    String message;
    String productName;
    Double productPrice;
    String productDescription;
    String buyerName;
    String buyerEmail;
    String thumbnail;
    String postedAt;  // Thay kiểu thành String
    String expiredAt; // Thay kiểu thành String
    PurchaseRequestStatus status;
}