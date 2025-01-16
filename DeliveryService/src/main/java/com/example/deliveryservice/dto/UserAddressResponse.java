package com.example.deliveryservice.dto;

import com.example.deliveryservice.entity.UserAddress;

public record UserAddressResponse(Long id, Long userId, String address, String alias) {
    public UserAddressResponse(UserAddress userAddress) {
        this(userAddress.getId(), userAddress.getUserId(), userAddress.getAddress(), userAddress.getAlias());
    }
}
