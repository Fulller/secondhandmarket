package com.secondhandmarket.service;

import com.secondhandmarket.model.Address;
import com.secondhandmarket.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Optional<Address> getAddressById(String id) {
        return addressRepository.findById(id);
    }

    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address updateAddress(String id, Address updatedAddress) {
        return addressRepository.findById(id).map(address -> {
            address.setProvince(updatedAddress.getProvince());
            address.setDistrict(updatedAddress.getDistrict());
            address.setWard(updatedAddress.getWard());
            address.setDetail(updatedAddress.getDetail());
            return addressRepository.save(address);
        }).orElseThrow(() -> new RuntimeException("Address not found with id " + id));
    }

    public void deleteAddress(String id) {
        addressRepository.deleteById(id);
    }
}
