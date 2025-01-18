package com.example.orderservice.feign;

import com.example.orderservice.dto.ProcessDeliveryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "deliveryClient", url = "http://delivery-service:8080")
public interface DeliveryClient {
    @GetMapping(value = "/deliveries/address/users/{userId}/first-address")
    Map<String, Object> getUserAddress(@PathVariable("userId") Long userId);

    @GetMapping(value = "/deliveries/address/{addressId}")
    Map<String, Object> getAddress(@PathVariable("addressId") Long addressId);

    @PostMapping(value = "/deliveries/process")
    Map<String, Object> processDelivery(@RequestBody ProcessDeliveryDTO dto);

    @GetMapping(value = "/deliveries/{deliveryId}")
    Map<String, Object> getDelivery(@PathVariable("deliveryId") Long deliveryId);
}
