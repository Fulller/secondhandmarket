package com.secondhandmarket.enums;

public enum ProductStatus {
    AVAILABLE,  // Sản phẩm có sẵn để mua
    SOLD,       // Sản phẩm đã được bán
    PENDING,    // Đang chờ xử lý, có thể do admin kiểm duyệt
    REMOVED     // Sản phẩm bị xóa hoặc ẩn khỏi hệ thống
}
