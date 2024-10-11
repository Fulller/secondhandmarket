package com.secondhandmarket.service;

import com.secondhandmarket.dto.address.AddressRequetst;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.Address;
import com.secondhandmarket.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(AddressRequetst addressrequetst) {
        Address address = new Address();
        address.setProvince(addressrequetst.getProvince());
        address.setDistrict(addressrequetst.getDistrict());
        address.setWard(addressrequetst.getWard());
        address.setDetail(addressrequetst.getDetail());
        return addressRepository.save(address);
    }

    public Optional<Address> getAddressById(String id) {
        return addressRepository.findById(id);
    }

    public Address updateAddress(String id, AddressRequetst addressrequetst) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Address not found", "address-e-01"));
        address.setProvince(addressrequetst.getProvince());
        address.setDistrict(addressrequetst.getDistrict());
        address.setWard(addressrequetst.getWard());
        address.setDetail(addressrequetst.getDetail());
        return addressRepository.save(address);
    }

    public void deleteAddress(String id) {
        if (!addressRepository.existsById(id)) {
            throw new AppException(HttpStatus.NOT_FOUND, "Address not found", "address-e-01");
        }
        addressRepository.deleteById(id);
    }
}
