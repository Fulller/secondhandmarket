package com.secondhandmarket.dto.product;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.PurchaseRequest;
import com.secondhandmarket.model.User;

public class ProductGetBySellerResponse extends Product {
    @JsonIgnore
    User seller;
    @JsonIgnore
    PurchaseRequest purchaseRequests;
}