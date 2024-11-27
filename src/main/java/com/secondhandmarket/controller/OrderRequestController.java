package com.secondhandmarket.controller;

import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.dto.order.OrderResponse;
import com.secondhandmarket.enums.OrderStatus;
import com.secondhandmarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRequestController {
    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/seller")
    ResponseEntity<ApiResponse<List<OrderResponse>>> getYourOrderSeller(
            @RequestParam(value = "status", required = false) OrderStatus status
    ) {
        ApiResponse<List<OrderResponse>> apiResponse = ApiResponse.<List<OrderResponse>>builder()
                .code("order-s-01")
                .message("get order seller success")
                .data(orderService.getYourOrderSeller(status))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/product/{id}")
    ResponseEntity<ApiResponse<List<OrderResponse>>> getYourOrderProduct(@PathVariable String id) {
        ApiResponse<List<OrderResponse>> apiResponse = ApiResponse.<List<OrderResponse>>builder()
                .code("order-s-02")
                .message("get order product success")
                .data(orderService.getYourOrderProduct(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/buyer")
    ResponseEntity<ApiResponse<List<OrderResponse>>> getYourOrderBuyer(
            @RequestParam(value = "status", required = false) OrderStatus status
    ) {
        ApiResponse<List<OrderResponse>> apiResponse = ApiResponse.<List<OrderResponse>>builder()
                .code("order-s-03")
                .message("get order buyer success")
                .data(orderService.getYourOrderBuyer(status))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cancel/{id}")
    ResponseEntity<ApiResponse> cancelOrder(@PathVariable String id) {
        orderService.cancelOrder(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("order-s-04")
                .message("cancel order success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/complete/{id}")
    ResponseEntity<ApiResponse> completeOrder(@PathVariable String id) {
        orderService.completeOrder(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("order-s-04")
                .message("complete order success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
