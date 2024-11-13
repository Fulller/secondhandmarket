package com.secondhandmarket.repository;

import com.secondhandmarket.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
