package com.secondhandmarket.mapper;

import com.secondhandmarket.dto.order.OrderResponse;
import com.secondhandmarket.model.Order;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse.PurchaseRequestDTO toPurchaseRequestDTO(Order order);
    OrderResponse.UserDTO toUserDTO(User user);
    OrderResponse.ProductDTO toProductDTO(Product product);
    List<OrderResponse> toOrderResponseList(List<Order> orders);
    OrderResponse toOrderResponse(Order order);
}
