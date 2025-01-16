package com.example.deliveryservice.service;

import com.example.deliveryservice.dg.DeliveryAdapter;
import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.entity.UserAddress;
import com.example.deliveryservice.enums.DeliveryStatus;
import com.example.deliveryservice.repository.DeliveryRepository;
import com.example.deliveryservice.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {
    private final UserAddressRepository userAddressRepository;

    private final DeliveryRepository deliveryRepository;

    private final DeliveryAdapter deliveryAdapter;

    @Transactional
    public UserAddress addUserAddress(Long userId, String address, String alias) {
        return userAddressRepository.save(new UserAddress(userId, address, alias));
    }

    @Transactional
    public Delivery processDelivery(Long orderId, String productName, Long productCount, String address) {
        Long refCode = deliveryAdapter.processDelivery(productName, productCount, address);
        return deliveryRepository.save(new Delivery(orderId, productName, productCount, address, refCode, DeliveryStatus.REQUESTED));
    }

    public Delivery getDelivery(Long deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow();
    }

    public UserAddress getAddress(Long addressId) {
        return userAddressRepository.findById(addressId).orElseThrow();
    }

    public List<UserAddress> getUserAddress(Long userId) {
        return userAddressRepository.findByUserId(userId);
    }
}
