package com.secondhandmarket.service;

import com.secondhandmarket.dto.order.OrderRequest;
import com.secondhandmarket.dto.order.OrderResponse;
import com.secondhandmarket.enums.*;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.*;
import com.secondhandmarket.repository.OrderRepository;
import com.secondhandmarket.repository.ProductRepository;
import com.secondhandmarket.repository.ReviewRepository;
import com.secondhandmarket.repository.UserRepository;
import com.secondhandmarket.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private ReviewRepository reviewRepository;

    //accept order
    public void createOrder(OrderRequest orderRequest) {
        Product product = productRepository.findById(orderRequest.getProduct_id())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Product not found"));
        Order order = Order.builder()
                .buyer(orderRequest.getBuyer())
                .seller(orderRequest.getSeller())
                .product(product)
                .purchaseRequest(orderRequest.getPurchase_request())
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .status(OrderStatus.ACCEPTED)
                .build();
        orderRepository.save(order);
    }

    //lấy tất cả order của seller
    public List<OrderResponse> getYourOrderSeller(OrderStatus status) {
        User seller = securityUtil.getCurrentUser();
        List<Order> orders;
        if(status == null){
            orders = orderRepository.findBySeller(seller);
        }else {
            orders = orderRepository.findBySellerAndStatus(seller, status);
        }
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setBuyer(order.getBuyer());
            orderResponse.setSeller(order.getSeller());
            orderResponse.setProduct(order.getProduct());
            orderResponse.setPurchaseRequest(order.getPurchaseRequest());
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }
    //lấy tất cả order của product seller
    public List<OrderResponse> getYourOrderProduct(String id) {
        User seller = securityUtil.getCurrentUser();

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Product not found"));
        if(!product.getSeller().equals(seller)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "You are not owner of this product");
        }

        List<Order> orders = orderRepository.findByProduct(product);
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setBuyer(order.getBuyer());
            orderResponse.setSeller(order.getSeller());
            orderResponse.setProduct(order.getProduct());
            orderResponse.setPurchaseRequest(order.getPurchaseRequest());
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }
    //lấy tất cả order của người mua
    public List<OrderResponse> getYourOrderBuyer(OrderStatus status) {
        User buyer = securityUtil.getCurrentUser();
        List<Order> orders;
        if(status == null){
            orders = orderRepository.findByBuyer(buyer);
        }else {
            orders = orderRepository.findByBuyerAndStatus(buyer, status);
        }
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setBuyer(order.getBuyer());
            orderResponse.setSeller(order.getSeller());
            orderResponse.setProduct(order.getProduct());
            orderResponse.setPurchaseRequest(order.getPurchaseRequest());
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    //cancel order
    public void cancelOrder(String id) {
        User user = securityUtil.getCurrentUser();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Order not found"));

        if(!order.getBuyer().equals(user) || !order.getSeller().equals(user)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "You are not buyer of this product");
        }
        order.setStatus(OrderStatus.CANCELED);
        order.getPurchaseRequest().setStatus(PurchaseRequestStatus.REJECTED);
        orderRepository.save(order);
    }

    //complete order fixed
    @Transactional
    public void completeOrder(String id) {
        User buyer = securityUtil.getCurrentUser();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Order not found"));

        if(!order.getBuyer().equals(buyer)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "You are not buyer of this product");
        }
        order.setStatus(OrderStatus.COMPLETED);
        order.getPurchaseRequest().getProduct().setStatus(ProductStatus.SOLD);

        orderRepository.save(order);

//        productService.changeStatus(order.getProduct().getId(), ProductStatus.SOLD);

        Product product = order.getProduct();
        List<PurchaseRequest> purchaseRequests = product.getPurchaseRequests();
        for (PurchaseRequest purchaseRequest : purchaseRequests) {
            purchaseRequest.setStatus(PurchaseRequestStatus.REJECTED);
        }

        //TẠO REVIEW PENDING
        Review review = Review.builder()
                .status(ReviewStatus.PENDING)
                .reviewer(buyer)
                .product(product)
                .seller(product.getSeller())
                .reviewType(ReviewType.PURCHASED_PRODUCT)
                .build();
        reviewRepository.save(review);
    }
}
