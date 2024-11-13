package com.secondhandmarket.controller;

import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.dto.purchaserequest.PurchaseRequestResponseDTO;
import com.secondhandmarket.service.PurchaseRequestService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PurchaseRequestController {

    PurchaseRequestService purchaseRequestService;

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<PurchaseRequestResponseDTO>> createPurchaseRequest(
            @PathVariable String productId, @RequestBody @Valid String message) {
        PurchaseRequestResponseDTO responseDTO = purchaseRequestService.createPurchaseRequest(productId, message);
        ApiResponse<PurchaseRequestResponseDTO> apiResponse = ApiResponse.<PurchaseRequestResponseDTO>builder()
                .code("purchase-request-s-01")
                .message("Tạo yêu cầu mua thành công")
                .data(responseDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/buyer")
    public ResponseEntity<ApiResponse<List<PurchaseRequestResponseDTO>>> getBuyerRequests() {
        List<PurchaseRequestResponseDTO> buyerRequests = purchaseRequestService.getBuyerRequests();
        ApiResponse<List<PurchaseRequestResponseDTO>> apiResponse = ApiResponse.<List<PurchaseRequestResponseDTO>>builder()
                .code("purchase-request-s-02")
                .message("Lấy danh sách yêu cầu mua của người mua thành công")
                .data(buyerRequests)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/seller")
    public ResponseEntity<ApiResponse<List<PurchaseRequestResponseDTO>>> getSellerRequests() {
        List<PurchaseRequestResponseDTO> sellerRequests = purchaseRequestService.getSellerRequests();
        ApiResponse<List<PurchaseRequestResponseDTO>> apiResponse = ApiResponse.<List<PurchaseRequestResponseDTO>>builder()
                .code("purchase-request-s-03")
                .message("Lấy danh sách yêu cầu mua của người bán thành công")
                .data(sellerRequests)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<PurchaseRequestResponseDTO>>> getPurchaseRequestsByProductId(@PathVariable String productId) {
        List<PurchaseRequestResponseDTO> purchaseRequests = purchaseRequestService.getPurchaseRequestsByProductId(productId);
        ApiResponse<List<PurchaseRequestResponseDTO>> apiResponse = ApiResponse.<List<PurchaseRequestResponseDTO>>builder()
                .code("purchase-request-s-06")
                .message("Lấy danh sách yêu cầu mua theo ID sản phẩm thành công")
                .data(purchaseRequests)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    @PutMapping("/{purchaseRequestId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectPurchaseRequest(@PathVariable String purchaseRequestId) {
        purchaseRequestService.rejectPurchaseRequest(purchaseRequestId);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("purchase-request-s-04")
                .message("Yêu cầu mua đã bị từ chối")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{purchaseRequestId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptPurchaseRequest(@PathVariable String purchaseRequestId) {
        purchaseRequestService.acceptPurchaseRequest(purchaseRequestId);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("purchase-request-s-05")
                .message("Yêu cầu mua đã được chấp nhận")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(apiResponse);
    }

    @DeleteMapping("/{purchaseRequestId}")
    public ResponseEntity<ApiResponse<Void>> deletePurchaseRequest(@PathVariable String purchaseRequestId) {
        purchaseRequestService.deletePurchaseRequest(purchaseRequestId);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("purchase-request-s-07")
                .message("Yêu cầu mua đã được xóa thành công")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
    }
}
