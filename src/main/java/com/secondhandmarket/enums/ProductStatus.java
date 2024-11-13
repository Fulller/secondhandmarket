package com.secondhandmarket.enums;

public enum ProductStatus {
    PENDING,    // Đang chờ xử lý, có thể do admin kiểm duyệt
    AVAILABLE,  // Sản phẩm có sẵn để mua
    SOLD,       // Sản phẩm đã được bán
    HIDDEN,  // Sản phẩm bị xóa hoặc ẩn khỏi hệ thống
    REJECTED, // Sản phẩm bị từ chối duyệt
    EXPIRED, // Sản phẩm đã hết hạn

}
