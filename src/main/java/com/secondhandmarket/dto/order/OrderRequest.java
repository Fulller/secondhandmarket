package com.secondhandmarket.dto.order;

import com.secondhandmarket.model.PurchaseRequest;
import com.secondhandmarket.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    User buyer;
    User seller;
    String product_id;
    PurchaseRequest purchase_request;
}
