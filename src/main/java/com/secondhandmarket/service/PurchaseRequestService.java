package com.secondhandmarket.service;

import com.secondhandmarket.dto.purchaserequest.PurchaseRequestResponseDTO;
import com.secondhandmarket.enums.ProductStatus;
import com.secondhandmarket.enums.PurchaseRequestStatus;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.PurchaseRequest;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.ProductRepository;
import com.secondhandmarket.repository.PurchaseRequestRepository;
import com.secondhandmarket.repository.UserRepository;
import com.secondhandmarket.security.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseRequestService {

    PurchaseRequestRepository purchaseRequestRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    SecurityUtil securityUtil;

    public PurchaseRequestResponseDTO createPurchaseRequest(String productId, String message) {
        String buyerId = securityUtil.getCurrentUserId();

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Buyer not found", "user-e-01"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Product not found", "product-e-01"));

        // Kiểm tra trạng thái sản phẩm
        if (!product.getStatus().equals(ProductStatus.AVAILABLE)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Product is not available for purchase", "product-e-02");
        }

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setProduct(product);
        purchaseRequest.setBuyer(buyer);
        purchaseRequest.setMessage(message);
        purchaseRequest.setStatus(PurchaseRequestStatus.PENDING); // Trạng thái yêu cầu mua

        purchaseRequest = purchaseRequestRepository.save(purchaseRequest);

        return mapToDTO(purchaseRequest);
    }

    public List<PurchaseRequestResponseDTO> getBuyerRequests() {
        String buyerId = securityUtil.getCurrentUserId();

        List<PurchaseRequest> requests = purchaseRequestRepository.findByBuyerId(buyerId);

        return requests.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseRequestResponseDTO> getSellerRequests() {
        String sellerId = securityUtil.getCurrentUserId();

        List<PurchaseRequest> requests = purchaseRequestRepository.findByProduct_SellerId(sellerId);

        return requests.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseRequestResponseDTO> getPurchaseRequestsByProductId(String productId) {
        List<PurchaseRequest> requests = purchaseRequestRepository.findByProduct_Id(productId);

        return requests.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void rejectPurchaseRequest(String purchaseRequestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(purchaseRequestId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Purchase request not found", "purchaseRequest-e-01"));
        String sellerId = securityUtil.getCurrentUserId();
        if (!request.getProduct().getSeller().getId().equals(sellerId)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "No permit", "purchaseRequest-e-02");
        }
        request.setStatus(PurchaseRequestStatus.REJECTED); // Cập nhật trạng thái yêu cầu mua
        purchaseRequestRepository.save(request);
    }

    public void acceptPurchaseRequest(String purchaseRequestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(purchaseRequestId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Purchase request not found", "purchaseRequest-e-01"));
        String sellerId = securityUtil.getCurrentUserId();
        if (!request.getProduct().getSeller().getId().equals(sellerId)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "No permit", "purchaseRequest-e-02");
        }
        request.setStatus(PurchaseRequestStatus.ACCEPTED); // Cập nhật trạng thái yêu cầu mua
        purchaseRequestRepository.save(request);
    }

    public void deletePurchaseRequest(String purchaseRequestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(purchaseRequestId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Purchase request not found", "purchaseRequest-e-01"));
        String buyerId = securityUtil.getCurrentUserId();
        if (!request.getBuyer().getId().equals(buyerId)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "No permit", "purchaseRequest-e-03");
        }
        purchaseRequestRepository.delete(request);
    }

    private PurchaseRequestResponseDTO mapToDTO(PurchaseRequest request) {
        PurchaseRequestResponseDTO dto = new PurchaseRequestResponseDTO();
        dto.setId(request.getId());
        dto.setProductId(request.getProduct().getId());
        dto.setBuyerId(request.getBuyer().getId());
        dto.setMessage(request.getMessage());
        dto.setStatus(request.getStatus());
        return dto;
    }
}
