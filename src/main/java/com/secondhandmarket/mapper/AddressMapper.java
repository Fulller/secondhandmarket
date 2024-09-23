package com.secondhandmarket.mapper;

import com.secondhandmarket.dto.request.AddressCreationRequest;
import com.secondhandmarket.dto.request.AddressUpdateRequest;
import com.secondhandmarket.dto.response.AddressResponse;
import com.secondhandmarket.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressCreationRequest address);
    void toUpdateAddress(@MappingTarget Address address, AddressUpdateRequest request);
    AddressResponse toAddressResponse(Address address);
}
