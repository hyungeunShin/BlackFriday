package com.example.deliveryservice.controller;

import com.example.deliveryservice.dto.DeliveryResponse;
import com.example.deliveryservice.dto.ProcessDeliveryDTO;
import com.example.deliveryservice.dto.RegisterAddressDTO;
import com.example.deliveryservice.dto.UserAddressResponse;
import com.example.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping("/registerAddress")
    public UserAddressResponse registerAddress(@RequestBody RegisterAddressDTO dto) {
        return new UserAddressResponse(deliveryService.addUserAddress(dto.userId(), dto.address(), dto.alias()));
    }

    @PostMapping("/process")
    public DeliveryResponse processDelivery(@RequestBody ProcessDeliveryDTO dto) {
        return new DeliveryResponse(deliveryService.processDelivery(dto.orderId(), dto.productName(), dto.productCount(), dto.address()));
    }

    @GetMapping("/{deliveryId}")
    public DeliveryResponse getDelivery(@PathVariable("deliveryId") Long deliveryId) {
        return new DeliveryResponse(deliveryService.getDelivery(deliveryId));
    }

    @GetMapping("/address/{addressId}")
    public UserAddressResponse getAddress(@PathVariable("addressId") Long addressId) {
        return new UserAddressResponse(deliveryService.getAddress(addressId));
    }

    @GetMapping("/address/users/{userId}")
    public List<UserAddressResponse> getUserAddress(@PathVariable("userId") Long userId) {
        return deliveryService.getUserAddress(userId).stream().map(UserAddressResponse::new).toList();
    }

    @GetMapping("/address/users/{userId}/first-address")
    public UserAddressResponse getUserAddressFirst(@PathVariable("userId") Long userId) {
        return deliveryService.getUserAddress(userId).stream().findFirst().map(UserAddressResponse::new).orElseThrow();
    }
}
