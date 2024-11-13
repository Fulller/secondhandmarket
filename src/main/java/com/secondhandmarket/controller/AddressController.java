package com.secondhandmarket.controller;

import com.secondhandmarket.dto.address.AddressRequetst;
import com.secondhandmarket.model.Address;
import com.secondhandmarket.service.AddressService;
import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.exception.AppException;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<Address>> createAddress(@RequestBody @Valid AddressRequetst addressRequetst) {
        Address createdAddress = addressService.createAddress(addressRequetst);
        ApiResponse<Address> response = ApiResponse.<Address>builder()
                .data(createdAddress)
                .code("address-s-01")
                .message("Address created successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Address>> getAddressById(@PathVariable String addressId) {
        Address address = addressService.getAddressById(addressId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Address not found", "address-e-01"));
    
        ApiResponse<Address> response = ApiResponse.<Address>builder()
                .data(address)
                .code("address-s-02")
                .message("Địa chỉ lấy thành công")
                .build();
    
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Address>> updateAddress(@PathVariable String addressId, @RequestBody @Valid AddressRequetst addressRequetst) {
        Address updatedAddress = addressService.updateAddress(addressId, addressRequetst);
        ApiResponse<Address> response = ApiResponse.<Address>builder()
                .data(updatedAddress)
                .code("address-s-03")
                .message("Address updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable String addressId) {
        addressService.deleteAddress(addressId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code("address-s-04")
                .message("Address deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
