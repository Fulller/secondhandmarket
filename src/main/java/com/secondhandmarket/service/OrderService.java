package com.secondhandmarket.service;

import com.secondhandmarket.dto.order.OrderRequest;
import com.secondhandmarket.dto.order.OrderResponse;
import com.secondhandmarket.enums.OrderStatus;
import com.secondhandmarket.enums.ProductStatus;
import com.secondhandmarket.enums.ReviewStatus;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.Order;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.Review;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.OrderRepository;
import com.secondhandmarket.repository.ProductRepository;
import com.secondhandmarket.repository.ReviewRepository;
import com.secondhandmarket.repository.UserRepository;
import com.secondhandmarket.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    private UserRepository userRepository;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private ProductService productService;
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
    public List<OrderResponse> getYourOrderSeller() {
        User seller = securityUtil.getCurrentUser();

        List<Order> orders = orderRepository.findBySeller(seller);
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
        List<OrderResponse> orderResponses = new ArrayList<OrderResponse>();
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
    public List<OrderResponse> getYourOrderBuyer() {
        User buyer = securityUtil.getCurrentUser();

        List<Order> orders = orderRepository.findByBuyer(buyer);
        List<OrderResponse> orderResponses = new ArrayList<OrderResponse>();
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
        User buyer = securityUtil.getCurrentUser();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Order not found"));

        if(!order.getBuyer().equals(buyer)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "You are not buyer of this product");
        }
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    //complete order fixed
    public void completeOrder(String id) {
        User buyer = securityUtil.getCurrentUser();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Order not found"));

        if(!order.getBuyer().equals(buyer)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "You are not buyer of this product");
        }
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        productService.changeStatus(order.getProduct().getId(), ProductStatus.SOLD);
        //TẠO REVIEW PENDING
        Review review = Review.builder()
                .status(ReviewStatus.PENDING)
                .build();
        reviewRepository.save(review);
    }
}
