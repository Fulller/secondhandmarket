package com.secondhandmarket.controller;

import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.dto.order.OrderResponse;
import com.secondhandmarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderRequestController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/seller/order")
    ResponseEntity<ApiResponse<List<OrderResponse>>> getYourOrderSeller() {
        ApiResponse<List<OrderResponse>> apiResponse = ApiResponse.<List<OrderResponse>>builder()
                .code("order-s-01")
                .message("get order seller success")
                .data(orderService.getYourOrderSeller())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/product/{id}/order")
    ResponseEntity<ApiResponse<List<OrderResponse>>> getYourOrderProduct(@PathVariable String id) {
        ApiResponse<List<OrderResponse>> apiResponse = ApiResponse.<List<OrderResponse>>builder()
                .code("order-s-02")
                .message("get order product success")
                .data(orderService.getYourOrderProduct(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("/buyer/order")
    ResponseEntity<ApiResponse<List<OrderResponse>>> getYourOrderBuyer() {
        ApiResponse<List<OrderResponse>> apiResponse = ApiResponse.<List<OrderResponse>>builder()
                .code("order-s-03")
                .message("get order buyer success")
                .data(orderService.getYourOrderBuyer())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/cancel/{id}/order")
    ResponseEntity<ApiResponse> cancelOrder(@PathVariable String id) {
        orderService.cancelOrder(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("order-s-04")
                .message("cancel order success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PutMapping("/complete/{id}/order")
    ResponseEntity<ApiResponse> completeOrder(@PathVariable String id) {
        orderService.completeOrder(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("order-s-04")
                .message("complete order success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
