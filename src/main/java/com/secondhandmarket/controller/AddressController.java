package com.secondhandmarket.controller;

import com.secondhandmarket.model.Address;
import com.secondhandmarket.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Quản lý địa chỉ", description = "Các API cho CRUD địa chỉ")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Thêm địa chỉ mới
    @PostMapping
    @Operation(summary = "Thêm địa chỉ", description = "Thêm một địa chỉ mới vào hệ thống")
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address createdAddress = addressService.createAddress(address);
        return ResponseEntity.ok(createdAddress);
    }

    // Lấy danh sách tất cả địa chỉ
    @GetMapping
    @Operation(summary = "Lấy danh sách địa chỉ", description = "Hiển thị danh sách tất cả địa chỉ")
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    // Lấy địa chỉ theo id
    @GetMapping("/{id}")
    @Operation(summary = "Lấy địa chỉ theo ID", description = "Lấy chi tiết địa chỉ theo mã ID")
    public ResponseEntity<Address> getAddressById(@PathVariable String id) {
        return addressService.getAddressById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cập nhật địa chỉ theo id
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật địa chỉ", description = "Cập nhật thông tin địa chỉ theo ID")
    public ResponseEntity<Address> updateAddress(@PathVariable String id, @RequestBody Address updatedAddress) {
        Address updated = addressService.updateAddress(id, updatedAddress);
        return ResponseEntity.ok(updated);
    }

    // Xóa địa chỉ theo id
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa địa chỉ", description = "Xóa địa chỉ theo mã ID")
    public ResponseEntity<Void> deleteAddress(@PathVariable String id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
