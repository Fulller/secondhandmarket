package com.secondhandmarket.service;

import com.secondhandmarket.dto.request.AddressCreationRequest;
import com.secondhandmarket.dto.request.AddressUpdateRequest;
import com.secondhandmarket.dto.request.UserUpdateRequest;
import com.secondhandmarket.dto.response.AddressResponse;
import com.secondhandmarket.dto.response.UserResponse;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.mapper.AddressMapper;
import com.secondhandmarket.mapper.UserMapper;
import com.secondhandmarket.model.Address;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.AddressRepository;
import com.secondhandmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    public UserResponse updateUser(UserUpdateRequest request){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found"));

        userMapper.toUpdateUser(user, request);

        return userMapper.toResponseUser(userRepository.save(user));
    }

    public UserResponse createAddress(AddressCreationRequest request){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found"));

        Address address = addressMapper.toAddress(request);

        addressRepository.save(address);

        user.setAddress(address);

        return userMapper.toResponseUser(userRepository.save(user));
    }

    public AddressResponse userUpdateAddress(AddressUpdateRequest request){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found"));

        Address address = user.getAddress();

        String addressId = address.getId();

        Address addressUpdated = addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Address not found"));

        addressMapper.toUpdateAddress(addressUpdated, request);

        return addressMapper.toAddressResponse(addressRepository.save(addressUpdated));
    }
}
